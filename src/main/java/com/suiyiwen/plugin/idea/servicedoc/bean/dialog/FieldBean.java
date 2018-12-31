package com.suiyiwen.plugin.idea.servicedoc.bean.dialog;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2018-12-18 15:13
 */
@Getter
@Setter
public class FieldBean implements Serializable {

    private String name;
    private String type;
    private String description;
    private List<FieldBean> childFieldList;
}
