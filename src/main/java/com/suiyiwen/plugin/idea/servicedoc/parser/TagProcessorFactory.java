package com.suiyiwen.plugin.idea.servicedoc.parser;

import com.suiyiwen.plugin.idea.servicedoc.bean.ServiceDocTag;
import com.suiyiwen.plugin.idea.servicedoc.bean.servicedoc.ServiceDocElement;

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
        Class cls = tag.getProcessorCls();
        try {
            AbstractTagProcessor tagParser = (AbstractTagProcessor) cls.newInstance();
            tagParser.setTag(tag);
            return tagParser;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public TagBuilder getTagBuilder(ServiceDocElement element) {
        if (element == null) {
            return null;
        }
        ServiceDocTag tag = ServiceDocTag.getTagByElementCls(element.getClass());
        if (tag == null) {
            return null;
        }
        Class cls = tag.getProcessorCls();
        try {
            AbstractTagProcessor tagBuilder = (AbstractTagProcessor) cls.newInstance();
            tagBuilder.setTag(tag);
            return tagBuilder;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
