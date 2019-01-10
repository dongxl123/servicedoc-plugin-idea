package com.suiyiwen.plugin.idea.servicedoc.ui;

import com.alibaba.fastjson.JSON;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiElement;
import com.suiyiwen.plugin.idea.servicedoc.bean.dialog.DialogModel;
import com.suiyiwen.plugin.idea.servicedoc.bean.dialog.ParamBean;
import com.suiyiwen.plugin.idea.servicedoc.component.ServiceDocSettings;
import com.suiyiwen.plugin.idea.servicedoc.constant.ServiceDocConstant;
import com.suiyiwen.plugin.idea.servicedoc.helper.DialogHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;


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
    private JTable resultTable;
    private DialogModel initModel;
    private JButton testButton;
    private PsiElement psiElement;

    public ServiceDocGenerateDialog(boolean canBeParent, @NotNull DialogModel initModel, @NotNull PsiElement psiElement) {
        super(canBeParent);
        this.initModel = initModel;
        this.psiElement = psiElement;
        init();
        setTitle(ServiceDocConstant.TITLE_GENERATE_DIALOG);
        testButton.addActionListener(e -> {
            System.out.println(JSON.toJSONString(getCurrentModel()));
        });
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
        DialogHelper.INSTANCE.writeJavaDoc(this.initModel, psiElement);
    }

    /**
     * 初始化数据
     */
    private void refreshWithInitModel() {
        renderModel(this.initModel);
    }

    private DialogModel getCurrentModel() {
        DialogModel currentModel = new DialogModel();
        if (StringUtils.isNotBlank(serviceTitleTextFiled.getText())) {
            currentModel.setServiceTitle(serviceTitleTextFiled.getText());
        }
        if (StringUtils.isNotBlank(serviceFunctionTextField.getText())) {
            currentModel.setServiceFunction(serviceFunctionTextField.getText());
        }
        if (StringUtils.isNotBlank(groupNameTextField.getText())) {
            currentModel.setGroupName(groupNameTextField.getText());
        }
        if (StringUtils.isNotBlank(nameTextField.getText())) {
            currentModel.setName(nameTextField.getText());
        }
        if (StringUtils.isNotBlank(versionTextField.getText())) {
            currentModel.setVersion(versionTextField.getText());
        }
        if (StringUtils.isNotBlank(authorTextField.getText())) {
            currentModel.setAuthor(authorTextField.getText());
        }
        if (StringUtils.isNotBlank(description.getText())) {
            currentModel.setDescription(description.getText());
        }
        //        if (CollectionUtils.isNotEmpty(model.getParamList())) {
//            serviceFunctionTextField.setText(model.getParamList());
//        }
//        if (model.getResult() != null) {
//            serviceFunctionTextField.setText(model.getServiceFunction());
//        }
        return currentModel;
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
        if (CollectionUtils.isNotEmpty(model.getParamList())) {
            for (ParamBean paramBean : model.getParamList()) {
                JTextArea textArea = new JTextArea(JSON.toJSONString(paramBean));
                paramTabbedPanel.addTab(paramBean.getTitle(), textArea);
            }
        }
        if (model.getResult() != null) {
            JTextArea textArea = new JTextArea(JSON.toJSONString(model.getResult()));
            resultTable.setToolTipText(JSON.toJSONString(model.getResult()));
            resultTable.add(textArea);
        }
    }
}
