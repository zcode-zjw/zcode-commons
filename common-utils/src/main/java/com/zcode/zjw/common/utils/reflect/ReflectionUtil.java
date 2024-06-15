package com.zcode.zjw.common.utils.reflect;

import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.LambdaMeta;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.zcode.zjw.common.utils.common.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.property.PropertyNamer;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 反射工具类
 *
 * @author zhangjiwei
 * @date 2021/3/16 8:56
 */
@Slf4j
public class ReflectionUtil {

    @SuppressWarnings("unchecked")
    public static <T, R> R getFiledValue(T t, SFunction<T, R> action) {
        String fieldName = Optional.ofNullable(getFiledName(action)).orElse("");
        try {
            Field field = t.getClass().getField(fieldName);
            return (R) field.get(t);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 通过反射 给属性赋值
     *
     * @param t
     * @param action
     * @param value
     * @param <S>
     * @param <RR>
     */
    public static <T, S, RR> void setFiledValue(T t, SFunction<S, RR> action, Object value) {
        String fieldName = Optional.ofNullable(getFiledName(action)).orElse("");
        try {
            Field field = t.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(t, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> void setFiledValue(T t, String fieldName, Object value) {
        try {
            Field field = t.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(t, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> void setFiledValue(T t, Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(t, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 通过方法引用获取指定实体类的字段名（属性名）
     *
     * @param action
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> String getFiledName(SFunction<T, R> action) {
        return Optional.ofNullable(action).map(LambdaUtils::extract)
                .map(LambdaMeta::getImplMethodName)
                .map(PropertyNamer::methodToProperty).orElse(null);
    }

    @SafeVarargs
    public static <T> List<String> getFiledNames(SFunction<T, ? extends Serializable>... action) {
        return Arrays.stream(action).map(LambdaUtils::extract)
                .map(LambdaMeta::getImplMethodName)
                .map(PropertyNamer::methodToProperty)
                .collect(Collectors.toList());
    }


    /**
     * 通过Clazz对象创建实例
     *
     * @param clazz CLass对象
     * @param <T>   泛型
     * @return 泛型实例
     */
    public static <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获得参数为{@code doClazz}的构造器
     *
     * @param doClazz DO实体类的CLass对象实例
     * @param voClazz VO实体类的CLass对象实例
     * @param <VO>    VO实体类泛型
     * @return VO实体类的构造器
     */
    // @SafeVarargs
    public static <VO> Constructor<VO> getConstructor(Class<VO> voClazz, Class<?>... doClazz) {
        Objects.requireNonNull(doClazz);
        try {
            return voClazz.getConstructor(doClazz);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 通过构造器创建对象
     *
     * @param constructor 以泛型{@code VO}为类型的构造器实例
     * @param initargs    以泛型{@code DO}为类型的参数实例
     * @param <DO>        {@code DO}泛型
     * @param <VO>        {@code VO}泛型
     * @return 以泛型{@code VO}为类型的对象实例
     */
    @SafeVarargs
    public static <DO, VO extends DO> VO newInstance(Constructor<VO> constructor, DO... initargs) {
        try {
            return constructor.newInstance(initargs);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <p>显示化获得{@code Class<T>}对象的类型</p>
     * <p>本方法的作用时避免在显示强转时出现<i>未检查警告</i></p>
     * <p>注意{@code Class<\?>}与{@code Class<T>}是同一个类型才能强转</p>
     *
     * @param clazz Class对象实例
     * @param <T>   元素类型
     * @return 如果参数<code>clazz</code>不为<code>null</code>，则返回强转后的对象，否则返回<code>null</code>
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getClass(Class<?> clazz) {
        return (Class<T>) clazz;
    }

    /**
     * 是否Debug模式
     */
    private static boolean isDebug = false;

    /**
     * 调用Getter方法.
     */
    public static Object invokeGetterMethod(Object obj, String propertyName) {
        String getterMethodName = "get" + StringUtil.capitalize(propertyName);
        return invokeMethod(obj, getterMethodName, new Class[]{}, new Object[]{});
    }

    /**
     * 调用Setter方法.使用value的Class来查找Setter方法.
     */
    public static void invokeSetterMethod(Object obj, String propertyName, Object value) {
        invokeSetterMethod(obj, propertyName, value, null);
    }

    /**
     * 调用Setter方法.
     *
     * @param propertyType 用于查找Setter方法,为空时使用value的Class替代.
     */
    public static void invokeSetterMethod(Object obj, String propertyName, Object value, Class<?> propertyType) {
        Class<?> type = propertyType != null ? propertyType : value.getClass();
        String setterMethodName = "set" + StringUtil.capitalize(propertyName);
        invokeMethod(obj, setterMethodName, new Class[]{type}, new Object[]{value});
    }

    /**
     * 直接读取对象属性值, 无视private/protected修饰符, 不经过getter函数.
     */
    public static Object getFieldValue(final Object obj, final String fieldName) {
        Field field = getAccessibleField(obj, fieldName);
        if (field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
        }
        Object result = null;
        try {
            result = field.get(obj);
        } catch (IllegalAccessException e) {
            log.error("不可能抛出的异常{}", e);
        }
        return result;
    }

    /**
     * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
     */
    public static void setFieldValue(final Object obj, final String fieldName, final Object value) {
        Field field = getAccessibleField(obj, fieldName);
        if (field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
        }
        try {
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            log.error("不可能抛出的异常:{}", e);
        }
    }

    /**
     * 循环向上转型, 获取对象的DeclaredField, 并强制设置为可访问.
     * <p>
     * 如向上转型到Object仍无法找到, 返回null.
     */
    public static Field getAccessibleField(final Object obj, final String fieldName) {
        Assert.notNull(obj, "object不能为空");
        Assert.hasText(fieldName, "fieldName");
        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                Field field = superClass.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field;
            } catch (NoSuchFieldException e) {// NOSONAR
                // Field不在当前类定义,继续向上转型
            }
        }
        return null;
    }

    /**
     * 直接调用对象方法, 无视private/protected修饰符. 用于一次性调用的情况.
     */
    public static Object invokeMethod(final Object obj, final String methodName, final Class<?>[] parameterTypes, final Object[] args) {
        Method method = getAccessibleMethod(obj, methodName, parameterTypes);
        if (method == null) {
            throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");
        }
        try {
            return method.invoke(obj, args);
        } catch (Exception e) {
            throw convertReflectionExceptionToUnchecked(e);
        }
    }


    /**
     * 系统调度使用！！！
     * 直接调用对象方法, 无视private/protected修饰符. 用于一次性调用的情况.
     */
    public static Boolean invokeMethodByTask(final Object obj, final String methodName, final Class<?>[] parameterTypes, final Object[] args) {
        Method method = getAccessibleMethod(obj, methodName, parameterTypes);
        if (method == null) {
            return false;
        }
        try {
            method.invoke(obj, args);
            return true;
        } catch (Exception e) {
            throw convertReflectionExceptionToUnchecked(e);
        }
    }

    /**
     * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问. 如向上转型到Object仍无法找到, 返回null.
     * <p>
     * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object... args)
     */
    public static Method getAccessibleMethod(final Object obj, final String methodName, final Class<?>... parameterTypes) {
        Assert.notNull(obj, "object不能为空");
        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass
                .getSuperclass()) {
            try {
                Method method = superClass.getDeclaredMethod(methodName, parameterTypes);
                method.setAccessible(true);
                return method;
            } catch (NoSuchMethodException e) {// NOSONAR
                // Method不在当前类定义,继续向上转型
            }
        }
        return null;
    }

    /**
     * 通过反射, 获得Class定义中声明的父类的泛型参数的类型. 如无法找到, 返回Object.class. eg. public UserDao extends HibernateDao<User>
     *
     * @param clazz The class to introspect
     * @return the first generic declaration, or Object.class if cannot be determined
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getSuperClassGenericType(final Class<?> clazz) {
        return (Class<T>) getSuperClassGenericType(clazz, 0);
    }

    /**
     * 通过反射, 获得Class定义中声明的父类的泛型参数的类型. 如无法找到, 返回Object.class.
     * <p>
     * 如public UserDao extends HibernateDao<User,Long>
     *
     * @param clazz clazz The class to introspect
     * @param index the Index of the generic ddeclaration,start from 0.
     * @return the index generic declaration, or Object.class if cannot be determined
     */
    public static Class<?> getSuperClassGenericType(final Class<?> clazz, final int index) {
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            log.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
            return Object.class;
        }
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            log.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: "
                    + params.length);
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            log.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
            return Object.class;
        }
        return (Class<?>) params[index];
    }

    /**
     * 将反射时的checked exception转换为unchecked exceptions.
     */
    public static RuntimeException convertReflectionExceptionToUnchecked(Exception e) {
        if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException
                || e instanceof NoSuchMethodException) {
            return new IllegalArgumentException("Reflection jnpf.exception.", e);
        } else if (e instanceof InvocationTargetException) {
            return new RuntimeException("Reflection jnpf.exception.", ((InvocationTargetException) e).getTargetException());
        } else if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        }
        return new RuntimeException("Unexpected Checked jnpf.exception.", e);
    }

    /**
     * 调用传入对象的toString方法或反射返回对象成员变量值字符串。
     *
     * @param obj 传入对象
     * @return
     * @author Lin Chenglin 2013-4-9
     */
    public static String toString(final Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj.getClass() == Object.class || obj.getClass().isPrimitive()) {
            return obj.toString();
        }
        try {
            Method method = obj.getClass().getDeclaredMethod("toString", new Class[]{});
            if (isDebug) {
                log.debug("传入的对象实现了自己的toString方法，直接调用！");
            }
            return (String) method.invoke(obj, new Object[]{});
        } catch (NoSuchMethodException e) {
            if (isDebug) {
                log.debug("传入的对象没有实现自己的toString方法，反射获取！");
            }
            StringBuffer buf = new StringBuffer(obj.getClass().getName());
            buf.append(" [");
            // 获取所有成员变量
            Field[] fileds = obj.getClass().getDeclaredFields();
            int size = fileds.length;
            for (int i = 0; i < size; i++) {
                Field field = fileds[i];
                Object value = ReflectionUtil.getFieldValue(obj, field.getName());
                buf.append(field.getName() + "=" + ReflectionUtil.toString(value));
                if (i != size - 1) {
                    buf.append(", ");
                }
            }
            buf.append("]");
            return buf.toString();
        } catch (Exception e) {
            throw convertReflectionExceptionToUnchecked(e);
        }
    }
}
