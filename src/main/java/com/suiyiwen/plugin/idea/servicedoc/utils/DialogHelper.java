package com.suiyiwen.plugin.idea.servicedoc.utils;

import com.suiyiwen.plugin.idea.servicedoc.bean.dialog.DialogModel;
import com.suiyiwen.plugin.idea.servicedoc.ui.ServiceDocGenerateDialog;

/**
 * @author dongxuanliang252
 * @date 2018-12-18 12:05
 */
public enum DialogHelper {

    INSTANCE;

    public void showGenerateDialog(DialogModel model) {
        ServiceDocGenerateDialog dialog = new ServiceDocGenerateDialog(false,model);
        dialog.show();
    }




}
