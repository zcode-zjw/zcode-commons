package com.zcode.zjw.common.utils.common;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.FieldAccessor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangjiwei
 * 对象处理工具
 */
public class ObjectUtil {
    /**
     * 判断该对象是否所有属性为空
     * 返回ture表示所有属性为null，返回false表示不是所有属性都是null
     */
    public static boolean isAllFieldNull(Object object) {
        boolean flag = true;

        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            //设置属性是可以访问的(私有的也可以)
            field.setAccessible(true);
            Object value = null;
            try {
                value = field.get(object);
                // 只要有1个属性不为空,那么就不是所有的属性值都为空
                if (value != null && !"serialVersionUID".equals(field.getName())) {
                    if (!"".equals(value)) {
                        flag = false;
                        break;
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return flag;
    }

    /**
     * 通过Map对象生成对象
     *
     * @param propertyMap map
     * @return
     * @throws Exception
     */
    public static Object createObjectByMap(Map<String, Object> propertyMap) throws Exception {
        // 使用 ByteBuddy 动态创建一个 POJO 类，并为其添加属性
        DynamicType.Builder<?> builder = new ByteBuddy()
                .subclass(Object.class)
                .name("DynamicPojo");

        for (Map.Entry<String, Object> entry : propertyMap.entrySet()) {
            String fieldName = entry.getKey();
            Object fieldValue = entry.getValue();
            Class<?> fieldType = fieldValue.getClass();

            builder = builder.defineField(fieldName, fieldType, Visibility.PRIVATE)
                    .defineMethod("get" + capitalize(fieldName), fieldType, Visibility.PUBLIC)
                    .intercept(FieldAccessor.ofBeanProperty())
                    .defineMethod("set" + capitalize(fieldName), void.class, Visibility.PUBLIC)
                    .withParameter(fieldType)
                    .intercept(FieldAccessor.ofBeanProperty());
        }

        DynamicType.Unloaded<?> dynamicType = builder.make();

        Class<?> dynamicClass = dynamicType.load(ObjectUtil.class.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                .getLoaded();
        Object dynamicInstance = dynamicClass.getDeclaredConstructor().newInstance();

        for (Map.Entry<String, Object> entry : propertyMap.entrySet()) {
            String fieldName = entry.getKey();
            Object fieldValue = entry.getValue();
            String setterName = "set" + capitalize(fieldName);
            Method setterMethod = dynamicClass.getMethod(setterName, fieldValue.getClass());
            setterMethod.invoke(dynamicInstance, fieldValue);
        }

        return dynamicInstance;
    }

    private static String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public static void main(String[] args) throws Exception {
        System.out.println(createObjectByMap(new HashMap<String, Object>() {{
            put("name", "zjw");
            put("age", 18);
        }}));
    }

}
