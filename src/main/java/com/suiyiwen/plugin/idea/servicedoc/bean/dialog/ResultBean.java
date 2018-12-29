package com.suiyiwen.plugin.idea.servicedoc.bean.dialog;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2018-12-18 15:20
 */

@Data
public class ResultBean implements Serializable {

    private List<FieldBean> fieldList;
    private String example;

}
