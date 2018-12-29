package com.suiyiwen.plugin.idea.servicedoc.parser;

import com.suiyiwen.plugin.idea.servicedoc.bean.ServiceDocTag;
import com.suiyiwen.plugin.idea.servicedoc.bean.servicedoc.ServiceDocElement;
import com.suiyiwen.plugin.idea.servicedoc.constant.ServiceDocConstant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author dongxuanliang252
 * @date 2018-12-27 20:24
 */
public abstract class AbstractTagProcessor implements TagParser, TagBuilder {

    private static final Logger log = LoggerFactory.getLogger(AbstractTagProcessor.class);

    private ServiceDocTag tag;

    public void setTag(ServiceDocTag tag) {
        this.tag = tag;
    }

    @Override
    public ServiceDocElement parse(String text) {
        if (tag == null || StringUtils.isEmpty(tag.getRegExp())) {
            return null;
        }
        try {
            Pattern pattern = Pattern.compile(tag.getRegExp());
            Matcher matcher = pattern.matcher(text);
            List<String> textList = new ArrayList<>();
            while (matcher.find()) {
                for (int i = 0; i < matcher.groupCount(); i++) {
                    textList.add(StringUtils.trim(matcher.group(i + 1)));
                }
            }
            return parse(textList);
        } catch (Exception e) {
            log.error("TagParser parse error, text:{}, regExp:{}", text, tag.getRegExp(), e);
        }
        return null;
    }

    public abstract ServiceDocElement parse(List<String> textList);

    @Override
    public String build(ServiceDocElement element) {
        return String.format("%s%s%s%s", ServiceDocConstant.TAG_TEXT_PREFIX, tag.name(), StringUtils.SPACE, buildValue(element));
    }

    public abstract String buildValue(ServiceDocElement element);

}
