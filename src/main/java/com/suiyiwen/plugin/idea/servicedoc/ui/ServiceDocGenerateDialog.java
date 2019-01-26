package com.suiyiwen.plugin.idea.servicedoc.ui;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiElement;
import com.intellij.ui.components.JBScrollPane;
import com.suiyiwen.plugin.idea.servicedoc.bean.dialog.DialogModel;
import com.suiyiwen.plugin.idea.servicedoc.bean.dialog.ParamBean;
import com.suiyiwen.plugin.idea.servicedoc.component.ServiceDocSettings;
import com.suiyiwen.plugin.idea.servicedoc.constant.ServiceDocConstant;
import com.suiyiwen.plugin.idea.servicedoc.helper.DialogHelper;
import com.suiyiwen.plugin.idea.servicedoc.utils.FieldBeanTreeUtils;
import com.suiyiwen.plugin.idea.servicedoc.utils.TreeTableUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jdesktop.swingx.JXTreeTable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;


public class ServiceDocGenerateDialog extends DialogWrapper {

    private JPanel contentPanel;
    private JTextField serviceFunctionTextField;
    private JTextField serviceTitleTextFiled;
    private JFormattedTextField groupNameTextField;
    private JFormattedTextField nameTextField;
    private JFormattedTextField versionTextField;
    private JFormattedTextField authorTextField;
    private JTextArea description;
    private JTabbedPane paramTabbedPanel;
    private JBScrollPane resultPanel;
    private DialogModel model;
    private PsiElement psiElement;

    public ServiceDocGenerateDialog(boolean canBeParent, @NotNull DialogModel initModel, @NotNull PsiElement psiElement) {
        super(canBeParent);
        this.model = initModel;
        this.psiElement = psiElement;
        init();
        setTitle(ServiceDocConstant.TITLE_GENERATE_DIALOG);
        setSize(800,800);
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

    @Override
    public void doOKAction() {
        saveSettings();
        generateComment();
        super.doOKAction();
    }

    private void saveSettings() {
        ServiceDocSettings settings = ServiceDocSettings.getInstance();
        if (settings == null) {
            return;
        }
        if (StringUtils.isNotBlank(authorTextField.getText())) {
            settings.setAuthor(authorTextField.getText());
        }
    }

    private void generateComment() {
        DialogHelper.INSTANCE.writeJavaDoc(getCurrentModel(), psiElement);
    }

    /**
     * 初始化数据
     */
    private void refreshWithInitModel() {
        renderModel(this.model);
    }

    private DialogModel getCurrentModel() {
        if (StringUtils.isNotBlank(serviceTitleTextFiled.getText())) {
            model.setServiceTitle(serviceTitleTextFiled.getText());
        }
        if (StringUtils.isNotBlank(serviceFunctionTextField.getText())) {
            model.setServiceFunction(serviceFunctionTextField.getText());
        }
        if (StringUtils.isNotBlank(groupNameTextField.getText())) {
            model.setGroupName(groupNameTextField.getText());
        }
        if (StringUtils.isNotBlank(nameTextField.getText())) {
            model.setName(nameTextField.getText());
        }
        if (StringUtils.isNotBlank(versionTextField.getText())) {
            model.setVersion(versionTextField.getText());
        }
        if (StringUtils.isNotBlank(authorTextField.getText())) {
            model.setAuthor(authorTextField.getText());
        }
        if (StringUtils.isNotBlank(description.getText())) {
            model.setDescription(description.getText());
        }
        return model;
    }

    private void renderModel(DialogModel model) {
        if (model == null) {
            return;
        }
        if (StringUtils.isNotBlank(model.getServiceTitle())) {
            serviceTitleTextFiled.setText(model.getServiceTitle());
        }
        if (StringUtils.isNotBlank(model.getServiceFunction())) {
            serviceFunctionTextField.setText(model.getServiceFunction());
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
        int paramMaxLines = 0;
        if (CollectionUtils.isNotEmpty(model.getParamList())) {
            for (ParamBean paramBean : model.getParamList()) {
                JXTreeTable treeTable = TreeTableUtils.INSTANCE.createTreeTable(paramBean);
                JBScrollPane jbScrollPane = new JBScrollPane();
                jbScrollPane.setViewportView(treeTable);
                paramTabbedPanel.addTab(paramBean.getTitle(), jbScrollPane);
                int count = FieldBeanTreeUtils.INSTANCE.getLines(paramBean.getFieldList());
                paramMaxLines = Math.max(count, paramMaxLines);
            }
        }
        paramTabbedPanel.setMinimumSize(new Dimension(-1, Math.min(Math.max(paramMaxLines * ServiceDocConstant.UI_LINE_MIN_SIZE, ServiceDocConstant.UI_MIN_SIZE + ServiceDocConstant.UI_TITLE_SIZE), ServiceDocConstant.UI_MAX_SIZE)));
        paramTabbedPanel.setMaximumSize(new Dimension(-1, Math.min(paramMaxLines * ServiceDocConstant.UI_LINE_MAX_SIZE, ServiceDocConstant.UI_MAX_SIZE + ServiceDocConstant.UI_TITLE_SIZE)));
        paramTabbedPanel.setPreferredSize(new Dimension(-1, Math.min(paramMaxLines * ServiceDocConstant.UI_LINE_PREFER_SIZE, ServiceDocConstant.UI_MAX_SIZE + ServiceDocConstant.UI_TITLE_SIZE)));
        int resultLines = 0;
        if (model.getResult() != null) {
            JXTreeTable treeTable = TreeTableUtils.INSTANCE.createTreeTable(model.getResult());
            resultPanel.setViewportView(treeTable);
            resultLines = FieldBeanTreeUtils.INSTANCE.getLines(model.getResult().getFieldList());
        }
        resultPanel.setMinimumSize(new Dimension(-1, Math.min(Math.max(resultLines * ServiceDocConstant.UI_LINE_MIN_SIZE, ServiceDocConstant.UI_MIN_SIZE), ServiceDocConstant.UI_MAX_SIZE)));
        resultPanel.setMaximumSize(new Dimension(-1, Math.min(resultLines * ServiceDocConstant.UI_LINE_MAX_SIZE, ServiceDocConstant.UI_MAX_SIZE)));
        resultPanel.setPreferredSize(new Dimension(-1, Math.min(resultLines * ServiceDocConstant.UI_LINE_PREFER_SIZE, ServiceDocConstant.UI_MAX_SIZE)));
    }
}
