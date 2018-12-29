package com.suiyiwen.plugin.idea.servicedoc.parser;

import com.suiyiwen.plugin.idea.servicedoc.bean.servicedoc.ServiceDocElement;
import com.suiyiwen.plugin.idea.servicedoc.bean.servicedoc.ServiceName;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2018-12-27 19:05
 */
public class ServiceNameProcessor extends AbstractTagProcessor {

    @Override
    public ServiceDocElement parse(List<String> textList) {
        ServiceName element = new ServiceName();
        if (CollectionUtils.isNotEmpty(textList)) {
            element.setName(textList.get(0));
        }
        return element;
    }

    @Override
    public String buildValue(ServiceDocElement element) {
        if (element instanceof ServiceName) {
            ServiceName tElement = (ServiceName) element;
            return StringUtils.stripToEmpty(tElement.getName());
        }
        return StringUtils.EMPTY;
    }
}
