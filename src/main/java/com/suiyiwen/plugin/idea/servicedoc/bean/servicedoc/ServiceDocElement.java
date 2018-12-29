package com.suiyiwen.plugin.idea.servicedoc.bean.servicedoc;

import com.suiyiwen.plugin.idea.servicedoc.bean.ServiceDocTag;

import java.io.Serializable;

/**
 * @author dongxuanliang252
 * @date 2018-12-27 19:05
 */
public interface ServiceDocElement extends Serializable {

    ServiceDocTag getTag();
}
