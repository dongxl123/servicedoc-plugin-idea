package com.suiyiwen.plugin.idea.servicedoc.ui;

import com.intellij.openapi.ui.DialogWrapper;
import com.suiyiwen.plugin.idea.servicedoc.bean.MetadataBean;
import com.suiyiwen.plugin.idea.servicedoc.constant.ServiceDocConstant;
import kotlin.Metadata;
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
    private MetadataBean model;

    public ServiceDocGenerateDialog(boolean canBeParent) {
        super(canBeParent);
        init();
        setTitle(ServiceDocConstant.TITLE_GENERATE_DIALOG);
    }

    public void setModel(MetadataBean model) {
        this.model = model;
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
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


    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
