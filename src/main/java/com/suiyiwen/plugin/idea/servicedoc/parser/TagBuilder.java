package com.suiyiwen.plugin.idea.servicedoc.parser;

import com.suiyiwen.plugin.idea.servicedoc.bean.servicedoc.ServiceDocElement;

/**
 * @author dongxuanliang252
 * @date 2018-12-28 19:10
 */
public interface TagBuilder {

    String build(ServiceDocElement element);
}
