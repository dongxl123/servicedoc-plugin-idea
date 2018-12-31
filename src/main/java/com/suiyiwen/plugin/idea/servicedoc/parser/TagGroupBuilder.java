package com.suiyiwen.plugin.idea.servicedoc.parser;

import com.suiyiwen.plugin.idea.servicedoc.bean.servicedoc.ServiceDocGroupElement;

/**
 * @author dongxuanliang252
 * @date 2018-12-31 11:44
 */
public interface TagGroupBuilder {

    String build(ServiceDocGroupElement element);

}
