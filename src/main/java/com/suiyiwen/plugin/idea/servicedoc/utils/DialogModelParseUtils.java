package com.suiyiwen.plugin.idea.servicedoc.utils;

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
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2019-01-02 13:47
 */
public enum DialogModelParseUtils {

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
        exampleBean.setFieldList(parseRefFieldBeanList(element, ServiceDocConstant.OBJECT_RESOLVE_DEPTH_START));
        List<FieldBean> fieldBeanList = exampleBean.getFieldList();
        if (CollectionUtils.isNotEmpty(fieldBeanList) && fieldBeanList.size() == 1) {
            FieldBean firstFieldBean = fieldBeanList.get(0);
            if (StringUtils.isEmpty(firstFieldBean.getName())) {
                exampleBean.setSpecialFlag(Boolean.TRUE);
                firstFieldBean.setName(exampleBean.getTitle());
            }
        }
        return exampleBean;
    }

    private List<FieldBean> parseRefFieldBeanList(PsiType psiType, int depth) {
        boolean isFirstDepth = ServiceDocConstant.OBJECT_RESOLVE_DEPTH_START == depth;
        List<FieldBean> childFieldList = new ArrayList<>();
        //boxedType, String, enum, map, primitiveType, need not extract array, need not extract Collection
        if (PsiTypesUtils.INSTANCE.isExtractEndPsiType(psiType)) {
            if (isFirstDepth) {
                FieldBean fieldBean = new FieldBean();
                fieldBean.setType(PsiTypesUtils.INSTANCE.getPresentableText(psiType));
                childFieldList.add(fieldBean);
            }
        } else if (PsiTypesUtils.INSTANCE.isCollection(psiType)) {
            PsiType[] genericPsiTypes = ((PsiClassReferenceType) psiType).getParameters();
            if (ArrayUtils.isNotEmpty(genericPsiTypes)) {
                PsiType genericPsiType = genericPsiTypes[0];
                childFieldList = parseRefFieldBeanList(genericPsiType, depth);
            }
        } else if (psiType instanceof PsiClassReferenceType) {
            PsiClass psiClass = PsiTypesUtil.getPsiClass(psiType);
            for (PsiField psiField : psiClass.getAllFields()) {
                if (PsiTypesUtils.INSTANCE.isVariable(psiField)) {
                    childFieldList.add(parseFieldBean(psiField, depth));
                }
            }
        } else if (psiType instanceof PsiArrayType) {
            PsiArrayType arrayType = (PsiArrayType) psiType;
            PsiType componentType = arrayType.getComponentType();
            childFieldList = parseRefFieldBeanList(componentType, depth);
        }
        if (CollectionUtils.isEmpty(childFieldList)) {
            return null;
        }
        return childFieldList;
    }

    private FieldBean parseFieldBean(PsiField psiField, int depth) {
        FieldBean fieldBean = new FieldBean();
        fieldBean.setName(psiField.getName());
        PsiType psiType = psiField.getType();
        fieldBean.setType(PsiTypesUtils.INSTANCE.getPresentableText(psiType));
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
