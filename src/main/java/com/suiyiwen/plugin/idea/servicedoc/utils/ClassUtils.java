package com.suiyiwen.plugin.idea.servicedoc.utils;

import com.intellij.psi.PsiType;

/**
 * @author dongxuanliang252
 * @date 2019-01-04 14:18
 */
public enum ClassUtils {

    INSTANCE;

    public <T> T newInstance(Class<T> cls) {
        try {
            return cls.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Class getClass(PsiType psiType) {
        String text = psiType.getCanonicalText();
        try {
            if (PsiTypesUtils.INSTANCE.isMap(psiType)) {
                return Object.class;
            }
            return org.apache.commons.lang3.ClassUtils.getClass(text);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

}
