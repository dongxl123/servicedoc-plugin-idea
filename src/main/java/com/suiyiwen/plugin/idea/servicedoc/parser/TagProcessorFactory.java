package com.suiyiwen.plugin.idea.servicedoc.parser;

import com.suiyiwen.plugin.idea.servicedoc.bean.ServiceDocTag;
import com.suiyiwen.plugin.idea.servicedoc.bean.servicedoc.ServiceDocElement;
import com.suiyiwen.plugin.idea.servicedoc.utils.ClassUtils;

/**
 * @author dongxuanliang252
 * @date 2018-12-28 12:05
 */
public enum TagProcessorFactory {

    INSTANCE;

    public TagParser getTagParserByName(String name) {
        ServiceDocTag tag = ServiceDocTag.getTag(name);
        if (tag == null) {
            return null;
        }
        Class<? extends AbstractTagProcessor> cls = tag.getProcessorCls();
        AbstractTagProcessor tagParser = ClassUtils.INSTANCE.newInstance(cls);
        if (tagParser == null) {
            return null;
        }
        tagParser.setTag(tag);
        return tagParser;
    }

    public TagBuilder getTagBuilder(ServiceDocElement element) {
        if (element == null) {
            return null;
        }
        ServiceDocTag tag = ServiceDocTag.getTagByElementCls(element.getClass());
        if (tag == null) {
            return null;
        }
        Class<? extends AbstractTagProcessor> cls = tag.getProcessorCls();
        AbstractTagProcessor tagBuilder = ClassUtils.INSTANCE.newInstance(cls);
        if (tagBuilder == null) {
            return null;
        }
        tagBuilder.setTag(tag);
        return tagBuilder;
    }

}
