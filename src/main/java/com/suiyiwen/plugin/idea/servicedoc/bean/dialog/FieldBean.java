package com.suiyiwen.plugin.idea.servicedoc.bean.dialog;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2018-12-18 15:13
 */
@Data
public class FieldBean implements Serializable {

    private String name;
    private Integer type;
    private String description;
    private List<FieldBean> childFieldList;
}
