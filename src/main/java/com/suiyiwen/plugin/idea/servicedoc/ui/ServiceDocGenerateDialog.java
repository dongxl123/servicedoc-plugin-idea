package com.suiyiwen.plugin.idea.servicedoc.ui;

import com.intellij.openapi.ui.DialogWrapper;
import com.suiyiwen.plugin.idea.servicedoc.bean.dialog.DialogModel;
import com.suiyiwen.plugin.idea.servicedoc.constant.ServiceDocConstant;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;


public class ServiceDocGenerateDialog extends DialogWrapper {

    private JPanel contentPanel;
    private JTextField functionTextField;
    private JFormattedTextField groupNameTextField;
    private JFormattedTextField nameTextField;
    private JFormattedTextField versionTextField;
    private JFormattedTextField authorTextField;
    private JTextArea description;
    private JTabbedPane paramTabbedPanel;
    private JTable resultTable;
    private DialogModel initModel;

    public ServiceDocGenerateDialog(boolean canBeParent, DialogModel initModel) {
        super(canBeParent);
        this.initModel = initModel;
        init();
        setTitle(ServiceDocConstant.TITLE_GENERATE_DIALOG);
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        refreshWithInitModel();
        return contentPanel;
    }

    @Nullable
    @Override
    public JComponent getPreferredFocusedComponent() {
        return super.getPreferredFocusedComponent();
    }

    @Nullable
    @Override
    protected String getDimensionServiceKey() {
        return "serviceId";
    }


    /**
     * 初始化数据
     */
    private void refreshWithInitModel() {
        renderModel(this.initModel);
    }

    private DialogModel getCurrentModel() {
        DialogModel newModel = new DialogModel();
        return newModel;
    }

    private void renderModel(DialogModel model) {
        if (model == null) {
            return;
        }
        if (StringUtils.isNotBlank(model.getFunction())) {
            functionTextField.setText(model.getFunction());
        }
        if (StringUtils.isNotBlank(model.getGroupName())) {
            groupNameTextField.setText(model.getGroupName());
        }
        if (StringUtils.isNotBlank(model.getName())) {
            nameTextField.setText(model.getName());
        }
        if (StringUtils.isNotBlank(model.getVersion())) {
            versionTextField.setText(model.getVersion());
        }
        if (StringUtils.isNotBlank(model.getAuthor())) {
            authorTextField.setText(model.getAuthor());
        }
        if (StringUtils.isNotBlank(model.getDescription())) {
            description.setText(model.getDescription());
        }
//        if (CollectionUtils.isNotEmpty(model.getParamList())) {
//            functionTextField.setText(model.getParamList());
//        }
//        if (model.getResult() != null) {
//            functionTextField.setText(model.getFunction());
//        }
    }

    private void createUIComponents() {

        // TODO: place custom component creation code here
    }
}
