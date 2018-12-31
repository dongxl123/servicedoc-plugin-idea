package com.suiyiwen.plugin.idea.servicedoc.bean.servicedoc;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2018-12-27 19:19
 */
@Getter
@Setter
public class ServiceDocCommentBean implements Serializable {

    private Service service;
    private ServiceVersion serviceVersion;
    private ServiceGroup serviceGroup;
    private ServiceName serviceName;
    private ServiceAuthor serviceAuthor;
    private ServiceDescription serviceDescription;
    private List<ServiceParamTagGroup> serviceParamGroupList;
    private ServiceResultTagGroup serviceResultGroup;

}
