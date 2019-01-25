package com.suiyiwen.plugin.idea.servicedoc.utils;

import com.intellij.psi.*;
import com.intellij.psi.util.PsiFormatUtil;
import com.intellij.psi.util.PsiTypesUtil;
import com.suiyiwen.plugin.idea.servicedoc.bean.dialog.AbstractExampleBean;
import com.suiyiwen.plugin.idea.servicedoc.bean.dialog.FieldBean;
import com.suiyiwen.plugin.idea.servicedoc.bean.dialog.ParamBean;
import com.suiyiwen.plugin.idea.servicedoc.bean.dialog.ResultBean;
import com.suiyiwen.plugin.idea.servicedoc.constant.ServiceDocConstant;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2019-01-02 13:47
 */
public enum NewDialogModelParseUtils {

    INSTANCE;

    public List<ParamBean> parseParamBeanList(PsiMethod element) {
        if (element == null) {
            return null;
        }
        List<ParamBean> paramBeanList = new ArrayList<>();
        for (PsiParameter psiParameter : element.getParameterList().getParameters()) {
            ParamBean paramBean = parseExampleBean(psiParameter.getName(), psiParameter.getType(), ParamBean.class);
            paramBeanList.add(paramBean);
        }
        return paramBeanList;
    }

    public ResultBean parseResultBean(PsiMethod element) {
        if (element == null) {
            return null;
        }
        ResultBean resultBean = parseExampleBean(ServiceDocConstant.TAG_TEXT_DEFAULT_TITLE_RESULT_GROUP, element.getReturnType(), ResultBean.class);
        return resultBean;
    }

    private <T extends AbstractExampleBean> T parseExampleBean(String title, PsiType element, Class<T> cls) {
        T exampleBean = ClassUtils.INSTANCE.newInstance(cls);
        if (element == null) {
            return null;
        }
        exampleBean.setTitle(title);
        exampleBean.setFieldList(parseRefFieldBeanList(element));
        List<FieldBean> fieldBeanList = exampleBean.getFieldList();
        if (CollectionUtils.isNotEmpty(fieldBeanList) && fieldBeanList.size() == 1) {
            FieldBean firstFieldBean = fieldBeanList.get(0);
            if (StringUtils.isEmpty(firstFieldBean.getName())) {
                firstFieldBean.setName(exampleBean.getTitle());
            }
        }
        return exampleBean;
    }

    private List<FieldBean> parseRefFieldBeanList(PsiType psiType) {
        return parseRefFieldBeanList(psiType, ServiceDocConstant.OBJECT_RESOLVE_DEPTH_START);
    }

    private List<FieldBean> parseRefFieldBeanList(PsiType psiType, int depth) {
        boolean isFirstDepth = ServiceDocConstant.OBJECT_RESOLVE_DEPTH_START == depth;
        if (isFirstDepth) {
            depth++;
        }
        List<FieldBean> innerChildFieldList = new ArrayList<>();
        //boxedType, String, enum, map, primitiveType
        if (PsiTypesUtils.INSTANCE.isExtractEndPsiType(psiType)) {
            //不处理
        } else if (PsiTypesUtils.INSTANCE.isIterable(psiType)) {
            PsiType[] genericPsiTypes = ((PsiClassType) psiType).getParameters();
            if (ArrayUtils.isNotEmpty(genericPsiTypes)) {
                innerChildFieldList = parseRefFieldBeanList(genericPsiTypes[0], depth);
            }
        } else if (psiType instanceof PsiClassType) {
            PsiClass psiClass = PsiTypesUtil.getPsiClass(psiType);
            PsiSubstitutor psiSubstitutor = ((PsiClassType) psiType).resolveGenerics().getSubstitutor();
            for (PsiField psiField : psiClass.getAllFields()) {
                if (PsiFieldUtils.INSTANCE.isVariable(psiField)) {
                    innerChildFieldList.add(parseFieldBean(psiField, psiSubstitutor, depth));
                }
            }
        } else if (psiType instanceof PsiArrayType) {
            PsiArrayType arrayType = (PsiArrayType) psiType;
            PsiType componentType = arrayType.getComponentType();
            innerChildFieldList = parseRefFieldBeanList(componentType, depth);
        }
        if (isFirstDepth) {
            List<FieldBean> retChildFieldList = new ArrayList<>();
            FieldBean fieldBean = new FieldBean();
            fieldBean.setType(PsiTypesUtils.INSTANCE.getPresentableText(psiType));
            if (PsiTypesUtils.INSTANCE.isEnum(psiType)) {
                fieldBean.setDescription(PsiTypesUtils.INSTANCE.generateEnumDescription(psiType));
            }
            if (CollectionUtils.isNotEmpty(innerChildFieldList)) {
                fieldBean.setChildFieldList(innerChildFieldList);
            }
            retChildFieldList.add(fieldBean);
            return retChildFieldList;
        } else if (CollectionUtils.isNotEmpty(innerChildFieldList)) {
            return innerChildFieldList;
        }
        return null;
    }

    private FieldBean parseFieldBean(PsiField psiField, PsiSubstitutor psiSubstitutor, int depth) {
        FieldBean fieldBean = new FieldBean();
        fieldBean.setName(psiField.getName());
        PsiType psiType = PsiTypesUtils.INSTANCE.createGenericPsiType(psiField.getType(), psiSubstitutor);
        fieldBean.setType(PsiTypesUtils.INSTANCE.getPresentableText(psiType));
        fieldBean.setDescription(PsiFieldUtils.INSTANCE.getFieldDescription(psiField));
        if (StringUtils.isBlank(fieldBean.getDescription()) && PsiTypesUtils.INSTANCE.isEnum(psiType)) {
            fieldBean.setDescription(PsiTypesUtils.INSTANCE.generateEnumDescription(psiType));
        }
        if (depth >= ServiceDocConstant.OBJECT_RESOLVE_MAX_DEPTH) {
            return fieldBean;
        }
        List<FieldBean> childFieldList = parseRefFieldBeanList(psiType, depth + 1);
        if (CollectionUtils.isNotEmpty(childFieldList)) {
            fieldBean.setChildFieldList(childFieldList);
        }
        return fieldBean;
    }

    public String parseServiceTitle(PsiMethod element) {
        if (element == null) {
            return null;
        }
        return PsiFormatUtil.formatClass(element.getContainingClass(), PsiFormatUtil.SHOW_NAME);
    }

    public String parseServiceFunction(PsiMethod element) {
        if (element == null) {
            return null;
        }
        return PsiFormatUtil.formatMethod(element, PsiSubstitutor.EMPTY,
                PsiFormatUtil.SHOW_TYPE | PsiFormatUtil.SHOW_NAME | PsiFormatUtil.SHOW_PARAMETERS | PsiFormatUtil.SHOW_THROWS,
                PsiFormatUtil.SHOW_TYPE | PsiFormatUtil.SHOW_NAME);
    }

    public String parseServiceName(PsiMethod element) {
        if (element == null) {
            return null;
        }
        return PsiFormatUtil.formatMethod(element, PsiSubstitutor.EMPTY,
                PsiFormatUtil.SHOW_NAME, PsiFormatUtil.SHOW_NAME);
    }

}
