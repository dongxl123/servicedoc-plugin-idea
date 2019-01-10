package com.suiyiwen.plugin.idea.servicedoc.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.javadoc.PsiDocComment;
import com.suiyiwen.plugin.idea.servicedoc.bean.dialog.DialogModel;
import com.suiyiwen.plugin.idea.servicedoc.constant.ServiceDocConstant;
import com.suiyiwen.plugin.idea.servicedoc.helper.DialogHelper;

/**
 * @author dongxuanliang252
 * @date 2018-12-17 11:09
 */
public class GenerateAction extends AnAction {

    /**
     * @param e
     */
    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        PsiElement psiElement = e.getData(LangDataKeys.PSI_ELEMENT);
        if (psiElement instanceof PsiMethod) {
            PsiMethod psiMethod = (PsiMethod) psiElement;
            PsiDocComment oldDocComment = null;
            PsiElement firstElement = psiMethod.getFirstChild();
            DialogModel oldDialogModel = null;
            if (firstElement instanceof PsiDocComment) {
                oldDocComment = (PsiDocComment) firstElement;
                oldDialogModel = DialogHelper.INSTANCE.parse(oldDocComment);
            }
            DialogModel newDialogModel = DialogHelper.INSTANCE.createNewDialogModel(psiMethod);
            DialogModel mergeDialogModel = DialogHelper.INSTANCE.mergeDialogModel(newDialogModel, oldDialogModel);
            DialogHelper.INSTANCE.showGenerateDialog(mergeDialogModel);
            System.out.println("serviceDoc plugin action 1");
        } else {
            Messages.showWarningDialog(ServiceDocConstant.NOTIFICATION_CONTENT, ServiceDocConstant.NOTIFICATION_TITLE);
        }
    }

}
