package com.zcode.zjw.common.file.xml.util;

import com.zcode.zjw.common.file.xml.annotation.Level;
import com.zcode.zjw.common.file.xml.annotation.Xml;
import com.zcode.zjw.common.file.xml.common.DataType;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author: zjw
 * @date: 2022/12/10
 **/
@Slf4j
public class Dom4Util {


    public static XmlAttribleSub createRoot(String name) {
        return new XmlAttribleSub(0, name);
    }

    /**
     * 递归遍历xml节点
     *
     * @param element
     * @throws Exception
     */
    private void traverseXmlNode(Element element, Map<String, String> nameValueMap) throws Exception {
        // 遍历节点属性
        for (Iterator iter = element.attributeIterator(); iter.hasNext(); ) {
            Attribute attr = (Attribute) iter.next();
            System.out
                    .println("[" + element.getName() + "]节点的属性---" + attr.getName() + ":" + attr.getValue());
            nameValueMap.put(attr.getName(), attr.getValue());
        }
        // 遍历子节点
        for (Iterator i = element.elementIterator(); i.hasNext(); ) {
            Element e = (Element) i.next();
            System.out.println("[" + element.getName() + "]的子节点" + e.getName() + ": " + e.getText());
            nameValueMap.put(e.getName(), e.getText());
            traverseXmlNode(e, nameValueMap);
        }
    }

    /**
     * 根据xPathId获取节点的值
     *
     * @param xml
     * @param xPathId
     * @return String[]
     * @throws Exception
     */
    public static String[] getNodeValue(String xml, String xPathId) throws Exception {
        List<XmlAttribleSub> xmlAttribleSubs = xPathAttribleSub(xml, xPathId);
        if (xmlAttribleSubs.isEmpty()) {
            return null;
        }
        String[] values = new String[xmlAttribleSubs.size()];
        for (int i = 0; i < xmlAttribleSubs.size(); i++) {
            XmlAttribleSub sub = xmlAttribleSubs.get(i);
            if (sub.isHasSubNode()) {
                throw new Exception(xPathId + "节点还有子节点，无法取值");
            }
            values[i] = sub.getValue();
        }
        return values;
    }

    /**
     * 获取xml最底层节点的值,
     *
     * @param xml   xml报文
     * @param name  节点名称
     * @param level 节点层级
     * @param num   当前层级对应的第几个值
     * @return
     * @throws Exception
     */
    public static String getNodeValue(String xml, String name, int level, int num) throws Exception {
        XmlAttribleSub xmlAttribleSub = getTargetSub(xml, level - 1, num);
        if (xmlAttribleSub == null) {
            log.debug("没有找到对应节点level-{}，num-{}", level, num);
            return null;
        }
        List<XmlAttribleSub> subs = xmlAttribleSub.getSubNodes();
        for (XmlAttribleSub sub : subs) {
            if (sub.getName().equalsIgnoreCase(name)) {
                if (sub.isHasSubNode()) {
                    throw new Exception(name + "不是XML最底层节点");
                }
                return sub.getValue();
            }
        }
        log.debug("未获取到层级为{}的第{}个{}节点的值，请检查XML报文是否正确", level, num, name);
        return null;
    }

    /**
     * 获取指定节点的属性值
     *
     * @param xml
     * @param name  属性名
     * @param level 层级
     * @param num   当前层级对应的第几个值
     * @return
     * @throws Exception
     */
    public static String getNodeAttrValue(String xml, String name, int level, int num)
            throws Exception {
        XmlAttribleSub xmlAttribleSub = getTargetSub(xml, level, num);
        if (xmlAttribleSub != null) {
            if (xmlAttribleSub.isHasAttr()) {
                return xmlAttribleSub.getAttrs().get(name);
            }
        } else {
            log.debug("没有找到对应节点level-{}，num-{}", level, num);
        }
        return null;
    }

    /**
     * 转换XML中的相应节点为bean，当前节点下不能包含多层节点
     *
     * @param xml
     * @param xPathId
     * @param clazz
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> List<T> getBeanForXmlNode(String xml, String xPathId, Class<T> clazz)
            throws Exception {
        List<XmlAttribleSub> xmlAttribleSubs = xPathAttribleSub(xml, xPathId);
        if (xmlAttribleSubs.size() == 0) {
            throw new Exception(xPathId + "节点不存在");
        }
        List<T> list = new ArrayList<>();
        Xml[] xmls = XmlAnnotationUtil.getClassXmls(clazz);
        T obj;
        Field field;
        Xml x;
        for (XmlAttribleSub xmlSub : xmlAttribleSubs) {
            obj = clazz.newInstance();
            for (XmlAttribleSub sub : xmlSub.getSubNodes()) {
                if (sub.isHasSubNode()) {
                    Class subClazz = XmlAnnotationUtil.getSubNodeBeanClazz(xmls, sub.getName());
                    if (subClazz == null) {
                        throw new Exception(xPathId + "节点下有嵌套节点，但类" + clazz.getTypeName() + "没有配置相应的clazz");
                    }
                    if (xmlSub.isHasAttr()) {
                        if (xml.length() == 0) {
                            throw new Exception("xml映射类" + clazz.getTypeName() + "没有配置属性xml");
                        }
                        // getBeanForXmlNode()
                    }
                } else {
                    field = ReflectUtil.getDeclareField(clazz, sub.getName());
                    if (field != null) {
                        ReflectUtil.setField(obj, field, sub.getValue());
                    } else {
                        if (xmls.length > 0) {
                            throw new Exception("xml解析异常--" + clazz.getName() + "类中没有对应属性" + sub.getName());
                        }
                        x = XmlAnnotationUtil.getXml(xmls, sub.getName());
                        if (x == null) {
                            throw new Exception(
                                    "xml解析异常--" + clazz.getName() + "类中没有对应属性" + sub.getName() + "也没有对应的注解配置");
                        }
                        field = clazz.getDeclaredField(x.attrName());
                        ReflectUtil.setField(obj, field, sub.getValue());
                    }
                }
            }
            list.add(obj);
        }
        return list;
    }

    /**
     * 转换XML中的相应节点属性为bean
     *
     * @param xml
     * @param xPathId
     * @param clazz
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> List<T> getBeanForXxmlAttr(String xml, String xPathId, Class<T> clazz)
            throws Exception {
        List<XmlAttribleSub> xmlAttribleSubs = xPathAttribleSub(xml, xPathId);
        if (xmlAttribleSubs.size() == 0) {
            throw new Exception(xPathId + "节点不存在");
        }
        List<T> list = new ArrayList<>();
        for (XmlAttribleSub xmlSub : xmlAttribleSubs) {
            if (xmlSub.isHasAttr()) {
                list.add(CommonUtils.mapToBean(xmlSub.getAttrs(), clazz));
            }
        }
        return list;
    }

    private static String convertName(String name) {
        String[] ss = name.split("_");
        StringBuilder sb = new StringBuilder("set");
        for (int i = 0; i < ss.length; i++) {
            sb.append(String.valueOf(ss[i].charAt(0)).toUpperCase()).append(ss[i].substring(1));
        }
        return sb.toString();
    }

    /**
     * 从根节点开始，将xml转换成XmlAttribleSub
     *
     * @param xml
     * @return
     * @throws Exception
     */
    public static XmlAttribleSub xPathAttribleSub(String xml) throws Exception {
        Document document = DocumentHelper.parseText(xml);
        Element root = document.getRootElement();
        XmlAttribleSub sub = new XmlAttribleSub(0, root.getName());
        recParse(root, sub, 0);
        return sub;
    }

    /**
     * 根据指定节点开始，将xml转换成XmlAttribleSub列表
     *
     * @param xml
     * @param xPathId 报文节点，例如//Orders/Order/goods/name
     * @return
     */
    public static List<XmlAttribleSub> xPathAttribleSub(String xml, String xPathId) throws Exception {
        List<XmlAttribleSub> subs = new ArrayList<>();
        Document document = DocumentHelper.parseText(xml);
        Element root = document.getRootElement();
        // 根据指定节点获取节点
        List<Element> elements = root.selectNodes(xPathId);
        // 判断当前节点的层级，从根节点0开始
        int level = xPathId.substring(1).split("/").length;
        for (Element element : elements) {
            XmlAttribleSub sub = new XmlAttribleSub(level - 2, element.getName());
            recParse(element, sub, level - 2);
            subs.add(sub);
        }
        return subs;
    }

    public static XmlAttribleSub beanToXmlSub(Object obj, int level) {
        Class clazz = obj.getClass();
        XmlAttribleSub root = new XmlAttribleSub(level, clazz.getSimpleName());
        Field[] fields = clazz.getDeclaredFields();
        XmlAttribleSub sub;
        for (Field field : fields) {
            field.setAccessible(true);
            sub = new XmlAttribleSub(level + 1, field.getName());
            try {
                sub.setValue(DataType.getAsStringNotNull(field.get(obj)));
                root.addSubNode(sub);
            } catch (IllegalAccessException e) {
                root = null;
                log.error("bean转换成XmlAttribleSub异常", e);
            }
        }
        return root;
    }

    public static XmlAttribleSub beanToXmlSub(Object obj) throws Exception {
        Class clazz = obj.getClass();
        Level level = (Level) ReflectUtil.getAnnotation(clazz, Level.class);
        int nodeLevel = 0;
        Xml[] xmls = XmlAnnotationUtil.getClassXmls(clazz);
        String classXmlName = clazz.getSimpleName();
        if (level != null) {
            nodeLevel = level.value();
            classXmlName = level.xmlName().length() > 0 ? level.xmlName() : classXmlName;
        } else {
            log.warn("转xml的类{}未添加注解标志节点级别，默认为root", clazz.getName());
        }

        XmlAttribleSub root = new XmlAttribleSub(nodeLevel, classXmlName);
        Field[] fields = ReflectUtil.getAllFields(clazz);
        XmlAttribleSub sub;
        Xml xml;
        String attrName;
        Object attrValue;
        for (Field field : fields) {
            xml = XmlAnnotationUtil.getXml(xmls, field.getName());
            field.setAccessible(true);
            try {
                attrValue = field.get(obj);
                if (xml != null) {
                    if (xml.isElement()) {
                        // 节点属性是xml元素
                        if (!Object.class.getName().equals(xml.clazz().getName())) {
                            if (xml.isList()) {
                                List list = (List) attrValue;
                                // XmlAttribleSub xmlAttribleSub = root;
                                if (level.isList()) {
                                    // 当前节点是list
                                    if (CommonUtils.isNotEmpty(list)) {
                                        for (Object subNode : list) {
                                            root.addSubNode(beanToXmlSub(subNode));
                                        }
                                    }
                                } else {
                                    // 当前节点包含list节点
                                    XmlAttribleSub xmlAttribleSub = new XmlAttribleSub(nodeLevel + 1, xml.xmlName());
                                    if (CommonUtils.isNotEmpty(list)) {
                                        for (Object subNode : list) {
                                            xmlAttribleSub.addSubNode(beanToXmlSub(subNode));
                                        }
                                    }
                                    root.addSubNode(xmlAttribleSub);
                                }
                            } else {
                                // 当前节点有bean子节点
                                root.addSubNode(beanToXmlSub(attrValue));
                            }
                        } else if (xml.isAttr()) {
                            // bean属性添加了xml注解，并且标志为xml节点属性
                            attrName = "".equals(xml.xmlName()) ? field.getName() : xml.xmlName();
                            String value = DataType.isDate(attrValue)
                                    ? DateUtil.formatDate((Date) attrValue, xml.dateFormat())
                                    : DataType.getAsStringNotNull(attrValue);
                            // 设置属性
                            root.addAttr(attrName, value);
                        } else {
                            // 属性是普通节点
                            attrName = "".equals(xml.xmlName()) ? field.getName() : xml.xmlName();
                            sub = new XmlAttribleSub(nodeLevel + 1, attrName);
                            String value = DataType.isDateType(attrValue)
                                    ? DateUtil.formatDate((Date) attrValue, xml.dateFormat())
                                    : DataType.getAsStringNotNull(attrValue);
                            sub.setValue(DataType.getAsStringNotNull(value));
                            root.addSubNode(sub);
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                root = null;
                log.error("bean转换成XmlAttribleSub异常", e);
            }
        }
        return root;
    }

    /**
     * XmlAttribleSub转换成bean
     *
     * @param sub
     * @param clazz
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T xmlSubToBean(XmlAttribleSub sub, Class<T> clazz) throws Exception {
        if (!sub.isHasAttr() && !sub.isHasSubNode()) {
            throw new Exception(sub + "既没有属性也没有子节点");
        }
        T t = clazz.newInstance();
        Xml[] xmls = XmlAnnotationUtil.getClassXmls(clazz);
        Field field;
        // 解析属性
        for (Map.Entry<String, String> entry : sub.getAttrs().entrySet()) {
            field = XmlAnnotationUtil.getBeanField(clazz, xmls, entry.getKey());
            Xml xml = XmlAnnotationUtil.getXml(xmls, entry.getKey());
            if (xml != null) {
                if (DataType.isDate(field.getType().getTypeName())) {
                    ReflectUtil.setField(t, field, DateUtil.parseDate(entry.getValue(), xml.dateFormat()));
                } else {
                    ReflectUtil.setField(t, field, entry.getValue());
                }
            }
        }
        // 解析子节点
        Xml xml;
        Object o;
        Level level = clazz.getAnnotation(Level.class);
        if (level != null && level.isList()) {
            // 如果当前节点就是一个list
            List list = new ArrayList();
            xml = XmlAnnotationUtil.getListXml(xmls);
            if (xml != null) {
                field = XmlAnnotationUtil.getBeanField(clazz, xmls, sub.getName());
                for (XmlAttribleSub xmlAttribleSub : sub.getSubNodes()) {
                    list.add(xmlSubToBean(xmlAttribleSub, xml.clazz()));
                }
                ReflectUtil.setField(t, field, list);
            }
        } else {
            // 遍历当前节点的子节点
            for (XmlAttribleSub xmlAttribleSub : sub.getSubNodes()) {
                xml = XmlAnnotationUtil.getXml(xmls, xmlAttribleSub.getName());
                if (xml != null) {
                    field = XmlAnnotationUtil.getBeanField(clazz, xmls, xmlAttribleSub.getName());
                    if (xml.isList()) {
                        // 当前sub的子节点是一个list的元素
                        Class subClazz = XmlAnnotationUtil.getSubNodeBeanClazz(xmls, xmlAttribleSub.getName());
                        if (subClazz == null) {
                            throw new Exception(xmlAttribleSub.getName() + "节点没有配置映射clazz");
                        }
                        List list = new ArrayList();
                        for (XmlAttribleSub s : xmlAttribleSub.getSubNodes()) {
                            // 子节点是否还有子节点或有属性
                            if (s.isHasSubNode() || s.isHasAttr()) {
                                list.add(xmlSubToBean(s, subClazz));
                            } else {
                                log.warn("list存在空节点");
                            }
                        }
                        ReflectUtil.setField(t, field, list);
                    } else if (!Object.class.getTypeName().equals(xml.clazz().getTypeName())) {
                        // 子节点是bean
                        o = xmlSubToBean(xmlAttribleSub, xml.clazz());
                        ReflectUtil.setField(t, field, o);
                    } else if (xmlAttribleSub.isUnderNode()) {
                        // 当前节点是普通节点
                        if (DataType.DATATYPE_DATE.equals(field.getType().getTypeName())) {
                            ReflectUtil.setField(t, field,
                                    DateUtil.parseDate(xmlAttribleSub.getValue(), xml.dateFormat()));
                        } else {
                            ReflectUtil.setField(t, field,
                                    DataType.transfer(xmlAttribleSub.getValue(), field.getType()));
                        }
                    }
                }
            }
        }
        return t;
    }

    /**
     * 递归解析xml
     *
     * @param root
     * @param sub
     * @param level
     */
    private static void recParse(Element root, XmlAttribleSub sub, int level) {
        level++;
        if (root.isTextOnly()) {
            // 该节点没有字节点
            sub.setValue(root.getText());
        }
        // 设置当前节点属性map
        for (Iterator it = root.attributeIterator(); it.hasNext(); ) {
            Attribute attr = (Attribute) it.next();
            sub.getAttrs().put(attr.getName(), attr.getValue());
        }
        // 递归子节点
        for (Iterator iter = root.elementIterator(); iter.hasNext(); ) {
            Element element = (Element) iter.next();
            XmlAttribleSub subNode = new XmlAttribleSub(level, element.getName());
            for (Iterator it = element.attributeIterator(); it.hasNext(); ) {
                Attribute attr = (Attribute) it.next();
                subNode.getAttrs().put(attr.getName(), attr.getValue());
            }
            if (element.isTextOnly()) {
                subNode.setValue(element.getText());
            } else {
                recParse(element, subNode, level);
            }
            sub.addSubNode(subNode);
        }
    }

    private static XmlAttribleSub getTargetSub(String xml, int level, int num) throws Exception {
        XmlAttribleSub xmlAttribleSub = xPathAttribleSub(xml);
        List<XmlAttribleSub> subs;
        for (int i = 0; i < level; i++) {
            subs = xmlAttribleSub.getSubNodes();
            if (subs.size() > 1) {
                xmlAttribleSub = subs.get(num - 1);
            } else if (subs.size() == 1) {
                xmlAttribleSub = subs.get(0);
            }
            if (xmlAttribleSub.getId() == level) {
                break;
            }
            if (subs.isEmpty()) {
                log.debug("没有找到层级为{}的第{}个节点", level, num);
                xmlAttribleSub = null;
            }
        }
        return xmlAttribleSub;
    }

    /**
     * 设置bean的属性
     *
     * @param xmlAttribleSub
     * @param clazz
     * @param xmls
     * @param t
     * @throws Exception
     */
    private static void setSubNodeAttr(XmlAttribleSub xmlAttribleSub, Class clazz, Xml[] xmls,
                                       Object t) throws Exception {
        Field field;
        // 子节点是否还有子节点或有属性
        if (xmlAttribleSub.isHasSubNode() || xmlAttribleSub.isHasAttr()) {
            Class subClazz = XmlAnnotationUtil.getSubNodeBeanClazz(xmls, xmlAttribleSub.getName());
            if (subClazz == null) {
                throw new Exception(xmlAttribleSub.getName() + "节点没有配置映射clazz");
            }
            field = XmlAnnotationUtil.getBeanField(clazz, xmls, xmlAttribleSub.getName());
            ReflectUtil.setField(t, field, xmlSubToBean(xmlAttribleSub, subClazz));
        } else {
            field = XmlAnnotationUtil.getBeanField(clazz, xmls, xmlAttribleSub.getName());
            ReflectUtil.setField(t, field, xmlAttribleSub.getValue());
        }
    }

    public static <T> T xmlToBean(String xml, Class<T> clazz) throws Exception {
        return xmlSubToBean(xPathAttribleSub(xml), clazz);
    }
}
