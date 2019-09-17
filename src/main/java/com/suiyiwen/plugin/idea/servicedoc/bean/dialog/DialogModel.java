package com.suiyiwen.plugin.idea.servicedoc.bean.dialog;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;


/**
 * @author dongxuanliang252
 * @date 2018-12-18 13:40
 */
@Getter
@Setter
public class DialogModel implements Serializable {

    private String serviceTitle;
    private String serviceFunction;
    private String groupName;
    private String name;
    private String version;
    private String author;
    private String description;
    private List<ParamBean> paramList;
    private ResultBean result;
    /**
     * 0:否 , 1:是 , 2:重新生成
     */
    private Integer generateExampleType;
}
