package com.suiyiwen.plugin.idea.servicedoc.bean.servicedoc;

import com.suiyiwen.plugin.idea.servicedoc.bean.ServiceDocTag;
import lombok.Data;

/**
 * @author dongxuanliang252
 * @date 2018-12-27 19:05
 */
@Data
public class Service implements ServiceDocElement {

    private String function;

    @Override
    public ServiceDocTag getTag() {
        return ServiceDocTag.service;
    }
}
