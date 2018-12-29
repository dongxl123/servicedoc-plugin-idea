package com.suiyiwen.plugin.idea.servicedoc.utils;

import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocTag;
import com.suiyiwen.plugin.idea.servicedoc.bean.ServiceDocTag;
import com.suiyiwen.plugin.idea.servicedoc.bean.dialog.DialogBean;
import com.suiyiwen.plugin.idea.servicedoc.bean.servicedoc.ServiceDocCommentBean;
import com.suiyiwen.plugin.idea.servicedoc.bean.servicedoc.ServiceDocElement;
import com.suiyiwen.plugin.idea.servicedoc.constant.ServiceDocConstant;
import com.suiyiwen.plugin.idea.servicedoc.parser.TagBuilder;
import com.suiyiwen.plugin.idea.servicedoc.parser.TagParser;
import com.suiyiwen.plugin.idea.servicedoc.parser.TagProcessorFactory;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author dongxuanliang252
 * @date 2018-12-18 12:05
 */
public enum ServiceDocUtils {

    INSTANCE;

    public DialogBean convertCommentBean2DialogBean(ServiceDocCommentBean commentBean) {

        return null;
    }

    public ServiceDocCommentBean convertDialogBean2CommentBean(DialogBean dialogBean) {

        return null;
    }

    @NotNull
    public ServiceDocCommentBean parsePsiDocComment(@NotNull PsiDocComment docComment) {
        Map<String, List<ServiceDocElement>> tags = findServiceDocTags(docComment);
        System.out.println(111);
        return null;
    }

    @NotNull
    private Map<String, List<ServiceDocElement>> findServiceDocTags(@NotNull PsiDocComment docComment) {
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
            if (serviceDocTag.isMutiple() || !tags.containsKey(name)) {
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


    @NotNull
    public String buildComment(ServiceDocCommentBean commentBean) {
        if (commentBean == null) {
            return StringUtils.EMPTY;
        }
        StringBuilder sb = new StringBuilder();
        if (commentBean.getService() != null) {
            sb.append(buildServiceDocElementComment(commentBean.getService())).append(System.lineSeparator());
        }
        if (commentBean.getServiceVersion() != null) {
            sb.append(buildServiceDocElementComment(commentBean.getServiceVersion())).append(System.lineSeparator());
        }
        if (commentBean.getServiceAuthor() != null) {
            sb.append(buildServiceDocElementComment(commentBean.getServiceAuthor())).append(System.lineSeparator());
        }
        if (commentBean.getServiceGroup() != null) {
            sb.append(buildServiceDocElementComment(commentBean.getServiceGroup())).append(System.lineSeparator());
        }
        if (commentBean.getServiceName() != null) {
            sb.append(buildServiceDocElementComment(commentBean.getServiceName())).append(System.lineSeparator());
        }
        if (commentBean.getServiceDescription() != null) {
            sb.append(buildServiceDocElementComment(commentBean.getServiceDescription())).append(System.lineSeparator());
        }
        if (CollectionUtils.isNotEmpty(commentBean.getParamGroupList())) {

        }
        if (commentBean.getServiceResultGroup() != null) {

        }
        return sb.toString();
    }

    private String buildServiceDocElementComment(ServiceDocElement element) {
        TagBuilder tagBuilder = TagProcessorFactory.INSTANCE.getTagBuilderByName(element.getTag());
        return tagBuilder.build(element);
    }


}
