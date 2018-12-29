package com.suiyiwen.plugin.idea.servicedoc.action;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.javadoc.PsiDocComment;
import com.suiyiwen.plugin.idea.servicedoc.constant.ServiceDocConstant;
import com.suiyiwen.plugin.idea.servicedoc.utils.DialogHelper;
import com.suiyiwen.plugin.idea.servicedoc.utils.ServiceDocUtils;

/**
 * @author dongxuanliang252
 * @date 2018-12-17 11:09
 */
public class GenerateAction extends AnAction {

    /**
     * dfdfdfa1232131
     *
     * @param e
     */
    @Override
    public void actionPerformed(AnActionEvent e) {
        //dfdsaffa
        Project product = e.getProject();
        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        PsiElement psiElement = e.getData(LangDataKeys.PSI_ELEMENT);
        if (psiElement instanceof PsiMethod) {
            PsiMethod psiMethod = (PsiMethod) psiElement;
            PsiDocComment oldDocComment = null;
            PsiElement firstElement = psiMethod.getFirstChild();
            if (firstElement instanceof PsiDocComment) {
                oldDocComment = (PsiDocComment) firstElement;
                ServiceDocUtils.INSTANCE.parsePsiDocComment(oldDocComment);
                System.out.println(11);
            }
            System.out.println("serviceDoc plugin action 1");
           // DialogHelper.INSTANCE.showGenerateDialog(null);
        } else {
            Notification notification = new Notification(ServiceDocConstant.NOTIFICATION_GROUP_DISPLAY_ID, ServiceDocConstant.NOTIFICATION_TITLE, ServiceDocConstant.NOTIFICATION_CONTENT, NotificationType.WARNING);
            Notifications.Bus.notify(notification);
        }
        System.out.println(123456789);
    }

}
