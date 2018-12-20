package com.suiyiwen.plugin.idea.servicedoc.helper;

import com.suiyiwen.plugin.idea.servicedoc.bean.MetadataBean;
import com.suiyiwen.plugin.idea.servicedoc.ui.ServiceDocGenerateDialog;

/**
 * @author dongxuanliang252
 * @date 2018-12-18 12:05
 */
public enum DialogHelper {

    INSTANCE;

    public void showGenerateDialog(MetadataBean model) {
        ServiceDocGenerateDialog dialog = new ServiceDocGenerateDialog(false);
        dialog.setModel(model);
        dialog.show();
    }


}
