package com.suiyiwen.plugin.idea.servicedoc.bean;

import com.suiyiwen.plugin.idea.servicedoc.bean.servicedoc.*;
import com.suiyiwen.plugin.idea.servicedoc.parser.*;

/**
 * @author dongxuanliang252
 * @date 2018-12-27 20:17
 */
public enum ServiceDocTag {

    service(false, CommonServiceTextProcessor.class, ServiceDocTag.TEXT_REGEXP, Service.class),
    serviceVersion(false, CommonServiceTextProcessor.class, ServiceDocTag.VERSION_REGEXP, ServiceVersion.class),
    serviceAuthor(false, CommonServiceTextProcessor.class, ServiceDocTag.TEXT_REGEXP, ServiceAuthor.class),
    serviceDescription(false, CommonServiceTextProcessor.class, ServiceDocTag.TEXT_REGEXP, ServiceDescription.class),
    serviceGroup(false, CommonServiceTextProcessor.class, ServiceDocTag.TEXT_REGEXP, ServiceGroup.class),
    serviceName(false, CommonServiceTextProcessor.class, ServiceDocTag.TEXT_REGEXP, ServiceName.class),
    serviceParam(true, CommonServiceFieldProcessor.class, ServiceDocTag.FIELD_REGEXP, ServiceParamField.class),
    serviceParamExample(true, CommonExampleProcessor.class, ServiceDocTag.EXAMPLE_REGEXP, ServiceParamExample.class),
    serviceResult(true, CommonServiceFieldProcessor.class, ServiceDocTag.FIELD_REGEXP, ServiceResultField.class),
    serviceResultExample(true, CommonExampleProcessor.class, ServiceDocTag.EXAMPLE_REGEXP, ServiceResultExample.class),
    ;

    private static final String VERSION_REGEXP = "^(\\d+\\.\\d+\\.\\d+)$";
    private static final String TEXT_REGEXP = "^(.*)$";
    private static final String FIELD_REGEXP = "^(\\([^\\(|^\\)]+\\))?\\s*(\\{[^\\{|^\\}]+\\})?\\s*([^\\s]+)\\s*([^\\s]*)\\s*$";
    private static final String EXAMPLE_REGEXP = "^([^\\s]+)\\s+(.+)$";

    private boolean multiple;
    private Class<? extends AbstractTagProcessor> processorCls;
    private String regExp;
    private Class<? extends ServiceDocElement> elementCls;

    ServiceDocTag(boolean multiple, Class<? extends AbstractTagProcessor> processorCls, String regExp, Class<? extends ServiceDocElement> elementCls) {
        this.multiple = multiple;
        this.processorCls = processorCls;
        this.regExp = regExp;
        this.elementCls = elementCls;
    }

    public boolean isMultiple() {
        return multiple;
    }

    public Class<? extends AbstractTagProcessor> getProcessorCls() {
        return processorCls;
    }

    public String getRegExp() {
        return regExp;
    }

    public Class<? extends ServiceDocElement> getElementCls() {
        return elementCls;
    }

    public static ServiceDocTag getTag(String name) {
        for (ServiceDocTag tag : ServiceDocTag.values()) {
            if (tag.name().equalsIgnoreCase(name)) {
                return tag;
            }
        }
        return null;
    }

    public static ServiceDocTag getTagByProcessorCls(Class<? extends AbstractTagProcessor> processorCls) {
        for (ServiceDocTag tag : ServiceDocTag.values()) {
            if (tag.getProcessorCls().equals(processorCls)) {
                return tag;
            }
        }
        return null;
    }

    public static ServiceDocTag getTagByElementCls(Class<? extends ServiceDocElement> elementCls) {
        for (ServiceDocTag tag : ServiceDocTag.values()) {
            if (tag.getElementCls().equals(elementCls)) {
                return tag;
            }
        }
        return null;
    }

}
