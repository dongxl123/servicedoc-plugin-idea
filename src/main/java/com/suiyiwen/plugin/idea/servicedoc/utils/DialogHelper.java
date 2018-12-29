package com.suiyiwen.plugin.idea.servicedoc.utils;

import com.suiyiwen.plugin.idea.servicedoc.bean.dialog.DialogBean;
import com.suiyiwen.plugin.idea.servicedoc.ui.ServiceDocGenerateDialog;

/**
 * @author dongxuanliang252
 * @date 2018-12-18 12:05
 */
public enum DialogHelper {

    INSTANCE;

    public void showGenerateDialog(DialogBean model) {
        ServiceDocGenerateDialog dialog = new ServiceDocGenerateDialog(false);
        dialog.setModel(model);
        dialog.show();
    }


}
