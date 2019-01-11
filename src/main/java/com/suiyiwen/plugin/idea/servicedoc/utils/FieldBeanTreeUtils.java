package com.suiyiwen.plugin.idea.servicedoc.utils;

import com.suiyiwen.plugin.idea.servicedoc.bean.dialog.FieldBean;
import com.suiyiwen.plugin.idea.servicedoc.bean.servicedoc.AbstractServiceField;
import com.suiyiwen.plugin.idea.servicedoc.constant.ServiceDocConstant;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author dongxuanliang252
 * @date 2018-12-29 20:16
 */
public enum FieldBeanTreeUtils {

    INSTANCE;

    public List<FieldBean> toTreeFieldBeanList(List<AbstractServiceField> serviceFieldList) {
        if (CollectionUtils.isEmpty(serviceFieldList)) {
            return null;
        }
        List<FieldBean> fieldBeanList = new ArrayList<>();
        for (AbstractServiceField field : serviceFieldList) {
            if (StringUtils.isBlank(field.getField())) {
                continue;
            }
            putRecursiveNode(fieldBeanList, field);
        }
        return fieldBeanList;
    }

    private void putRecursiveNode(List<FieldBean> fieldBeanList, AbstractServiceField newField) {
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
                FieldBean newNode = new FieldBean();
                newNode.setName(shortName);
                newNode.setChildFieldList(new ArrayList<>());
                thisFieldBeanList.add(newNode);
                thisFieldBeanList = newNode.getChildFieldList();
            } else {
                thisFieldBeanList = existNode.getChildFieldList();
            }
        }
        //thisNode
        FieldBean existNode = findNode(thisFieldBeanList, shortNames[shortNames.length - 1]);
        if (existNode == null) {
            FieldBean thisNode = new FieldBean();
            thisNode.setName(shortNames[shortNames.length - 1]);
            thisNode.setType(newField.getType());
            thisNode.setChildFieldList(new ArrayList<>());
            thisNode.setDescription(newField.getDescription());
            thisFieldBeanList.add(thisNode);
        } else if (StringUtils.isBlank(existNode.getType())) {
            existNode.setType(newField.getType());
            existNode.setDescription(newField.getDescription());
        }
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
        if (CollectionUtils.isEmpty(fieldBeanList)) {
            return null;
        }
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
        if (StringUtils.isBlank(prefix)) {
            prefix = field.getName();
        } else {
            prefix = prefix + ServiceDocConstant.CHAR_DOT + field.getName();
        }
        AbstractServiceField serviceField = ClassUtils.INSTANCE.newInstance(cls);
        if (serviceField != null) {
            if (StringUtils.isNotBlank(title)) {
                serviceField.setGroup(title);
            }
            serviceField.setType(field.getType());
            serviceField.setField(prefix);
            serviceField.setDescription(field.getDescription());
            serviceFieldList.add(serviceField);
        }
        if (CollectionUtils.isNotEmpty(field.getChildFieldList())) {
            for (FieldBean childField : field.getChildFieldList()) {
                putRecursiveNode(serviceFieldList, childField, title, prefix, cls);
            }
        }
    }

    public List<FieldBean> merge(List<FieldBean> newFieldList, List<FieldBean> oldFieldList) {
        if (CollectionUtils.isEmpty(newFieldList)) {
            return null;
        }
        if (CollectionUtils.isEmpty(oldFieldList)) {
            return newFieldList;
        }
        Map<String, FieldBean> oldFieldMap = oldFieldList.stream().collect(Collectors.toMap(o -> o.getName(), o -> o));
        for (FieldBean newField : newFieldList) {
            String name = newField.getName();
            if (oldFieldMap.containsKey(name)) {
                FieldBean oldField = oldFieldMap.get(name);
                if (StringUtils.isBlank(newField.getDescription())) {
                    newField.setDescription(oldField.getDescription());
                }
                newField.setChildFieldList(merge(newField.getChildFieldList(), oldField.getChildFieldList()));
            }
        }
        return newFieldList;
    }

}
