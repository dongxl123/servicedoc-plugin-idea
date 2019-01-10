package com.suiyiwen.plugin.idea.servicedoc.utils;

import com.suiyiwen.plugin.idea.servicedoc.bean.dialog.AbstractExampleBean;
import com.suiyiwen.plugin.idea.servicedoc.bean.dialog.DialogModel;
import com.suiyiwen.plugin.idea.servicedoc.bean.dialog.ParamBean;
import com.suiyiwen.plugin.idea.servicedoc.bean.dialog.ResultBean;
import com.suiyiwen.plugin.idea.servicedoc.bean.servicedoc.*;
import com.suiyiwen.plugin.idea.servicedoc.constant.ServiceDocConstant;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2019-01-02 11:47
 */
public enum ConvertUtils {

    INSTANCE;

    public DialogModel convertCommentBean2DialogModel(ServiceDocCommentBean commentBean) {
        if (commentBean == null) {
            return null;
        }
        DialogModel dialogModel = new DialogModel();
        if (commentBean.getService() != null) {
            dialogModel.setServiceTitle(commentBean.getService().getName());
            dialogModel.setServiceFunction(commentBean.getService().getText());
        }
        if (commentBean.getServiceVersion() != null) {
            dialogModel.setVersion(commentBean.getServiceVersion().getText());
        }
        if (commentBean.getServiceAuthor() != null) {
            dialogModel.setAuthor(commentBean.getServiceAuthor().getText());
        }
        if (commentBean.getServiceGroup() != null) {
            dialogModel.setGroupName(commentBean.getServiceGroup().getText());
        }
        if (commentBean.getServiceName() != null) {
            dialogModel.setName(commentBean.getServiceName().getText());
        }
        if (commentBean.getServiceDescription() != null) {
            dialogModel.setDescription(commentBean.getServiceDescription().getText());
        }
        if (CollectionUtils.isNotEmpty(commentBean.getServiceParamGroupList())) {
            List<ParamBean> paramBeanList = new ArrayList<>();
            for (AbstractServiceFlowTagGroup paramGroup : commentBean.getServiceParamGroupList()) {
                paramBeanList.add(convertTagGroupBean2ExampleBean(paramGroup, ParamBean.class));
            }
            dialogModel.setParamList(paramBeanList);
        }
        AbstractServiceFlowTagGroup resultGroup = commentBean.getServiceResultGroup();
        dialogModel.setResult(convertTagGroupBean2ExampleBean(resultGroup, ResultBean.class));
        return dialogModel;
    }

    private <T extends AbstractExampleBean> T convertTagGroupBean2ExampleBean(AbstractServiceFlowTagGroup tagGroupBean, Class<T> cls) {
        if (tagGroupBean == null) {
            return null;
        }
        T exampleBean = ClassUtils.INSTANCE.newInstance(cls);
        if (exampleBean == null) {
            return null;
        }
        if (tagGroupBean.getExample() != null) {
            exampleBean.setExample(tagGroupBean.getExample().getExample());
            exampleBean.setTitle(tagGroupBean.getExample().getTitle());
        }
        List<AbstractServiceField> serviceFieldList = tagGroupBean.getFieldList();
        if (CollectionUtils.isNotEmpty(serviceFieldList)) {
            AbstractServiceField firstServiceField = serviceFieldList.get(0);
            if (StringUtils.isBlank(exampleBean.getTitle())) {
                exampleBean.setTitle(firstServiceField.getGroup());
            }
            if (serviceFieldList.size() == 1 && firstServiceField.getGroup().equals(firstServiceField.getField())) {
                exampleBean.setSingleFlag(Boolean.TRUE);
            }
            exampleBean.setFieldList(FieldBeanTreeUtils.INSTANCE.toTreeFieldBeanList(serviceFieldList));
        }
        return exampleBean;
    }


    public ServiceDocCommentBean convertDialogModel2CommentBean(DialogModel dialogModel) {
        if (dialogModel == null) {
            return null;
        }
        ServiceDocCommentBean commentBean = new ServiceDocCommentBean();
        if (StringUtils.isNotBlank(dialogModel.getServiceFunction())) {
            Service service = new Service();
            service.setName(dialogModel.getServiceTitle());
            service.setText(dialogModel.getServiceFunction());
            commentBean.setService(service);
        }
        if (StringUtils.isNotBlank(dialogModel.getVersion())) {
            ServiceVersion serviceVersion = new ServiceVersion();
            serviceVersion.setText(dialogModel.getVersion());
            commentBean.setServiceVersion(serviceVersion);
        }
        if (StringUtils.isNotBlank(dialogModel.getAuthor())) {
            ServiceAuthor serviceAuthor = new ServiceAuthor();
            serviceAuthor.setText(dialogModel.getAuthor());
            commentBean.setServiceAuthor(serviceAuthor);
        }
        if (StringUtils.isNotBlank(dialogModel.getGroupName())) {
            ServiceGroup serviceGroup = new ServiceGroup();
            serviceGroup.setText(dialogModel.getGroupName());
            commentBean.setServiceGroup(serviceGroup);
        }

        if (StringUtils.isNotBlank(dialogModel.getName())) {
            ServiceName serviceName = new ServiceName();
            serviceName.setText(dialogModel.getName());
            commentBean.setServiceName(serviceName);
        }
        if (StringUtils.isNotBlank(dialogModel.getDescription())) {
            ServiceDescription serviceDescription = new ServiceDescription();
            serviceDescription.setText(dialogModel.getDescription());
            commentBean.setServiceDescription(serviceDescription);
        }
        if (CollectionUtils.isNotEmpty(dialogModel.getParamList())) {
            List<ServiceParamTagGroup> tagGroupList = new ArrayList<>();
            for (ParamBean paramBean : dialogModel.getParamList()) {
                ServiceParamTagGroup tagGroup = convertExampleBean2TagGroupBean(paramBean, ServiceParamTagGroup.class, ServiceParamExample.class);
                tagGroupList.add(tagGroup);
            }
            commentBean.setServiceParamGroupList(tagGroupList);
        }
        ResultBean resultBean = dialogModel.getResult();
        commentBean.setServiceResultGroup(convertExampleBean2TagGroupBean(resultBean, ServiceResultTagGroup.class, ServiceResultExample.class));
        return commentBean;
    }

    private <T extends AbstractServiceFlowTagGroup, K extends AbstractServiceExample> T convertExampleBean2TagGroupBean(AbstractExampleBean exampleBean, Class<T> tagGroupCls, Class<K> exampleCls) {
        if (exampleBean == null) {
            return null;
        }
        T tagGroupBean = ClassUtils.INSTANCE.newInstance(tagGroupCls);
        if (tagGroupBean == null) {
            return null;
        }
        if (StringUtils.isNotBlank(exampleBean.getExample())) {
            K example = ClassUtils.INSTANCE.newInstance(exampleCls);
            example.setExample(exampleBean.getExample());
            example.setTitle(StringUtils.defaultIfBlank(exampleBean.getTitle(), ServiceDocConstant.TAG_TEXT_DEFAULT_TITLE_RESULT_GROUP));
            tagGroupBean.setExample(example);
        }
        if (exampleBean.getFieldList() != null) {
            tagGroupBean.setFieldList(FieldBeanTreeUtils.INSTANCE.toServiceFieldTagList(StringUtils.defaultIfBlank(exampleBean.getTitle(), ServiceDocConstant.TAG_TEXT_DEFAULT_TITLE_RESULT_GROUP), exampleBean.getFieldList(), ServiceResultField.class));
        }
        return tagGroupBean;
    }
}
