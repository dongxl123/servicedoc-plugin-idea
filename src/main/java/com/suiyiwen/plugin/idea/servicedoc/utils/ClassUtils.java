package com.suiyiwen.plugin.idea.servicedoc.utils;

import com.intellij.psi.PsiType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dongxuanliang252
 * @date 2019-01-04 14:18
 */
public enum ClassUtils {

    INSTANCE;
    private static final Logger log = LoggerFactory.getLogger(ClassUtils.class);

    public <T> T newInstance(Class<T> cls) {
        try {
            return cls.newInstance();
        } catch (InstantiationException e) {
            log.error("InstantiationException", e);
        } catch (IllegalAccessException e) {
            log.error("IllegalAccessException", e);
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
