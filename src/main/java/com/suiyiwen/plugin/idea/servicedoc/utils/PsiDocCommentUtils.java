package com.suiyiwen.plugin.idea.servicedoc.utils;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import com.intellij.psi.javadoc.PsiDocComment;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2019-01-04 17:38
 */
public enum PsiDocCommentUtils {

    INSTANCE;

    public PsiDocComment createPsiDocComment(String commentText) {
        Project project = CommonDataKeys.PROJECT.getData(DataManager.getInstance().getDataContext());
        return JavaPsiFacade.getElementFactory(project).createDocCommentFromText(commentText);
    }

}
