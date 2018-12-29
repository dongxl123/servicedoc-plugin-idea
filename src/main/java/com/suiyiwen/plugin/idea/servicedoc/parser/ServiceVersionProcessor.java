package com.suiyiwen.plugin.idea.servicedoc.parser;

import com.suiyiwen.plugin.idea.servicedoc.bean.servicedoc.ServiceDocElement;
import com.suiyiwen.plugin.idea.servicedoc.bean.servicedoc.ServiceVersion;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2018-12-27 19:05
 */
public class ServiceVersionProcessor extends AbstractTagProcessor {

    @Override
    public ServiceDocElement parse(List<String> textList) {
        ServiceVersion element = new ServiceVersion();
        if (CollectionUtils.isNotEmpty(textList)) {
            element.setVersion(textList.get(0));
        }
        return element;
    }

    @Override
    public String buildValue(ServiceDocElement element) {
        if (element instanceof ServiceVersion) {
            ServiceVersion tElement = (ServiceVersion) element;
            return StringUtils.stripToEmpty(tElement.getVersion());
        }
        return StringUtils.EMPTY;
    }
}
