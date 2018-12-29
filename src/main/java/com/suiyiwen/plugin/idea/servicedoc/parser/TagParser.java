package com.suiyiwen.plugin.idea.servicedoc.parser;

import com.suiyiwen.plugin.idea.servicedoc.bean.servicedoc.ServiceDocElement;

/**
 * @author dongxuanliang252
 * @date 2018-12-27 20:24
 */
public interface TagParser {

    ServiceDocElement parse(String text);
}
