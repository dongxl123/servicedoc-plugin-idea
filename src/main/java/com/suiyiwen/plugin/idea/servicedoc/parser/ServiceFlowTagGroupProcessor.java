package com.suiyiwen.plugin.idea.servicedoc.parser;

import com.suiyiwen.plugin.idea.servicedoc.bean.ServiceDocTag;
import com.suiyiwen.plugin.idea.servicedoc.bean.servicedoc.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2018-12-31 11:48
 */
public class ServiceFlowTagGroupProcessor implements TagGroupParser, TagGroupBuilder {


    @Override
    public ServiceDocGroupElement parse(List<ServiceDocElement> elements) {
        AbstractServiceFlowTagGroup group = determineNewInstance(elements);
        if (group == null) {
            return null;
        }
        for (ServiceDocElement element : elements) {
            if (element instanceof AbstractServiceField) {
                if (group.getFieldList() == null) {
                    group.setFieldList(new ArrayList<>());
                }
                group.getFieldList().add((AbstractServiceField) element);
            } else if (element instanceof AbstractServiceExample) {
                group.setExample((AbstractServiceExample) element);
            }
        }
        return group;
    }

    private AbstractServiceFlowTagGroup determineNewInstance(List<ServiceDocElement> elements) {
        if (CollectionUtils.isEmpty(elements)) {
            return null;
        }
        ServiceDocElement element = elements.get(0);
        ServiceDocTag docTag = ServiceDocTag.getTagByElementCls(element.getClass());
        if (docTag == null) {
            return null;
        }
        if (ServiceDocTag.serviceParam.equals(docTag) || ServiceDocTag.serviceParamExample.equals(docTag)) {
            return new ServiceParamTagGroup();
        } else if (ServiceDocTag.serviceResult.equals(docTag) || ServiceDocTag.serviceResultExample.equals(docTag)) {
            return new ServiceResultTagGroup();
        }
        return null;
    }


    @Override
    public String build(ServiceDocGroupElement element) {
        if (element instanceof AbstractServiceFlowTagGroup) {
            AbstractServiceFlowTagGroup tagGroup = (AbstractServiceFlowTagGroup) element;
            StringBuilder sb = new StringBuilder();
            if (CollectionUtils.isNotEmpty(tagGroup.getFieldList())) {
                for (AbstractServiceField serviceField : tagGroup.getFieldList()) {
                    TagBuilder tagBuilder = TagProcessorFactory.INSTANCE.getTagBuilder(serviceField);
                    if (tagBuilder != null) {
                        sb.append(tagBuilder.build(serviceField));
                    }
                }
            }
            if (tagGroup.getExample() != null) {
                TagBuilder tagBuilder = TagProcessorFactory.INSTANCE.getTagBuilder(tagGroup.getExample());
                if (tagBuilder != null) {
                    sb.append(tagBuilder.build(tagGroup.getExample()));
                }
            }
            return sb.toString();
        }
        return StringUtils.EMPTY;
    }


}
