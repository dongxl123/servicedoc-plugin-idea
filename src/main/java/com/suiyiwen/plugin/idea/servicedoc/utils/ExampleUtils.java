package com.suiyiwen.plugin.idea.servicedoc.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.DoubleSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.github.jsonzou.jmockdata.JMockData;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import com.suiyiwen.plugin.idea.servicedoc.bean.FieldType;
import com.suiyiwen.plugin.idea.servicedoc.bean.dialog.FieldBean;
import com.suiyiwen.plugin.idea.servicedoc.constant.ServiceDocConstant;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * @author dongxuanliang252
 * @date 2019-01-25 18:26
 */
public enum ExampleUtils {

    INSTANCE;

    static {
        SerializeConfig config = SerializeConfig.getGlobalInstance();
        config.put(Double.class, new DoubleSerializer("#.##"));
    }

    public String generateExampleString(List<FieldBean> fieldBeanList) {
        if (CollectionUtils.isEmpty(fieldBeanList)) {
            return null;
        }
        JSONObject o = generateExampleRecursively(fieldBeanList);
        if (MapUtils.isEmpty(o)) {
            return null;
        }
        return JSONObject.toJSONString(CollectionUtils.get(o.values(), 0));
    }

    private JSONObject generateExampleRecursively(List<FieldBean> fieldBeanList) {
        if (CollectionUtils.isEmpty(fieldBeanList)) {
            return null;
        }
        JSONObject root = new JSONObject();
        for (FieldBean fieldBean : fieldBeanList) {
            List<FieldBean> childFieldBeanList = fieldBean.getChildFieldList();
            if (FieldType.Array.equals(fieldBean.getFieldType())) {
                JSONArray jsonArray = new JSONArray();
                if (CollectionUtils.isEmpty(childFieldBeanList)) {
                    Object defaultFieldValue = generateDefaultFieldValue(fieldBean);
                    if (defaultFieldValue != null) {
                        jsonArray = JSONArray.parseArray(JSON.toJSONString(defaultFieldValue));
                        if (jsonArray != null && jsonArray.size() > 2) {
                            jsonArray = new JSONArray(jsonArray.subList(0, 2));
                        }
                    }
                } else {
                    JSONObject o = generateExampleRecursively(childFieldBeanList);
                    if (o != null) {
                        jsonArray.add(o);
                    }
                }
                root.put(fieldBean.getName(), jsonArray);
            } else if (CollectionUtils.isEmpty(childFieldBeanList)) {
                Object defaultFieldValue = generateDefaultFieldValue(fieldBean);
                root.put(fieldBean.getName(), defaultFieldValue);
            } else {
                JSONObject o = generateExampleRecursively(childFieldBeanList);
                root.put(fieldBean.getName(), o);
            }
        }
        return root;
    }

    private Object generateDefaultFieldValue(FieldBean fieldBean) {
        if (fieldBean == null || fieldBean.getFieldType() == null || CollectionUtils.isNotEmpty(fieldBean.getChildFieldList())) {
            return null;
        }
        //对于ITERABLE特殊处理
        PsiType psiType = fieldBean.getPsiType();
        if (PsiTypesUtils.INSTANCE.isIterable(psiType)) {
            List<Object> array = new ArrayList<>();
            PsiType[] genericPsiTypes = ((PsiClassType) psiType).getParameters();
            if (ArrayUtils.isNotEmpty(genericPsiTypes)) {
                PsiType innerPsiType = genericPsiTypes[0];
                Object o = generateDefaultFieldValue(innerPsiType);
                if (o != null) {
                    array.add(o);
                }
            }
            return array;
        }
        return generateDefaultFieldValue(psiType);
    }

    private Object generateDefaultFieldValue(PsiType psiType) {
        //ENUM 特殊处理
        if (PsiTypesUtils.INSTANCE.isEnum(psiType)) {
            List<String> enumStrList = new ArrayList<>();
            PsiClass psiClass = ((PsiClassReferenceType) psiType).resolve();
            for (PsiField psiField : psiClass.getFields()) {
                if (psiField instanceof PsiEnumConstant) {
                    enumStrList.add(psiField.getNameIdentifier().getText().trim());
                }
            }
            if (CollectionUtils.isNotEmpty(enumStrList)) {
                Random random = new Random(System.currentTimeMillis());
                return enumStrList.get(random.nextInt(enumStrList.size()));
            }
            return StringUtils.EMPTY;
        }
        Class cls = ClassUtils.INSTANCE.getClass(psiType);
        if (cls == null) {
            return null;
        }
        FieldType fieldType = PsiTypesUtils.INSTANCE.getFieldType(psiType);
        try {
            Object v = JMockData.mock(cls);
            if (v == null && FieldType.Array.equals(fieldType)) {
                return ArrayUtils.EMPTY_OBJECT_ARRAY;
            }
            return v;
        } catch (
                Exception e) {
            if (FieldType.Array.equals(fieldType)) {
                return ArrayUtils.EMPTY_OBJECT_ARRAY;
            }
            return null;
        }
    }

}
