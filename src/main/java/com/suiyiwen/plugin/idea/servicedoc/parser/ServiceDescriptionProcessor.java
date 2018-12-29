package com.suiyiwen.plugin.idea.servicedoc.parser;

import com.suiyiwen.plugin.idea.servicedoc.bean.servicedoc.ServiceDescription;
import com.suiyiwen.plugin.idea.servicedoc.bean.servicedoc.ServiceDocElement;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2018-12-27 19:05
 */
public class ServiceDescriptionProcessor extends AbstractTagProcessor {

    @Override
    public ServiceDocElement parse(List<String> textList) {
        ServiceDescription element = new ServiceDescription();
        if (CollectionUtils.isNotEmpty(textList)) {
            element.setDescription(textList.get(0));
        }
        return element;
    }

    @Override
    public String buildValue(ServiceDocElement element) {
        if (element instanceof ServiceDescription) {
            ServiceDescription tElement = (ServiceDescription) element;
            return StringUtils.stripToEmpty(tElement.getDescription());
        }
        return StringUtils.EMPTY;
    }

}
