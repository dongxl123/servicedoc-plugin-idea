package com.suiyiwen.plugin.idea.servicedoc.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.suiyiwen.plugin.idea.servicedoc.manager.ServiceDocActionManger;

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
        ServiceDocActionManger actionManager = new ServiceDocActionManger();
        actionManager.showDialog(e);
    }

}
