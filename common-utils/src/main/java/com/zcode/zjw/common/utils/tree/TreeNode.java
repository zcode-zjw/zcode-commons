package com.zcode.zjw.common.utils.tree;

import java.util.List;
import java.util.Objects;

/**
 * @author zhangjiwei
 */
public class TreeNode<T> implements Comparable<TreeNode<T>> {
    private T id;
    private String name;
    private List<TreeNode<T>> childList;

    public TreeNode() {
    }

    public TreeNode(T id, String name) {
        this.id = id;
        this.name = name;
    }

    public TreeNode(TreeNode<T> node) {
        if (Objects.nonNull(node)) {
            this.id = node.id;
            this.name = node.name;
            this.childList = node.childList;
        }
    }


    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TreeNode<T>> getChildList() {
        return childList;
    }

    public void setChildList(List<TreeNode<T>> childList) {
        this.childList = childList;
    }

    @Override
    public int compareTo(TreeNode<T> o) {
        if (o.id instanceof Integer) {
            return Integer.compare((Integer) this.id, (Integer) o.id);
        } else if (o.id instanceof Long) {
            return Long.compare((Long) this.id, (Long) o.id);
        } else if (o.id instanceof String) {
            return ((String) this.id).compareTo((String) o.id);
        }
        return 0;
    }

    @Override
    public String toString() {
        return "TreeNode{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", childList=" + childList +
                '}';
    }
}
