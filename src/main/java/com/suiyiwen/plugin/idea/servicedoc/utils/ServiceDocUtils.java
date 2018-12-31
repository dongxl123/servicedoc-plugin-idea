package com.suiyiwen.plugin.idea.servicedoc.utils;

import com.intellij.psi.PsiElement;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocTag;
import com.suiyiwen.plugin.idea.servicedoc.bean.ServiceDocTag;
import com.suiyiwen.plugin.idea.servicedoc.bean.dialog.DialogModel;
import com.suiyiwen.plugin.idea.servicedoc.bean.dialog.ParamBean;
import com.suiyiwen.plugin.idea.servicedoc.bean.dialog.ResultBean;
import com.suiyiwen.plugin.idea.servicedoc.bean.servicedoc.*;
import com.suiyiwen.plugin.idea.servicedoc.constant.ServiceDocConstant;
import com.suiyiwen.plugin.idea.servicedoc.parser.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.ref.PhantomReference;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dongxuanliang252
 * @date 2018-12-18 12:05
 */
public enum ServiceDocUtils {

    INSTANCE;

    public DialogModel parseDialogModel(PsiDocComment docComment) {
        ServiceDocCommentBean commentBean = parseServiceDocCommentBean(docComment);
        return convertCommentBean2DialogModel(commentBean);
    }

    public String buildComment(DialogModel model) {
        ServiceDocCommentBean commentBean = convertDialogModel2CommentBean(model);
        return buildComment(commentBean);
    }

    private ServiceDocCommentBean parseServiceDocCommentBean(PsiDocComment docComment) {
        Map<String, List<ServiceDocElement>> tags = findServiceDocTags(docComment);
        if (MapUtils.isEmpty(tags)) {
            return null;
        }
        //参数、返回结果特殊处理
        Map<String, List<ServiceDocElement>> paramsTags = new LinkedHashMap<>();
        Map<String, List<ServiceDocElement>> resultTags = new LinkedHashMap<>();
        ServiceDocCommentBean commentBean = new ServiceDocCommentBean();
        for (String tagName : tags.keySet()) {
            List<ServiceDocElement> elements = tags.get(tagName);
            if (CollectionUtils.isEmpty(elements)) {
                continue;
            }
            ServiceDocElement firstElement = elements.get(0);
            ServiceDocTag docTag = ServiceDocTag.getTagByElementCls(firstElement.getClass());
            if (docTag == null) {
                continue;
            }
            if (ServiceDocTag.service.equals(docTag)) {
                commentBean.setService((Service) firstElement);
            } else if (ServiceDocTag.serviceAuthor.equals(docTag)) {
                commentBean.setServiceAuthor((ServiceAuthor) firstElement);
            } else if (ServiceDocTag.serviceVersion.equals(docTag)) {
                commentBean.setServiceVersion((ServiceVersion) firstElement);
            } else if (ServiceDocTag.serviceGroup.equals(docTag)) {
                commentBean.setServiceGroup((ServiceGroup) firstElement);
            } else if (ServiceDocTag.serviceName.equals(docTag)) {
                commentBean.setServiceName((ServiceName) firstElement);
            } else if (ServiceDocTag.serviceDescription.equals(docTag)) {
                commentBean.setServiceDescription((ServiceDescription) firstElement);
            } else if (ServiceDocTag.serviceParam.equals(docTag) || ServiceDocTag.serviceParamExample.equals(docTag)) {
                putIntoMap(docTag, elements, paramsTags);
            } else if (ServiceDocTag.serviceResult.equals(docTag) || ServiceDocTag.serviceResultExample.equals(docTag)) {
                putIntoMap(docTag, elements, resultTags);
            }
        }
        TagGroupParser tagGroupParser = new ServiceFlowTagGroupProcessor();
        if (MapUtils.isNotEmpty(paramsTags)) {
            List<ServiceParamTagGroup> paramGroupList = new ArrayList<>();
            for (String groupName : paramsTags.keySet()) {
                List<ServiceDocElement> elements = paramsTags.get(groupName);
                ServiceParamTagGroup paramGroup = (ServiceParamTagGroup) tagGroupParser.parse(elements);
                if (paramGroup != null) {
                    paramGroupList.add(paramGroup);
                }
            }
            commentBean.setServiceParamGroupList(paramGroupList);
        }
        if (MapUtils.isNotEmpty(resultTags)) {
            List<ServiceResultTagGroup> resultGroupList = new ArrayList<>();
            for (String groupName : resultTags.keySet()) {
                List<ServiceDocElement> elements = resultTags.get(groupName);
                ServiceResultTagGroup resultGroup = (ServiceResultTagGroup) tagGroupParser.parse(elements);
                if (resultGroup != null) {
                    resultGroupList.add(resultGroup);
                }
            }
            commentBean.setServiceResultGroup(resultGroupList.get(0));
        }
        return commentBean;
    }

    private void putIntoMap(ServiceDocTag docTag, List<ServiceDocElement> elements, Map<String, List<ServiceDocElement>> map) {
        if (docTag == null || CollectionUtils.isEmpty(elements)) {
            return;
        }
        if (map == null) {
            map = new LinkedHashMap<>();
        }
        for (ServiceDocElement element : elements) {
            String groupName = null;
            if (ServiceDocTag.serviceParam.equals(docTag) || ServiceDocTag.serviceResult.equals(docTag)) {
                AbstractServiceField tElement = (AbstractServiceField) element;
                groupName = tElement.getGroup();
            } else if (ServiceDocTag.serviceParamExample.equals(docTag) || ServiceDocTag.serviceResultExample.equals(docTag)) {
                AbstractServiceExample tElement = (AbstractServiceExample) element;
                groupName = tElement.getTitle();
            }
            if (StringUtils.isNotBlank(groupName)) {
                if (!map.containsKey(groupName)) {
                    map.put(groupName, new ArrayList<>());
                }
                map.get(groupName).add(element);
            }
        }
    }

    private Map<String, List<ServiceDocElement>> findServiceDocTags(PsiDocComment docComment) {
        if (docComment == null) {
            return null;
        }
        //LinkedHashMap保证存储顺序
        Map<String, List<ServiceDocElement>> tags = new LinkedHashMap<>();
        PsiDocTag[] docTags = docComment.getTags();
        for (PsiDocTag docTag : docTags) {
            String name = docTag.getName();
            if (!name.startsWith(ServiceDocConstant.TAG_PREFIX)) {
                continue;
            }
            ServiceDocTag serviceDocTag = ServiceDocTag.getTag(name);
            if (serviceDocTag == null) {
                continue;
            }
            if (serviceDocTag.isMultiple() || !tags.containsKey(name)) {
                TagParser parser = TagProcessorFactory.INSTANCE.getTagParserByName(name);
                if (parser != null) {
                    String tagText = docTag.getText();
                    tagText = StringUtils.removeStart(tagText, String.format("@%s", name));
                    tagText = StringUtils.remove(tagText, "*");
                    tagText = tagText.trim();
                    if (!tags.containsKey(name)) {
                        tags.put(name, new ArrayList<>());
                    }
                    tags.get(name).add(parser.parse(tagText));
                }
            }
        }
        return tags;
    }

    private String buildComment(ServiceDocCommentBean commentBean) {
        if (commentBean == null) {
            return StringUtils.EMPTY;
        }
        StringBuilder sb = new StringBuilder();
        if (commentBean.getService() != null) {
            sb.append(buildServiceDocElementComment(commentBean.getService()));
        }
        if (commentBean.getServiceVersion() != null) {
            sb.append(buildServiceDocElementComment(commentBean.getServiceVersion()));
        }
        if (commentBean.getServiceAuthor() != null) {
            sb.append(buildServiceDocElementComment(commentBean.getServiceAuthor()));
        }
        if (commentBean.getServiceGroup() != null) {
            sb.append(buildServiceDocElementComment(commentBean.getServiceGroup()));
        }
        if (commentBean.getServiceName() != null) {
            sb.append(buildServiceDocElementComment(commentBean.getServiceName()));
        }
        if (commentBean.getServiceDescription() != null) {
            sb.append(buildServiceDocElementComment(commentBean.getServiceDescription()));
        }
        TagGroupBuilder tagGroupBuilder = new ServiceFlowTagGroupProcessor();
        if (CollectionUtils.isNotEmpty(commentBean.getServiceParamGroupList())) {
            for (ServiceParamTagGroup serviceParamGroup : commentBean.getServiceParamGroupList()) {
                sb.append(tagGroupBuilder.build(serviceParamGroup));
            }
        }
        ServiceResultTagGroup serviceResultGroup = commentBean.getServiceResultGroup();
        if (serviceResultGroup != null) {
            sb.append(tagGroupBuilder.build(serviceResultGroup));
        }
        return sb.toString();
    }

    private String buildServiceDocElementComment(ServiceDocElement element) {
        TagBuilder tagBuilder = TagProcessorFactory.INSTANCE.getTagBuilder(element);
        if (tagBuilder == null) {
            return StringUtils.EMPTY;
        }
        return tagBuilder.build(element);
    }

    private DialogModel convertCommentBean2DialogModel(ServiceDocCommentBean commentBean) {
        if (commentBean == null) {
            return null;
        }
        DialogModel dialogModel = new DialogModel();
        if (commentBean.getService() != null) {
            dialogModel.setFunction(commentBean.getService().getText());
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
            for (ServiceParamTagGroup paramGroup : commentBean.getServiceParamGroupList()) {
                ParamBean paramBean = new ParamBean();
                if (paramGroup.getExample() != null) {
                    paramBean.setExample(paramGroup.getExample().getExample());
                    paramBean.setParamTitle(paramGroup.getExample().getTitle());
                }
                if (CollectionUtils.isNotEmpty(paramGroup.getFieldList())) {
                    if (StringUtils.isBlank(paramBean.getParamTitle())) {
                        paramBean.setParamTitle(paramGroup.getFieldList().get(0).getGroup());
                    }
                    paramBean.setFieldList(FiledBeanTreeUtils.INSTANCE.toTreeList(paramGroup.getFieldList()));
                }
                paramBeanList.add(paramBean);
            }
            dialogModel.setParamList(paramBeanList);
        }
        ServiceResultTagGroup resultGroup = commentBean.getServiceResultGroup();
        if (resultGroup != null) {
            ResultBean resultBean = new ResultBean();
            if (resultGroup.getExample() != null) {
                resultBean.setExample(resultGroup.getExample().getExample());
            }
            if (CollectionUtils.isNotEmpty(resultGroup.getFieldList())) {
                resultBean.setFieldList(FiledBeanTreeUtils.INSTANCE.toTreeList(resultGroup.getFieldList()));
            }
            dialogModel.setResult(resultBean);
        }
        return dialogModel;
    }

    private ServiceDocCommentBean convertDialogModel2CommentBean(DialogModel dialogModel) {
        if (dialogModel == null) {
            return null;
        }
        ServiceDocCommentBean commentBean = new ServiceDocCommentBean();
        if (StringUtils.isNotBlank(dialogModel.getFunction())) {
            Service service = new Service();
            service.setText(dialogModel.getFunction());
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
                ServiceParamTagGroup tagGroup = new ServiceParamTagGroup();
                if (StringUtils.isNotBlank(paramBean.getExample())) {
                    AbstractServiceExample example = new ServiceParamExample();
                    example.setExample(paramBean.getExample());
                    example.setTitle(paramBean.getParamTitle());
                    tagGroup.setExample(example);
                }
                if (CollectionUtils.isNotEmpty(paramBean.getFieldList())) {
                    tagGroup.setFieldList(FiledBeanTreeUtils.INSTANCE.toServiceFieldTagList(paramBean.getParamTitle(), paramBean.getFieldList(), ServiceParamField.class));
                }
                tagGroupList.add(tagGroup);
            }
            commentBean.setServiceParamGroupList(tagGroupList);
        }
        ResultBean resultBean = dialogModel.getResult();
        if (resultBean != null) {
            ServiceResultTagGroup tagGroup = new ServiceResultTagGroup();
            if (StringUtils.isNotBlank(resultBean.getExample())) {
                AbstractServiceExample example = new ServiceResultExample();
                example.setExample(resultBean.getExample());
                example.setTitle(ServiceDocConstant.TAG_TEXT_DEFAULT_TITLE_RESULT_GROUP);
                tagGroup.setExample(example);
            }
            if (CollectionUtils.isNotEmpty(resultBean.getFieldList())) {
                tagGroup.setFieldList(FiledBeanTreeUtils.INSTANCE.toServiceFieldTagList(ServiceDocConstant.TAG_TEXT_DEFAULT_TITLE_RESULT_GROUP, resultBean.getFieldList(), ServiceResultField.class));
            }
            commentBean.setServiceResultGroup(tagGroup);
        }
        return commentBean;

    }

    public DialogModel getCurrentDialogModel(PsiElement psiElement) {
        return null;
    }
}
