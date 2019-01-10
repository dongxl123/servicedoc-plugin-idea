package com.suiyiwen.plugin.idea.servicedoc.utils;

import com.alibaba.fastjson.JSON;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import com.intellij.psi.util.PsiFormatUtil;
import com.intellij.psi.util.PsiTypesUtil;
import com.suiyiwen.plugin.idea.servicedoc.bean.dialog.AbstractExampleBean;
import com.suiyiwen.plugin.idea.servicedoc.bean.dialog.FieldBean;
import com.suiyiwen.plugin.idea.servicedoc.bean.dialog.ParamBean;
import com.suiyiwen.plugin.idea.servicedoc.bean.dialog.ResultBean;
import com.suiyiwen.plugin.idea.servicedoc.constant.ServiceDocConstant;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2019-01-02 13:47
 */
public enum PsiElementParseUtils {

    INSTANCE;

    public List<ParamBean> parseParamBeanList(PsiMethod element) {
        if (element == null) {
            return null;
        }
        List<ParamBean> paramBeanList = new ArrayList<>();
        for (PsiParameter psiParameter : element.getParameterList().getParameters()) {
            ParamBean paramBean = parseExampleBean(psiParameter.getType(), ParamBean.class);
            paramBean.setTitle(psiParameter.getName());
            paramBeanList.add(paramBean);
        }
        return paramBeanList;
    }

    public ResultBean parseResultBean(PsiMethod element) {
        if (element == null) {
            return null;
        }
        ResultBean resultBean = parseExampleBean(element.getReturnType(), ResultBean.class);
        resultBean.setTitle(ServiceDocConstant.TAG_TEXT_DEFAULT_TITLE_RESULT_GROUP);
        return resultBean;
    }

    private <T extends AbstractExampleBean> T parseExampleBean(PsiType element, Class<T> cls) {
        T exampleBean = ClassUtils.INSTANCE.newInstance(cls);
        if (element == null) {
            return null;
        }
        exampleBean.setFieldList(parseRefFieldBeanList(element, ServiceDocConstant.OBJECT_RESOLVE_DEPTH_START));
        List<FieldBean> fieldBeanList = exampleBean.getFieldList();
        if (CollectionUtils.isNotEmpty(fieldBeanList) && fieldBeanList.size() == 1) {
            FieldBean firstFieldBean = fieldBeanList.get(0);
            if (StringUtils.isEmpty(firstFieldBean.getName())) {
                exampleBean.setSingleFlag(Boolean.TRUE);
                firstFieldBean.setName(exampleBean.getTitle());
            }
        }
        return exampleBean;
    }

    private List<FieldBean> parseRefFieldBeanList(PsiType psiType, int depth) {
        List<FieldBean> childFieldList = new ArrayList<>();
        if (psiType instanceof PsiClassReferenceType) {
            if (PsiTypesUtils.INSTANCE.isBoxedType(psiType) || PsiTypesUtils.INSTANCE.isString(psiType)) {
                FieldBean fieldBean = new FieldBean();
                fieldBean.setType(psiType.getPresentableText());
                childFieldList.add(fieldBean);
            } else if (PsiTypesUtils.INSTANCE.isCollection(psiType)) {
                 System.out.println(111);
            } else {
                PsiClass psiClass = PsiTypesUtil.getPsiClass(psiType);
                for (PsiField psiField : psiClass.getAllFields()) {
                    if (PsiTypesUtils.INSTANCE.isVariable(psiField)) {
                        childFieldList.add(parseFieldBean(psiField, depth));
                    }
                }
            }
        } else if (psiType instanceof PsiArrayType) {
            PsiArrayType arrayType = (PsiArrayType) psiType;
            PsiType componentType = arrayType.getComponentType();
            FieldBean fieldBean = new FieldBean();
            fieldBean.setType(psiType.getPresentableText());
            fieldBean.setChildFieldList(parseRefFieldBeanList(componentType, depth + 1));
            childFieldList.add(fieldBean);
        } else if (psiType instanceof PsiPrimitiveType) {
            FieldBean fieldBean = new FieldBean();
            fieldBean.setType(psiType.getPresentableText());
            childFieldList.add(fieldBean);
        }
        return childFieldList;
    }

    private FieldBean parseFieldBean(PsiField psiField, int depth) {
        FieldBean fieldBean = new FieldBean();
        fieldBean.setName(psiField.getName());
        PsiType psiType = psiField.getType();
        fieldBean.setType(PsiTypesUtil.boxIfPossible(psiType.getPresentableText()));
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

}
