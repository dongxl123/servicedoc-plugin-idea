package com.suiyiwen.plugin.idea.servicedoc.bean.servicedoc;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2018-12-27 19:24
 */
@Getter
@Setter
public abstract class AbstractServiceFlowTagGroup implements ServiceDocGroupElement {

    private List<AbstractServiceField> fieldList;
    private AbstractServiceExample example;

}
