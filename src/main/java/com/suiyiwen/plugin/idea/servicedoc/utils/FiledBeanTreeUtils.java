package com.suiyiwen.plugin.idea.servicedoc.utils;

import com.suiyiwen.plugin.idea.servicedoc.bean.dialog.FieldBean;
import com.suiyiwen.plugin.idea.servicedoc.bean.servicedoc.AbstractServiceField;
import com.suiyiwen.plugin.idea.servicedoc.constant.ServiceDocConstant;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2018-12-29 20:16
 */
public enum FiledBeanTreeUtils {

    INSTANCE;

    public List<FieldBean> toTreeList(List<AbstractServiceField> serviceFieldList) {
        List<FieldBean> fieldBeanList = new ArrayList<>();
        for (AbstractServiceField field : serviceFieldList) {
            if (StringUtils.isBlank(field.getField())) {
                continue;
            }
            putRecursiveNode(fieldBeanList, field);
        }
        return fieldBeanList;
    }

    private FieldBean putRecursiveNode(List<FieldBean> fieldBeanList, AbstractServiceField newField) {
        if (newField == null || StringUtils.isBlank(newField.getField())) {
            throw new IllegalArgumentException("text can not be blank");
        }
        if (fieldBeanList == null) {
            fieldBeanList = new ArrayList<>();
        }
        String[] shortNames = StringUtils.split(newField.getField(), ServiceDocConstant.CHAR_DOT);
        List<FieldBean> thisFieldBeanList = fieldBeanList;
        //初始化parentTree
        for (int i = 0; i < shortNames.length - 1; i++) {
            String shortName = shortNames[i];
            FieldBean existNode = findNode(thisFieldBeanList, shortName);
            if (existNode == null) {
                FieldBean fieldBean = new FieldBean();
                fieldBean.setName(shortName);
                fieldBean.setChildFieldList(new ArrayList<>());
                thisFieldBeanList.add(fieldBean);
                thisFieldBeanList = fieldBean.getChildFieldList();
            } else {
                thisFieldBeanList = existNode.getChildFieldList();
            }
        }
        //thisNode
        FieldBean existNode = findNode(thisFieldBeanList, shortNames[shortNames.length - 1]);
        FieldBean thisNode = new FieldBean();
        thisNode.setName(newField.getField());
        thisNode.setType(newField.getType());
        thisNode.setDescription(newField.getDescription());
        if (existNode == null) {
            thisFieldBeanList.add(thisNode);
        } else if (StringUtils.isBlank(existNode.getType())) {
            existNode = thisNode;
        }
        return thisNode;
    }

    private FieldBean findNode(List<FieldBean> fieldBeanList, String shortName) {
        if (CollectionUtils.isEmpty(fieldBeanList) || StringUtils.isBlank(shortName)) {
            return null;
        }
        for (FieldBean fieldBean : fieldBeanList) {
            if (shortName.equals(fieldBean.getName())) {
                return fieldBean;
            }
        }
        return null;
    }

    public <T extends AbstractServiceField> List<AbstractServiceField> toServiceFieldTagList(String title, List<FieldBean> fieldBeanList, Class<T> cls) {
        List<AbstractServiceField> serviceFieldList = new ArrayList<>();
        for (FieldBean field : fieldBeanList) {
            if (StringUtils.isBlank(field.getName())) {
                continue;
            }
            putRecursiveNode(serviceFieldList, field, title, null, cls);
        }
        return serviceFieldList;

    }

    private <T extends AbstractServiceField> void putRecursiveNode(List<AbstractServiceField> serviceFieldList, FieldBean field, String title, String prefix, Class<T> cls) {
        if (serviceFieldList == null) {
            serviceFieldList = new ArrayList<>();
        }
        if (field == null) {
            return;
        }
        try {
            AbstractServiceField serviceField = cls.newInstance();
            if (StringUtils.isNotBlank(title)) {
                serviceField.setGroup(title);
            }
            serviceField.setType(field.getType());
            serviceField.setField(field.getName());
            serviceField.setDescription(field.getDescription());
            serviceFieldList.add(serviceField);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        prefix = StringUtils.isBlank(prefix) ? field.getName() : prefix + ServiceDocConstant.CHAR_DOT + field.getName();
        while (CollectionUtils.isNotEmpty(field.getChildFieldList())) {
            for (FieldBean childField : field.getChildFieldList()) {
                putRecursiveNode(serviceFieldList, childField, title, prefix, cls);
            }
        }
    }
}
