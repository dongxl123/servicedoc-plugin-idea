package com.suiyiwen.plugin.idea.servicedoc.parser;

import com.suiyiwen.plugin.idea.servicedoc.bean.servicedoc.Service;
import com.suiyiwen.plugin.idea.servicedoc.bean.servicedoc.ServiceDocElement;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2018-12-27 19:05
 */
public class ServiceProcessor extends AbstractTagProcessor {

    @Override
    public ServiceDocElement parse(List<String> textList) {
        Service element = new Service();
        if (CollectionUtils.isNotEmpty(textList)) {
            element.setFunction(textList.get(0));
        }
        return element;
    }

    @Override
    public String buildValue(ServiceDocElement element) {
        if (element instanceof Service) {
            Service tElement = (Service) element;
            return StringUtils.stripToEmpty(tElement.getFunction());
        }
        return StringUtils.EMPTY;
    }
}
