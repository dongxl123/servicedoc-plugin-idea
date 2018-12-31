package com.suiyiwen.plugin.idea.servicedoc.parser;

import com.suiyiwen.plugin.idea.servicedoc.bean.servicedoc.ServiceDocElement;
import com.suiyiwen.plugin.idea.servicedoc.bean.servicedoc.ServiceDocGroupElement;

import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2018-12-31 11:44
 */
public interface TagGroupParser {

    ServiceDocGroupElement parse(List<ServiceDocElement> elements);
}
