package com.suiyiwen.plugin.idea.servicedoc.bean.servicedoc;

import com.suiyiwen.plugin.idea.servicedoc.bean.ServiceDocTag;
import lombok.Data;

/**
 * @author dongxuanliang252
 * @date 2018-12-27 19:05
 */
@Data
public class ServiceParam implements ServiceDocElement {

    private String group;
    private String field;
    private Integer type;
    private String description;

    @Override
    public ServiceDocTag getTag() {
        return ServiceDocTag.serviceParam;
    }
}
