package com.zcode.zjw.common.file.xml.util;

import com.zcode.zjw.common.file.xml.common.DataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * @author zhangjiwei
 * @since 2022年03月16日
 */
public class ReflectUtil {
  private static final Logger log = LoggerFactory.getLogger(ReflectUtil.class);

  public static Method getMethod(Class clazz, String methodName) {
    Method[] methods = clazz.getMethods();
    Optional<Method> optional = Stream.of(methods)
        .filter(method -> methodName.equals(method.getName()) && method.getParameterCount() == 0)
        .findAny();
    if (optional.isPresent()) {
      return optional.get();
    }
    return null;
  }

  /**
   * 根据方法名获取未知对象的无参方法，子类没有会一直向上遍历父类（包括没有继承的父类方法）
   * 
   * @param object 反射的对象
   * @param methodName 方法名
   * @return
   */
  public static Method getDeclareMethod(Object object, String methodName) {
    Method method = null;
    if (object == null) {
      return method;
    }
    Class clazz = object.getClass();
    while (clazz != Object.class) {
      try {
        method = clazz.getDeclaredMethod(methodName);
        break;
      } catch (NoSuchMethodException e) {
        clazz = clazz.getSuperclass();
      }
    }
    return method;
  }

  /**
   * 根据方法名和参数获取未知对象的无参方法，子类没有会一直向上遍历父类
   * 
   * @param object 反射的对象
   * @param methodName 方法名
   * @param paramTypes 参数
   * @return
   */
  public static Method getDeclareMethod(Object object, String methodName, Class[] paramTypes) {
    Method method = null;
    if (object == null) {
      return method;
    }
    Class clazz = object.getClass();
    while (clazz != Object.class) {
      try {
        method = clazz.getDeclaredMethod(methodName, paramTypes);
        break;
      } catch (NoSuchMethodException e) {
        clazz = clazz.getSuperclass();
      }
    }
    return method;
  }

  /**
   * 根据属性名获取未知对象的属性，子类没有会一直向上遍历父类
   * 
   * @param object 反射的对象
   * @param fieldName 属性名
   * @return
   */
  public static Field getAllDeclareField(Object object, String fieldName) {
    Field field = null;
    if (object == null) {
      return field;
    }
    Class clazz = object.getClass();
    while (clazz != Object.class) {
      try {
        field = clazz.getDeclaredField(fieldName);
        break;
      } catch (NoSuchFieldException e) {
        clazz = clazz.getSuperclass();
      }
    }
    return field;
  }

  public static Field getAllDeclareField(Class clazz, String fieldName) {
    Field field = null;
    if (clazz != null) {
      try {
        field = clazz.getDeclaredField(fieldName);
      } catch (NoSuchFieldException e) {
        clazz = clazz.getSuperclass();
        field = getAllDeclareField(clazz, fieldName);
      }
    }
    return field;
  }

  public static Field getDeclareField(Object object, String fieldName) {
    if (object == null) {
      return null;
    }
    Class clazz = object.getClass();
    return getDeclareField(clazz, fieldName);
  }

  public static Field getDeclareField(Class clazz, String fieldName) {
    try {
      return clazz.getDeclaredField(fieldName);
    } catch (NoSuchFieldException e) {
      return null;
    }
  }

  /**
   * 获取类的所有属性，包括继承父类的属性
   * 
   * @param object
   * @return
   */
  public static Field[] getFields(Object object) {
    if (null == object) {
      return new Field[0];
    }
    Class clazz = object.getClass();
    Field[] fields = clazz.getDeclaredFields();
    List<Field> fieldList = new ArrayList<>();
    fieldList.addAll(Arrays.asList(fields));
    clazz = clazz.getSuperclass();
    while (clazz != Object.class) {
      fields = clazz.getDeclaredFields();
      List<Field> list = Stream.of(fields).filter(field -> {
        String s = field.toGenericString().split(" ")[0];
        return "public".equals(s) || "protected".equals(s);
      }).collect(Collectors.toList());
      fieldList.addAll(list);
      clazz = clazz.getSuperclass();
    }
    return fieldList.toArray(new Field[fieldList.size()]);
  }

  /**
   * 获取类的所有属性，包括父类的所有属性
   * 
   * @param clazz
   * @return
   */
  public static Field[] getAllFields(Class clazz) {
    List<Field> fieldList = new ArrayList<>();
    recurField(fieldList, clazz);
    return CollectionUtil.toArray(fieldList);
  }

  private static void recurField(List<Field> fieldList, Class clazz) {
    if (clazz != null) {
      Field[] fields = clazz.getDeclaredFields();
      fieldList.addAll(Arrays.asList(fields));
      recurField(fieldList, clazz.getSuperclass());
    }
  }

  /**
   * copy 两个对象相同的指定属性的值
   * 
   * @param dest 目标对象
   * @param orig 源对象
   * @param field 属性
   */
  public static void copyProperty(Object dest, Object orig, Field field) {
    String fieldName = field.getName();
    String setMethodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    String getMethodName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    if (field.getType() == Boolean.class || field.getType() == boolean.class) {
      getMethodName = "is" + getMethodName;
    } else {
      getMethodName = "get" + getMethodName;
    }
    try {
      Method getMethod = orig.getClass().getDeclaredMethod(getMethodName);
      Method setMethod = dest.getClass().getDeclaredMethod(setMethodName, field.getType());
      setMethod.invoke(dest, getMethod.invoke(orig));
    } catch (Exception e) {
      log.info("copy对象属性出错---{}", e);
    }
  }

  /**
   * copy 两个对象相同的属性的值
   * 
   * @param dest 目标对象
   * @param orig 源对象
   */
  public static void copyProperties(Object dest, Object orig) {
    Field[] origFields = orig.getClass().getDeclaredFields();
    Field[] destFields = dest.getClass().getDeclaredFields();
    Stream.of(origFields).filter(field -> {
      List<String> list = Stream.of(destFields).map(Field::getName).collect(Collectors.toList());
      return list.contains(field.getName());
    }).forEach(field -> copyProperty(dest, orig, field));
  }

  public static void setField(Object target, Field field, Object value) throws Exception {
    field.setAccessible(true);
    Type type = field.getGenericType();
    if (DataType.DATATYPE_STRING.equals(type.getTypeName())) {
      field.set(target, DataType.getAsString(value));
    } else if (DataType.DATATYPE_SHORT.equals(type.getTypeName())
        || DataType.DATATYPE_short.equals(type.getTypeName())) {
      field.set(target, DataType.getAsShort(value));
    } else if (DataType.DATATYPE_INTEGER.equals(type.getTypeName())
        || DataType.DATATYPE_int.equals(type.getTypeName())) {
      field.set(target, DataType.getAsInt(value));
    } else if (DataType.DATATYPE_LONG.equals(type.getTypeName())
        || DataType.DATATYPE_long.equals(type.getTypeName())) {
      field.set(target, DataType.getAsLong(value));
    } else if (DataType.DATATYPE_DOUBLE.equals(type.getTypeName())
        || DataType.DATATYPE_double.equals(type.getTypeName())) {
      field.set(target, DataType.getAsDouble(value));
    } else if (DataType.DATATYPE_FLOAT.equals(type.getTypeName())
        || DataType.DATATYPE_float.equals(type.getTypeName())) {
      field.set(target, DataType.getAsFloat(value));
    } else if (DataType.DATATYPE_BYTE.equals(type.getTypeName())
        || DataType.DATATYPE_byte.equals(type.getTypeName())) {
      field.set(target, DataType.getAsByte(value));
    } else if (DataType.DATATYPE_CHAR.equals(type.getTypeName())
        || DataType.DATATYPE_char.equals(type.getTypeName())) {
      field.set(target, DataType.getAsChar(value));
    } else if (DataType.DATATYPE_BOOLEAN.equals(type.getTypeName())
        || DataType.DATATYPE_boolean.equals(type.getTypeName())) {
      field.set(target, DataType.getAsBoolean(value));
    } else if (DataType.DATATYPE_DATE.equals(type.getTypeName())) {
      field.set(target, DataType.getAsDate(value));
    } else if (DataType.DATATYPE_TIME.equals(type.getTypeName())) {
      field.set(target, DataType.getAsTime(value));
    } else if (DataType.DATATYPE_TIMESTAMP.equals(type.getTypeName())) {
      field.set(target, DataType.getAsTimestamp(value));
    } else {
      field.set(target, value);
    }
  }

  /**
   * 修改注解的属性值
   * 
   * @param annotation
   * @param name
   * @param value
   * @throws NoSuchFieldException
   * @throws IllegalAccessException
   */
  public static void changeAnnotationValue(Annotation annotation, String name, Object value)
      throws NoSuchFieldException, IllegalAccessException {
    InvocationHandler handler = Proxy.getInvocationHandler(annotation);
    Field f = handler.getClass().getDeclaredField("memberValues");
    f.setAccessible(true);
    Map<String, Object> memberValues = (Map<String, Object>) f.get(handler);
    memberValues.put(name, value);
  }

  /**
   * 反射类的注解，本类没有会一直查找父类
   * 
   * @param clazz
   * @param annotationClazz
   * @return
   */
  public static Annotation getAnnotation(Class clazz, Class annotationClazz) {
    Annotation annotation = clazz.getAnnotation(annotationClazz);
    if (annotation == null) {
      Class supClazz = clazz.getSuperclass();
      if (supClazz != null) {
        annotation = getAnnotation(supClazz, annotationClazz);
        return annotation;
      } else {
        log.warn("类{}以及其父类{}都没有{}注解", clazz, supClazz, annotationClazz);
      }
    }
    return annotation;
  }

}
