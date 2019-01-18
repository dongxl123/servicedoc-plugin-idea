package com.suiyiwen.plugin.idea.servicedoc.utils;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import com.intellij.psi.javadoc.PsiDocComment;
import com.suiyiwen.plugin.idea.servicedoc.bean.javadoc.JavaDocElements;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2019-01-04 17:38
 */
public enum PsiTypesUtils {

    INSTANCE;

    private static final List<String> boxedTypes = new ArrayList<>();

    static {
        boxedTypes.add(CommonClassNames.JAVA_LANG_BOOLEAN);
        boxedTypes.add(CommonClassNames.JAVA_LANG_BYTE);
        boxedTypes.add(CommonClassNames.JAVA_LANG_CHARACTER);
        boxedTypes.add(CommonClassNames.JAVA_LANG_SHORT);
        boxedTypes.add(CommonClassNames.JAVA_LANG_INTEGER);
        boxedTypes.add(CommonClassNames.JAVA_LANG_LONG);
        boxedTypes.add(CommonClassNames.JAVA_LANG_DOUBLE);
        boxedTypes.add(CommonClassNames.JAVA_LANG_FLOAT);
    }


    public boolean isVariable(PsiField psiField) {
        PsiModifierList psiModifierList = psiField.getModifierList();
        if (psiModifierList.hasModifierProperty(PsiModifier.PUBLIC) || psiModifierList.hasModifierProperty(PsiModifier.STATIC) || psiModifierList.hasModifierProperty(PsiModifier.FINAL) || psiModifierList.hasModifierProperty(PsiModifier.TRANSIENT)) {
            return false;
        }
        return true;
    }

    public boolean isBoxedType(PsiType psiType) {
        String canonicalText = psiType.getCanonicalText();
        return boxedTypes.contains(canonicalText);
    }

    public boolean isString(PsiType psiType) {
        String canonicalText = psiType.getCanonicalText();
        return CommonClassNames.JAVA_LANG_STRING.equals(canonicalText);
    }

    public boolean isIterable(PsiType psiType) {
        return isAssignableFrom(CommonClassNames.JAVA_LANG_ITERABLE, psiType) || isAssignableFrom(CommonClassNames.JAVA_UTIL_ITERATOR, psiType);
    }

    public boolean isEnum(PsiType psiType) {
        return isAssignableFrom(CommonClassNames.JAVA_LANG_ENUM, psiType);
    }

    public boolean isMap(PsiType psiType) {
        return isAssignableFrom(CommonClassNames.JAVA_UTIL_MAP, psiType);
    }

    private boolean isAssignableFrom(String fQClassName, PsiType psiType) {
        PsiType fqType = createPsiType(fQClassName);
        return fqType.isAssignableFrom(psiType);
    }

    public PsiType createPsiType(String fQClassName) {
        Project project = CommonDataKeys.PROJECT.getData(DataManager.getInstance().getDataContext());
        return JavaPsiFacade.getElementFactory(project).createTypeByFQClassName(fQClassName);
    }

    public boolean isExtractEndPsiType(PsiType psiType) {
        if (psiType instanceof PsiClassType) {
            if (PsiTypesUtils.INSTANCE.isBoxedType(psiType) || PsiTypesUtils.INSTANCE.isString(psiType) || PsiTypesUtils.INSTANCE.isMap(psiType) || PsiTypesUtils.INSTANCE.isEnum(psiType)) {
                return true;
            }
        } else if (psiType instanceof PsiPrimitiveType) {
            return true;
        }
        return false;
    }

    public boolean hasGenericTypes(PsiType psiType) {
        if (psiType instanceof PsiClassType) {
            PsiType[] genericPsiTypes = ((PsiClassType) psiType).getParameters();
            return ArrayUtils.isNotEmpty(genericPsiTypes);
        }
        return false;
    }


    public String getPresentableText(PsiType psiType) {
        String presentableText = psiType.getPresentableText();
        if (StringUtils.isNotBlank(presentableText)) {
            return StringUtils.remove(presentableText, StringUtils.SPACE);
        }
        return CommonClassNames.JAVA_LANG_OBJECT_SHORT;
    }

    public String getFieldDescription(PsiField psiField) {
        PsiDocComment psiDocComment = psiField.getDocComment();
        if (psiDocComment == null) {
            return null;
        }
        PsiElement[] descriptions = psiDocComment.getDescriptionElements();
        if (ArrayUtils.isEmpty(descriptions)) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (PsiElement description : descriptions) {
            sb.append(description.getText());
        }
        return StringUtils.remove(sb.toString(), JavaDocElements.NEW_LINE.getPresentation());
    }

    public PsiType createGenericPsiType(PsiType psiType, PsiSubstitutor psiSubstitutor) {
        if (psiType instanceof PsiClassType && MapUtils.isNotEmpty(psiSubstitutor.getSubstitutionMap())) {
            return psiSubstitutor.substitute(psiType);
        }
        return psiType;
    }

    public boolean isPublicMethod(PsiMethod element) {
        PsiModifierList psiModifierList = element.getModifierList();
        if (psiModifierList.hasModifierProperty(PsiModifier.PUBLIC)) {
            return true;
        }
        return false;
    }

    public String generateEnumDescription(PsiType psiType) {
        if (isEnum(psiType)) {
            StringBuilder sb = new StringBuilder();
            PsiClass psiClass = ((PsiClassReferenceType) psiType).resolve();
            for (PsiField psiField : psiClass.getFields()) {
                if (psiField instanceof PsiEnumConstant) {
                    sb.append(psiField.getText()).append(JavaDocElements.NEW_LINE.getPresentation());
                }
            }
            return sb.toString();
        }
        return null;
    }
}
