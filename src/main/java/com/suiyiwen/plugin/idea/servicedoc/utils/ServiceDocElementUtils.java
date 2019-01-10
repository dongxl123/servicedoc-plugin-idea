package com.suiyiwen.plugin.idea.servicedoc.utils;

import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocTag;
import com.suiyiwen.plugin.idea.servicedoc.bean.ServiceDocTag;
import com.suiyiwen.plugin.idea.servicedoc.bean.javadoc.JavaDocElements;
import com.suiyiwen.plugin.idea.servicedoc.bean.servicedoc.*;
import com.suiyiwen.plugin.idea.servicedoc.constant.ServiceDocConstant;
import com.suiyiwen.plugin.idea.servicedoc.parser.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dongxuanliang252
 * @date 2018-12-18 12:05
 */
public enum ServiceDocElementUtils {

    INSTANCE;

    public ServiceDocCommentBean parse(PsiDocComment docComment) {
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

    public String build(ServiceDocCommentBean commentBean) {
        if (commentBean == null) {
            return StringUtils.EMPTY;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(JavaDocElements.STARTING.getPresentation()).append(JavaDocElements.LINE_START.getPresentation());
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
        sb.append(JavaDocElements.NEW_LINE.getPresentation()).append(JavaDocElements.LINE_START.getPresentation()).append(JavaDocElements.ENDING.getPresentation());
        return sb.toString();
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

    private String buildServiceDocElementComment(ServiceDocElement element) {
        TagBuilder tagBuilder = TagProcessorFactory.INSTANCE.getTagBuilder(element);
        if (tagBuilder == null) {
            return StringUtils.EMPTY;
        }
        return tagBuilder.build(element);
    }

}
