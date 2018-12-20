package com.suiyiwen.plugin.idea.servicedoc.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2018-12-18 13:40
 */
public class MetadataBean implements Serializable {

    private String function;
    private String groupName;
    private String name;
    private String version;
    private String author;
    private String description;
    private List<ParamBean> paramList;
    private ResultBean result;

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ParamBean> getParamList() {
        return paramList;
    }

    public void setParamList(List<ParamBean> paramList) {
        this.paramList = paramList;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }
}
