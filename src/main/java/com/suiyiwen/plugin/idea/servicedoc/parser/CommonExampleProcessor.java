package com.suiyiwen.plugin.idea.servicedoc.parser;

import com.suiyiwen.plugin.idea.servicedoc.bean.servicedoc.AbstractServiceExample;
import com.suiyiwen.plugin.idea.servicedoc.bean.servicedoc.ServiceDocElement;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2018-12-27 19:05
 */
public class CommonExampleProcessor extends AbstractTagProcessor {

    @Override
    public ServiceDocElement parse(List<String> textList) {
        AbstractServiceExample element = newElementInstance();
        if (element == null) {
            return null;
        }
        if (CollectionUtils.isNotEmpty(textList)) {
            element.setTitle(textList.get(0));
            element.setExample(textList.get(1));
        }
        return element;
    }

    @Override
    public String buildValue(ServiceDocElement element) {
        if (element instanceof AbstractServiceExample) {
            AbstractServiceExample tElement = (AbstractServiceExample) element;
            StringBuilder sb = new StringBuilder();
            sb.append(StringUtils.trimToEmpty(tElement.getTitle())).append(System.lineSeparator());
            sb.append(StringUtils.trimToEmpty(tElement.getExample()));
            return sb.toString();
        }
        return StringUtils.EMPTY;
    }
}
