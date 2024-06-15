package com.zcode.zjw.common.file.xml.util;

import com.zcode.zjw.common.file.xml.annotation.Level;
import com.zcode.zjw.common.file.xml.annotation.Xml;
import com.zcode.zjw.common.utils.common.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: zjw
 * @date: 2022/1/3
 **/
@Slf4j
public class XmlAnnotationUtil {

  /**
   * 从xml数组中获取指定attrName或xmlName的xml注解对象
   * 
   * @param xmls
   * @param attrName
   * @return
   */
  public static Xml getXml(Xml[] xmls, String attrName) {
    if (xmls != null && xmls.length > 0) {
      for (Xml xml : xmls) {
        // 如果存在属性名为attrName或者xmlName为attrName
        if (StringUtil.equals(xml.attrName(), attrName)
                || StringUtil.equals(xml.xmlName(), attrName)) {
          return xml;
        }
      }
    }
    log.warn("{}节点没有配置Xml注解或者xmlName配置错误, xml映射采用属性名", attrName);
    return null;
  }

  /**
   * 获得level的isList为true的list的配置xml
   * 
   * @param xmls
   * @return
   */
  public static Xml getListXml(Xml[] xmls) {
    if (xmls != null && xmls.length > 0) {
      for (int i = 0; i < xmls.length; i++) {
        if (xmls[i].isList()) {
          return xmls[i];
        }
      }
    }
    return null;
  }

  /**
   * 获取xml映射bean的所有xml注解
   * 
   * @param clazz
   * @return
   */
  public static Xml[] getClassXmls(Class clazz) throws Exception {
    List<Xml> xmlList = new ArrayList<>();
    Level level = (Level) ReflectUtil.getAnnotation(clazz, Level.class);
    Xml[] xs = null;
    if (levelHasXml(level)) {
      xs = level.xmls();
    }
    Field[] fields = ReflectUtil.getAllFields(clazz);
    if (CollectionUtil.isNotBlank(fields)) {
      Field f;
      for (Field field : fields) {
        Xml xml = field.getAnnotation(Xml.class);
        if (xml != null) {
          // 设置xml的attrName和xmlName
          InvocationHandler handler = Proxy.getInvocationHandler(xml);
          f = handler.getClass().getDeclaredField("memberValues");
          f.setAccessible(true);
          Map<String, Object> memberValues = (Map<String, Object>) f.get(handler);
          memberValues.put("attrName", field.getName());
          if (xml.xmlName().isEmpty()) {
            memberValues.put("xmlName", field.getName());
          }
          // 以属性上的注解为主，level注解中配置的xml会被覆盖掉
          xmlList.add(xml);
          if (xs != null) {
            for (int i = 0; i < xs.length; i++) {
              if (field.getName().equals(xs[i].attrName())) {
                xs[i] = null;
              }
            }
          }
        }
      }
    }
    if (CollectionUtil.isNotBlank(xs)) {
      for (Xml xml : xs) {
        if (xml != null) {
          xmlList.add(xml);
        }
      }
    }
    return CollectionUtil.toArray(xmlList);
  }

  public static boolean hasListAttr(Xml[] xmls) {
    if (CollectionUtil.isBlank(xmls)) {
      return false;
    }
    for (Xml xml : xmls) {
      if (xml.isList()) {
        return true;
      }
    }
    return false;
  }

  /**
   * 判断xml映射bean是否含有list
   * 
   * @param clazz
   * @return
   */
  public static boolean hasListAttr(Class clazz) {
    Level level = (Level) ReflectUtil.getAnnotation(clazz, Level.class);
    if (levelHasXml(level)) {
      for (Xml xml : level.xmls()) {
        if (xml.isList()) {
          return true;
        }
      }
    } else {
      Field[] fields = clazz.getDeclaredFields();
      for (Field field : fields) {
        Xml xml = field.getAnnotation(Xml.class);
        if (xml != null && xml.isList()) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * 获取xml映射bean的属性是否配置了子节点bean配置,xml配置了isList属性必须配置clazz
   * 
   * @param clazz
   * @return
   */
  public static Class getSubNodeBeanClazz(Class clazz, String xmlName) {
    Level level = (Level) ReflectUtil.getAnnotation(clazz, Level.class);
    if (levelHasXml(level)) {
      for (Xml xml : level.xmls()) {
        if (xml.xmlName().equals(xmlName) || xml.attrName().equals(xmlName)) {
          if (!Object.class.getName().equals(xml.clazz().getName())) {
            return xml.clazz();
          }
        }
      }
    } else {
      Field[] fields = clazz.getDeclaredFields();
      for (Field field : fields) {
        Xml xml = field.getAnnotation(Xml.class);
        if (field.getName().equals(xmlName)
            && !Object.class.getName().equals(xml.clazz().getName())) {
          return xml.clazz();
        }
      }
    }
    return null;
  }

  public static Class getSubNodeBeanClazz(Xml[] xmls, String xmlName) {
    for (Xml xml : xmls) {
      if (xml.xmlName().equals(xmlName) || xml.attrName().equals(xmlName)) {
        if (!Object.class.getName().equals(xml.clazz().getName())) {
          return xml.clazz();
        }
      }
    }
    return null;
  }

  /**
   * 判断level注解中是否添加了xml注解
   * 
   * @param level
   * @return
   */
  public static boolean levelHasXml(Level level) {
    if (level == null) {
      return false;
    }
    if (level.xmls().length == 1 && !level.xmls()[0].isElement()) {
      return false;
    }
    return true;
  }

  /**
   * 根据xmlName获取clazz的field
   * 
   * @param clazz
   * @param xmls
   * @param xmlName
   * @return
   * @throws Exception
   */
  public static Field getBeanField(Class clazz, Xml[] xmls, String xmlName) throws Exception {
    Field field = ReflectUtil.getAllDeclareField(clazz, xmlName);
    if (field != null) {
      return field;
    }
    Xml xml = getXml(xmls, xmlName);
    if (xml == null) {
      throw new Exception("xml属性" + xmlName + "在类" + clazz.getTypeName() + "中既没有相应属性，也没有配置xml注解");
    }
    field = ReflectUtil.getAllDeclareField(clazz, xml.attrName());
    if (field == null) {
      throw new Exception("xml属性" + xmlName + "在类" + clazz.getTypeName() + "中的xmlName配置错误");
    }
    return field;
  }

  /**
   * XmlAttribleSub的sunNodes是否对应list
   * 
   * @param sub
   * @param xmls
   * @return
   */
  public static boolean subXmlHasList(XmlAttribleSub sub, Xml[] xmls) {
    if (sub.isHasSubNode()) {
      Xml xml = getXml(xmls, sub.getSubNodes().get(0).getName());
      if (xml != null) {
        return xml.isList();
      }
    }
    return false;
  }

  /**
   * 当前节点的子节点是否为bean
   * 
   * @param sub
   * @param xmls
   * @return
   */
  public static boolean subXmlIsBean(XmlAttribleSub sub, Xml[] xmls) {
    if (sub.getSubNodes().size() == 1) {
      Xml xml = getXml(xmls, sub.getSubNodes().get(0).getName());
      if (xml != null) {
        return !xml.clazz().getTypeName().equals(Object.class);
      }
    }
    return false;
  }

  /**
   * 根据body的实体类获取不同UserModifyReq
   *
   * @param reClazz root节点的映射bean的class
   * @param bodyClazz body节点中额外的节点映射bean的class
   * @param <T>
   * @return
   * @throws NoSuchFieldException
   * @throws IllegalAccessException
   */
  public static <T> Class<T> getXmlClass(Class<T> reClazz, Class bodyClazz)
      throws NoSuchFieldException, IllegalAccessException {
    Level level = reClazz.getAnnotation(Level.class);
    Level bodyLevel = (Level) ReflectUtil.getAnnotation(bodyClazz, Level.class);
    Xml[] xmls = level.xmls();
    for (Xml xml : xmls) {
      if (xml.xmlName().equals(bodyLevel.xmlName())) {
        ReflectUtil.changeAnnotationValue(xml, "clazz", bodyClazz);
      }
    }
    return reClazz;
  }
}
