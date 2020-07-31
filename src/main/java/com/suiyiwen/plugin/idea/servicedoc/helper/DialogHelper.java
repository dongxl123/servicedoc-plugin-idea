package com.suiyiwen.plugin.idea.servicedoc.helper;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.javadoc.PsiDocComment;
import com.suiyiwen.plugin.idea.servicedoc.bean.dialog.*;
import com.suiyiwen.plugin.idea.servicedoc.bean.servicedoc.ServiceDocCommentBean;
import com.suiyiwen.plugin.idea.servicedoc.component.ServiceDocSettings;
import com.suiyiwen.plugin.idea.servicedoc.component.operation.JavaDocWriter;
import com.suiyiwen.plugin.idea.servicedoc.constant.ServiceDocConstant;
import com.suiyiwen.plugin.idea.servicedoc.ui.ServiceDocGenerateDialog;
import com.suiyiwen.plugin.idea.servicedoc.utils.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author dongxuanliang252
 * @date 2018-12-18 12:05
 */
public enum DialogHelper {

    INSTANCE;

    private JavaDocWriter writer = ServiceManager.getService(JavaDocWriter.class);

    public void showGenerateDialog(DialogModel model, PsiElement psiElement) {
        ServiceDocGenerateDialog dialog = new ServiceDocGenerateDialog(false, model, psiElement);
        dialog.show();
    }

    public DialogModel parseOldDialogModel(PsiDocComment docComment) {
        ServiceDocCommentBean commentBean = ServiceDocElementUtils.INSTANCE.parse(docComment);
        return ConvertUtils.INSTANCE.convertCommentBean2DialogModel(commentBean);
    }

    public void writeJavaDoc(DialogModel model, PsiElement psiElement) {
        processDialogModel(model, psiElement);
        PsiDocComment javaDoc = PsiDocCommentUtils.INSTANCE.createPsiDocComment(buildCommentText(model), psiElement);
        writer.write(javaDoc, psiElement);
    }

    private void processDialogModel(DialogModel model, @NotNull PsiElement psiElement) {
        if (model == null) {
            return;
        }
        List<ParamBean> paramBeanList = model.getParamList();
        if (CollectionUtils.isNotEmpty(paramBeanList)) {
            for (ParamBean paramBean : paramBeanList) {
                filterDialogModelFieldBeanRecursively(paramBean.getFieldList());
                if (model.getGenerateExampleType().equals(1)) {
                    if (StringUtils.isBlank(paramBean.getExample())) {
                        paramBean.setExample(ExampleUtils.INSTANCE.generateExampleString(paramBean.getFieldList(), psiElement));
                    }
                } else if (model.getGenerateExampleType().equals(2)) {
                    paramBean.setExample(ExampleUtils.INSTANCE.generateExampleString(paramBean.getFieldList(), psiElement));
                } else {
                    paramBean.setExample(null);
                }
            }
        }
        ResultBean resultBean = model.getResult();
        if (resultBean != null) {
            filterDialogModelFieldBeanRecursively(resultBean.getFieldList());
            if (model.getGenerateExampleType().equals(1)) {
                if (StringUtils.isBlank(resultBean.getExample())) {
                    resultBean.setExample(ExampleUtils.INSTANCE.generateExampleString(resultBean.getFieldList(), psiElement));
                }
            } else if (model.getGenerateExampleType().equals(2)) {
                resultBean.setExample(ExampleUtils.INSTANCE.generateExampleString(resultBean.getFieldList(), psiElement));
            } else {
                resultBean.setExample(null);
            }
        }
    }

    private void filterDialogModelFieldBeanRecursively(List<FieldBean> fieldBeanList) {
        if (CollectionUtils.isEmpty(fieldBeanList)) {
            return;
        }
        Iterator<FieldBean> itr = fieldBeanList.iterator();
        while (itr.hasNext()) {
            FieldBean fieldBean = itr.next();
            if (!fieldBean.isChecked()) {
                itr.remove();
            }
            filterDialogModelFieldBeanRecursively(fieldBean.getChildFieldList());
        }
    }

    private String buildCommentText(DialogModel model) {
        ServiceDocCommentBean commentBean = ConvertUtils.INSTANCE.convertDialogModel2CommentBean(model);
        return ServiceDocElementUtils.INSTANCE.build(commentBean);
    }

    public DialogModel createNewDialogModel(PsiMethod element) {
        if (element == null) {
            return null;
        }
        DialogModel dialogModel = new DialogModel();
        dialogModel.setParamList(NewDialogModelParseUtils.INSTANCE.parseParamBeanList(element));
        dialogModel.setResult(NewDialogModelParseUtils.INSTANCE.parseResultBean(element));
        dialogModel.setVersion(ServiceDocConstant.DEFAULT_VERSION);
        dialogModel.setServiceTitle(NewDialogModelParseUtils.INSTANCE.parseServiceTitle(element));
        dialogModel.setServiceFunction(NewDialogModelParseUtils.INSTANCE.parseServiceFunction(element));
        ServiceDocSettings settings = ServiceDocSettings.getInstance();
        if (settings != null) {
            dialogModel.setAuthor(settings.getAuthor());
            dialogModel.setVersion(settings.getVersion());
        }
        dialogModel.setGroupName(dialogModel.getServiceTitle());
        dialogModel.setName(NewDialogModelParseUtils.INSTANCE.parseServiceName(element));
        return dialogModel;
    }

    public DialogModel mergeDialogModel(DialogModel newModel, DialogModel oldModel) {
        if (newModel == null && oldModel == null) {
            return null;
        }
        if (newModel == null) {
            return oldModel;
        }
        if (oldModel == null) {
            return newModel;
        }
        DialogModel mergeModel = newModel;
        if (StringUtils.isNotBlank(oldModel.getVersion())) {
            mergeModel.setVersion(oldModel.getVersion());
        }
        if (StringUtils.isNotBlank(oldModel.getAuthor())) {
            mergeModel.setAuthor(oldModel.getAuthor());
        }
        if (StringUtils.isNotBlank(oldModel.getGroupName())) {
            mergeModel.setGroupName(oldModel.getGroupName());
        }
        if (StringUtils.isNotBlank(oldModel.getName())) {
            mergeModel.setName(oldModel.getName());
        }
        if (StringUtils.isBlank(mergeModel.getServiceTitle())) {
            mergeModel.setServiceTitle(oldModel.getServiceTitle());
        }
        if (StringUtils.isBlank(mergeModel.getServiceFunction())) {
            mergeModel.setServiceFunction(oldModel.getServiceFunction());
        }
        if (StringUtils.isNotBlank(oldModel.getDescription())) {
            mergeModel.setDescription(oldModel.getDescription());
        }
        mergeModel.setParamList(mergeExampleList(newModel.getParamList(), oldModel.getParamList()));
        mergeModel.setResult(mergeExample(newModel.getResult(), oldModel.getResult()));
        return mergeModel;
    }

    private <T extends AbstractExampleBean> List<T> mergeExampleList(List<T> newBeanList, List<T> oldBeanList) {
        if (CollectionUtils.isEmpty(newBeanList)) {
            return null;
        }
        if (CollectionUtils.isEmpty(oldBeanList)) {
            return newBeanList;
        }
        Map<String, AbstractExampleBean> oldBeanMap = oldBeanList.stream().collect(Collectors.toMap(o -> o.getTitle(), o -> o));
        for (AbstractExampleBean newBean : newBeanList) {
            String title = newBean.getTitle();
            if (oldBeanMap.containsKey(title)) {
                AbstractExampleBean oldBean = oldBeanMap.get(title);
                mergeExample(newBean, oldBean);
            }
        }
        return newBeanList;
    }

    private <T extends AbstractExampleBean> T mergeExample(T newBean, T oldBean) {
        if (newBean == null) {
            return null;
        }
        if (oldBean == null) {
            return newBean;
        }
        newBean.setFieldList(FieldBeanTreeUtils.INSTANCE.merge(newBean.getFieldList(), oldBean.getFieldList()));
        if (StringUtils.isBlank(newBean.getExample())) {
            newBean.setExample(oldBean.getExample());
        }
        return newBean;
    }

}
