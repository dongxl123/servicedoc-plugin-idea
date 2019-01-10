package com.suiyiwen.plugin.idea.servicedoc.utils;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;

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

    public boolean isCollection(PsiType psiType) {
        return isAssignableFrom(CommonClassNames.JAVA_UTIL_COLLECTION, psiType);
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
}
