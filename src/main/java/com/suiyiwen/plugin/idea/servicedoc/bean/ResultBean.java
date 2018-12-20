package com.suiyiwen.plugin.idea.servicedoc.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2018-12-18 15:20
 */

public class ResultBean implements Serializable {

    private List<FieldBean> fieldList;

    public List<FieldBean> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<FieldBean> fieldList) {
        this.fieldList = fieldList;
    }
}
