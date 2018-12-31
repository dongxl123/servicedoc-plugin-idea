package com.suiyiwen.plugin.idea.servicedoc.parser;

import com.suiyiwen.plugin.idea.servicedoc.bean.servicedoc.AbstractServiceField;
import com.suiyiwen.plugin.idea.servicedoc.bean.servicedoc.ServiceDocElement;
import com.suiyiwen.plugin.idea.servicedoc.constant.ServiceDocConstant;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2018-12-27 19:05
 */
public class CommonServiceFieldProcessor extends AbstractTagProcessor {

    @Override
    public ServiceDocElement parse(List<String> textList) {
        AbstractServiceField element = newElementInstance();
        if (element == null) {
            return null;
        }
        if (CollectionUtils.isNotEmpty(textList)) {
            int i = 0;
            for (String text : textList) {
                if (StringUtils.startsWith(text, ServiceDocConstant.TAG_TEXT_OPEN_PAREN) && StringUtils.endsWith(text, ServiceDocConstant.TAG_TEXT_CLOSE_PAREN)) {
                    element.setGroup(StringUtils.strip(text, String.format("%s%s", ServiceDocConstant.TAG_TEXT_OPEN_PAREN, ServiceDocConstant.TAG_TEXT_CLOSE_PAREN)));
                } else if (StringUtils.startsWith(text, ServiceDocConstant.TAG_TEXT_OPEN_BRACE) && StringUtils.endsWith(text, ServiceDocConstant.TAG_TEXT_CLOSE_BRACE)) {
                    String typeName = StringUtils.strip(text, String.format("%s%s", ServiceDocConstant.TAG_TEXT_OPEN_BRACE, ServiceDocConstant.TAG_TEXT_CLOSE_BRACE));
                    element.setType(typeName);
                } else {
                    i++;
                    if (i > 0) {
                        element.setField(text);
                    } else if (i > 1) {
                        element.setDescription(text);
                    }
                }
            }
        }
        return element;
    }

    @Override
    public String buildValue(ServiceDocElement element) {
        if (element instanceof AbstractServiceField) {
            AbstractServiceField tElement = (AbstractServiceField) element;
            StringBuilder sb = new StringBuilder();
            if (StringUtils.isNotBlank(tElement.getGroup())) {
                sb.append(ServiceDocConstant.TAG_TEXT_OPEN_PAREN).append(tElement.getGroup()).append(ServiceDocConstant.TAG_TEXT_CLOSE_PAREN).append(StringUtils.SPACE);
            }
            if (tElement.getType() != null) {
                sb.append(ServiceDocConstant.TAG_TEXT_OPEN_BRACE).append(tElement.getType()).append(ServiceDocConstant.TAG_TEXT_CLOSE_BRACE).append(StringUtils.SPACE);
            }
            sb.append(StringUtils.stripToEmpty(tElement.getField())).append(StringUtils.SPACE);
            sb.append(StringUtils.stripToEmpty(tElement.getDescription()));
            return sb.toString();
        }
        return StringUtils.EMPTY;
    }

}
