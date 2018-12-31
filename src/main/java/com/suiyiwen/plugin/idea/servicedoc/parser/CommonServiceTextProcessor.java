package com.suiyiwen.plugin.idea.servicedoc.parser;

import com.suiyiwen.plugin.idea.servicedoc.bean.servicedoc.AbstractServiceText;
import com.suiyiwen.plugin.idea.servicedoc.bean.servicedoc.ServiceDocElement;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2018-12-27 19:05
 */
public class CommonServiceTextProcessor extends AbstractTagProcessor {

    @Override
    public ServiceDocElement parse(List<String> textList) {
        AbstractServiceText element = newElementInstance();
        if (element == null) {
            return null;
        }
        if (CollectionUtils.isNotEmpty(textList)) {
            element.setText(textList.get(0));
        }
        return element;
    }


    @Override
    public String buildValue(ServiceDocElement element) {
        if (element instanceof AbstractServiceText) {
            AbstractServiceText tElement = (AbstractServiceText) element;
            return StringUtils.stripToEmpty(tElement.getText());
        }
        return StringUtils.EMPTY;
    }
}
