package com.suiyiwen.plugin.idea.servicedoc.bean;

import com.suiyiwen.plugin.idea.servicedoc.parser.*;

/**
 * @author dongxuanliang252
 * @date 2018-12-27 20:17
 */
public enum ServiceDocTag {

    service(false, ServiceProcessor.class, ServiceDocTag.TEXT_REGEXP),
    serviceVersion(false, ServiceVersionProcessor.class, ServiceDocTag.VERSION_REGEXP),
    serviceAuthor(false, ServiceAuthorProcessor.class, ServiceDocTag.TEXT_REGEXP),
    serviceDescription(false, ServiceDescriptionProcessor.class, ServiceDocTag.TEXT_REGEXP),
    serviceGroup(false, ServiceGroupProcessor.class, ServiceDocTag.TEXT_REGEXP),
    serviceName(false, ServiceNameProcessor.class, ServiceDocTag.TEXT_REGEXP),
    serviceParam(true, ServiceParamProcessor.class, ServiceDocTag.FIELD_REGEXP),
    serviceParamExample(true, ServiceParamExampleProcessor.class, ServiceDocTag.EXAMPLE_REGEXP),
    serviceResult(true, ServiceResultProcessor.class, ServiceDocTag.FIELD_REGEXP),
    serviceResultExample(true, ServiceResultExampleProcessor.class, ServiceDocTag.EXAMPLE_REGEXP),
    ;

    private static final String VERSION_REGEXP = "^(\\d+\\.\\d+\\.\\d+)$";
    private static final String TEXT_REGEXP = "^(.*)$";
    private static final String FIELD_REGEXP = "^(\\([^\\(|^\\)]+\\))?\\s*(\\{[^\\{|^\\}]+\\})?\\s*([^\\s]+)\\s*([^\\s]*)\\s*$";
    private static final String EXAMPLE_REGEXP = "^([^\\s]+)\\s+(.+)$";

    private boolean mutiple;
    private Class<? extends AbstractTagProcessor> processor;
    private String regExp;

    ServiceDocTag(boolean mutiple, Class<? extends AbstractTagProcessor> parser, String regExp) {
        this.mutiple = mutiple;
        this.processor = parser;
        this.regExp = regExp;
    }

    public boolean isMutiple() {
        return mutiple;
    }

    public Class<? extends AbstractTagProcessor> getProcessor() {
        return processor;
    }

    public String getRegExp() {
        return regExp;
    }

    public static ServiceDocTag getTag(String name) {
        for (ServiceDocTag tag : ServiceDocTag.values()) {
            if (tag.name().equalsIgnoreCase(name)) {
                return tag;
            }
        }
        return null;
    }

}
