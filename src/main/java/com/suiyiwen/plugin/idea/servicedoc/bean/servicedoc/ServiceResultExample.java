package com.suiyiwen.plugin.idea.servicedoc.bean.servicedoc;

import com.suiyiwen.plugin.idea.servicedoc.bean.ServiceDocTag;
import lombok.Data;

/**
 * @author dongxuanliang252
 * @date 2018-12-27 19:05
 */
@Data
public class ServiceResultExample implements ServiceDocElement {

    private String title;
    private String example;

    @Override
    public ServiceDocTag getTag() {
        return ServiceDocTag.serviceParamExample;
    }

}
