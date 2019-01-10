package com.suiyiwen.plugin.idea.servicedoc.helper;

import com.intellij.psi.PsiMethod;
import com.intellij.psi.javadoc.PsiDocComment;
import com.suiyiwen.plugin.idea.servicedoc.bean.dialog.AbstractExampleBean;
import com.suiyiwen.plugin.idea.servicedoc.bean.dialog.DialogModel;
import com.suiyiwen.plugin.idea.servicedoc.bean.servicedoc.ServiceDocCommentBean;
import com.suiyiwen.plugin.idea.servicedoc.component.ServiceDocSettings;
import com.suiyiwen.plugin.idea.servicedoc.constant.ServiceDocConstant;
import com.suiyiwen.plugin.idea.servicedoc.ui.ServiceDocGenerateDialog;
import com.suiyiwen.plugin.idea.servicedoc.utils.ConvertUtils;
import com.suiyiwen.plugin.idea.servicedoc.utils.FieldBeanTreeUtils;
import com.suiyiwen.plugin.idea.servicedoc.utils.PsiElementParseUtils;
import com.suiyiwen.plugin.idea.servicedoc.utils.ServiceDoElementUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author dongxuanliang252
 * @date 2018-12-18 12:05
 */
public enum DialogHelper {

    INSTANCE;

    public void showGenerateDialog(DialogModel model) {
        ServiceDocGenerateDialog dialog = new ServiceDocGenerateDialog(false, model);
        dialog.show();
    }

    public DialogModel parse(PsiDocComment docComment) {
        ServiceDocCommentBean commentBean = ServiceDoElementUtils.INSTANCE.parse(docComment);
        return ConvertUtils.INSTANCE.convertCommentBean2DialogModel(commentBean);
    }

    public String build(DialogModel model) {
        ServiceDocCommentBean commentBean = ConvertUtils.INSTANCE.convertDialogModel2CommentBean(model);
        return ServiceDoElementUtils.INSTANCE.build(commentBean);
    }

    public DialogModel createNewDialogModel(PsiMethod element) {
        if (element == null) {
            return null;
        }
        DialogModel dialogModel = new DialogModel();
        dialogModel.setParamList(PsiElementParseUtils.INSTANCE.parseParamBeanList(element));
        dialogModel.setResult(PsiElementParseUtils.INSTANCE.parseResultBean(element));
        dialogModel.setVersion(ServiceDocConstant.DEFAULT_VERSION);
        dialogModel.setServiceTitle(PsiElementParseUtils.INSTANCE.parseServiceTitle(element));
        dialogModel.setServiceFunction(PsiElementParseUtils.INSTANCE.parseServiceFunction(element));
        ServiceDocSettings settings = ServiceDocSettings.getInstance();
        if (settings != null) {
            dialogModel.setAuthor(settings.getAuthor());
        }
        dialogModel.setGroupName(dialogModel.getServiceTitle());
        dialogModel.setName(dialogModel.getServiceFunction());
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
            newModel.setVersion(oldModel.getVersion());
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
