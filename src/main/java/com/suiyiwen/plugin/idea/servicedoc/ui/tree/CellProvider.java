package com.suiyiwen.plugin.idea.servicedoc.ui.tree;

/**
 * Created by dim on 16/11/7.
 */
public interface CellProvider {

    String getCellTitle(int index);

    void setValueAt(int column, String text);
}
