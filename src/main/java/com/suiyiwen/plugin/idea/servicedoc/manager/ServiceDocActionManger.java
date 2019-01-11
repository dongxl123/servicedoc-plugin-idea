package com.suiyiwen.plugin.idea.servicedoc.manager;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.javadoc.PsiDocComment;
import com.suiyiwen.plugin.idea.servicedoc.bean.dialog.DialogModel;
import com.suiyiwen.plugin.idea.servicedoc.constant.ServiceDocConstant;
import com.suiyiwen.plugin.idea.servicedoc.helper.DialogHelper;

/**
 * @author dongxuanliang252
 * @date 2018-12-18 12:05
 */
public class ServiceDocActionManger {

    public void showDialog(AnActionEvent e) {
        PsiElement psiElement = e.getData(LangDataKeys.PSI_ELEMENT);
        if (psiElement instanceof PsiMethod) {
            PsiMethod psiMethod = (PsiMethod) psiElement;
            PsiDocComment oldDocComment = null;
            PsiElement firstElement = psiMethod.getFirstChild();
            DialogModel oldDialogModel = null;
            if (firstElement instanceof PsiDocComment) {
                oldDocComment = (PsiDocComment) firstElement;
                oldDialogModel = DialogHelper.INSTANCE.parseOldDialogModel(oldDocComment);
            }
            DialogModel newDialogModel = DialogHelper.INSTANCE.createNewDialogModel(psiMethod);
            DialogModel mergeDialogModel = DialogHelper.INSTANCE.mergeDialogModel(newDialogModel, oldDialogModel);
            DialogHelper.INSTANCE.writeJavaDoc(mergeDialogModel, psiElement);
//            DialogHelper.INSTANCE.showGenerateDialog(mergeDialogModel, psiElement);
        } else {
            Messages.showWarningDialog(ServiceDocConstant.NOTIFICATION_CONTENT, ServiceDocConstant.NOTIFICATION_TITLE);
        }
    }
}
