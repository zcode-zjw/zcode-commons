package com.zcode.zjw.common.file.xml.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author: zjw
 * @date: 2022/12/21
 **/
public class XmlAttribleSub implements Serializable {
    private static final long serialVersionUID = -4651442473666480694L;

    /**
     * 节点所在层级
     */
    private int id;
    /**
     * 节点名称
     */
    private String name;
    /**
     * 节点值 基本节点才有值
     */
    private String value = "";
    /**
     * 属性map
     */
    private Map<String, String> attrs = new HashMap<>();
    /**
     * 子节点列表
     */
    private List<XmlAttribleSub> subNodes = new ArrayList<>();

    public XmlAttribleSub() {
    }

    public XmlAttribleSub(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getAttrs() {
        return attrs;
    }

    public void addAttr(String name, String value) {
        this.attrs.put(name, value);
    }

    public List<XmlAttribleSub> getSubNodes() {
        return subNodes;
    }

    public String getValue() {
        return value == null ? "" : value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void addSubNode(XmlAttribleSub subNode) {
        this.subNodes.add(subNode);
    }

    public boolean isRoot() {
        return this.id == 0;
    }

    public boolean isHasAttr() {
        return this.attrs.size() > 0;
    }

    public boolean isHasSubNode() {
        return this.subNodes.size() > 0;
    }

    public boolean isUnderNode() {
        return !isHasAttr() && !isHasSubNode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.id == 0) {
            sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        }
        sb.append("<").append(this.name);
        if (CollectionUtil.isNotBlank(attrs)) {
            for (Map.Entry<String, String> entry : attrs.entrySet()) {
                sb.append(" ").append(entry.getKey()).append("=").append("\"").append(entry.getValue())
                        .append("\"");
            }
        }
        sb.append(">");
        if (!subNodes.isEmpty()) {
            for (XmlAttribleSub subNode : subNodes) {
                recSub(subNode, sb);
            }
        } else {
            sb.append(this.getValue());
        }
        sb.append("</").append(this.name).append(">");
        return sb.toString();
    }

    private void recSub(XmlAttribleSub sub, StringBuilder sb) {
        sb.append("<").append(sub.getName());
        if (CollectionUtil.isNotBlank(sub.getAttrs())) {
            for (Map.Entry<String, String> entry : sub.getAttrs().entrySet()) {
                sb.append(" ").append(entry.getKey()).append("=").append("\"").append(entry.getValue())
                        .append("\"");
            }
        }
        sb.append(">");
        if (sub.getSubNodes().isEmpty()) {
            sb.append(sub.getValue());
        } else {
            for (XmlAttribleSub subNode : sub.getSubNodes()) {
                recSub(subNode, sb);
            }
        }
        sb.append("</").append(sub.getName()).append(">");
    }

}
