package com.suiyiwen.plugin.idea.servicedoc.bean.dialog;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2018-12-18 13:40
 */
@Data
public class DialogBean implements Serializable {

    private String function;
    private String groupName;
    private String name;
    private String version;
    private String author;
    private String description;
    private List<ParamBean> paramList;
    private ResultBean result;

}
