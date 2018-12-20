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
import com.suiyiwen.plugin.idea.servicedoc.constant.ServiceDocConstant;
import com.suiyiwen.plugin.idea.servicedoc.helper.DialogHelper;

/**
 * @author dongxuanliang252
 * @date 2018-12-17 11:09
 */
public class GenerateAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project product = e.getProject();
        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        PsiElement psiElement = e.getData(LangDataKeys.PSI_ELEMENT);
        if (psiElement instanceof PsiMethod) {

        } else {
            Notification notification = new Notification(ServiceDocConstant.NOTIFICATION_GROUP_DISPLAY_ID, ServiceDocConstant.NOTIFICATION_TITLE,  ServiceDocConstant.NOTIFICATION_CONTENT, NotificationType.WARNING);
            Notifications.Bus.notify(notification);
        }
        System.out.println("serviceDoc plugin action 1");
        DialogHelper.INSTANCE.showGenerateDialog(null);
        System.out.println(123456789);
    }

}
