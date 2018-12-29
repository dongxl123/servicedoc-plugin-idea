package com.suiyiwen.plugin.idea.servicedoc.bean.servicedoc;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2018-12-27 19:24
 */
@Data
public class ServiceResultGroupElement implements Serializable {

    private List<ServiceResult> serviceResult;
    private ServiceResultExample serviceResultExample;

}
