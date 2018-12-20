package com.suiyiwen.plugin.idea.servicedoc.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2018-12-18 15:13
 */
public class FieldBean implements Serializable {

    private String name;
    private Integer type;
    private String description;
    private List<FieldBean> childFieldList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<FieldBean> getChildFieldList() {
        return childFieldList;
    }

    public void setChildFieldList(List<FieldBean> childFieldList) {
        this.childFieldList = childFieldList;
    }
}
