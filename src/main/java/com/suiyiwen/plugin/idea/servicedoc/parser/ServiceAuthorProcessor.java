package com.suiyiwen.plugin.idea.servicedoc.parser;

import com.suiyiwen.plugin.idea.servicedoc.bean.servicedoc.ServiceAuthor;
import com.suiyiwen.plugin.idea.servicedoc.bean.servicedoc.ServiceDocElement;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2018-12-27 19:05
 */
public class ServiceAuthorProcessor extends AbstractTagProcessor {

    @Override
    public String buildValue(ServiceDocElement element) {
        if (element instanceof ServiceAuthor) {
            ServiceAuthor tElement = (ServiceAuthor) element;
            return StringUtils.stripToEmpty(tElement.getAuthor());
        }
        return StringUtils.EMPTY;
    }

    @Override
    public ServiceDocElement parse(List<String> textList) {
        ServiceAuthor element = new ServiceAuthor();
        if (CollectionUtils.isNotEmpty(textList)) {
            element.setAuthor(textList.get(0));
        }
        return element;
    }
}
