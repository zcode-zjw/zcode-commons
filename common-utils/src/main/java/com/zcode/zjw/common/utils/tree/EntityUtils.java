
/*
 *
 * Copyright (c) 2020-2022, Java知识图谱 (http://www.altitude.xin).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.zcode.zjw.common.utils.tree;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.zcode.zjw.common.utils.collection.ColUtils;
import com.zcode.zjw.common.utils.reflect.ReflectionUtil;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * <p>超级强大的<i>POJO</i>数据实体类转换工具</p>
 * <p>{@link EntityUtils}工具类用于基于Lambda表达式实现类型转换，具有如下优点：</p>
 * <p>1. 实现对象转对象；集合转集合；分页对象转分页对象</p>
 * <p>2. 实体类转Vo、实体类转DTO等都能应用此工具类</p>
 * <p>3. 转换参数均为不可变类型，业务更加安全</p>
 *
 * @author zhangjiwei
 * @since 2019/06/19 17:23
 **/
public class EntityUtils {
    private EntityUtils() {
    }

    /**
     * 将对象集合按照一定规则映射后收集为另一种形式的集合
     *
     * @param <R>       最终结果的泛型
     * @param <S>       原始集合元素的类泛型
     * @param <T>       转换后元素的中间状态泛型
     * @param <A>       最终结果收集器泛型
     * @param source    最原始的集合实例
     * @param action    转换规则
     * @param collector 收集器的类型
     * @return 变换后存储新元素的集合实例
     */
    public static <R, S, T, A> R collectCommon(final Collection<S> source, Function<? super S, ? extends T> action, Collector<? super T, A, R> collector) {
        Objects.requireNonNull(source);
        Objects.requireNonNull(collector);
        return source.stream().map(action).collect(collector);
    }

    /**
     * 将实体类对象从{@code T}类型转化为{@code R}类型
     *
     * @param <T>    源数据类型
     * @param <R>    变换后数据类型
     * @param obj    源对象实例
     * @param action 映射Lambda表达式 参数不能为<code>null</code> 否则抛出异常
     * @return 变换后的类型，如果source为null,则返回null
     */
    public static <T, R> R toObj(final T obj, final Function<? super T, ? extends R> action) {
        Objects.requireNonNull(action);
        return Optional.ofNullable(obj).map(action).orElse(null);
    }


    /**
     * <p>将{@code List}集合换成另一种类型</p>
     * <pre>
     *     public class User {
     *         private Long userId;
     *         private String userName;
     *         private String sex;
     *     }
     * </pre>
     * <p>通过方法引用获得任意列组成的新{@code List}集合</p>
     * <pre>
     *     List&lt;Long&gt; userIds = EntityUtils.toList(list,User::getUserId)
     * </pre>
     * <p>在{@code User}类中添加有如下构造器</p>
     * <pre>
     *     public User(User user) {
     *         if(user != null) {
     *             this.userId = user.userId;
     *             this.userName = user.userName;
     *             this.sex = user.sex;
     *         }
     *     }
     * </pre>
     * <pre>
     *     public class UserVo extends User {
     *         private String deptName;
     *
     *         public UserVo (User user) {
     *             super(user);
     *         }
     *     }
     * </pre>
     * 通过如下代码可实现DO 转 VO
     * <pre>
     *     List&lt;Long&gt; userVos = EntityUtils.toList(list,UserVo::new)
     * </pre>
     *
     * @param <T>    源数据类型
     * @param <R>    变换后数据类型
     * @param list   源List集合
     * @param action 映射Lambda表达式
     * @return 变换后的类型集合，如果source为null,则返回空集合
     */
    public static <T, R> List<R> toList(final Collection<T> list, final Function<? super T, ? extends R> action) {
        Objects.requireNonNull(action);
        if (Objects.nonNull(list)) {
            return list.stream().map(action).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    /**
     * <p>将以{@code T}类型为元素的集合转化成以以{@code R}类型为元素的集合 并允许过滤数据</p>
     *
     * @param <T>    源数据类型
     * @param <R>    变换后数据类型
     * @param list   源List集合
     * @param action 映射Lambda表达式
     * @return 变换后的类型集合，如果source为null,则返回空集合
     */
    public static <T, R> List<R> toList(final Collection<T> list, final Function<? super T, ? extends R> action, Predicate<R> pred) {
        Objects.requireNonNull(action);
        Objects.requireNonNull(pred);
        if (Objects.nonNull(list)) {
            return list.stream().map(action).filter(pred).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    /**
     * <p>使用反射将元素{@code T}类型的集合转化为元素{@code VO}类型的集合</p>
     * <p>使用本方法的限制条件是泛型{@code VO}类包含以泛型{@code T}类为参数的构造器</p>
     *
     * @param list    元素{@code T}类型的集合实例
     * @param voClazz 泛型{@code VO}实例类Class对象
     * @param <T>     DO为{@code T}实体类泛型
     * @param <VO>    VO为{@code VO}实体类泛型
     * @return 以元素{@code VO}为类型的集合实例 如果获取不到指定类型的构造器或者发生异常 则返回空集合
     */
    public static <T, VO> List<VO> toList(final Collection<T> list, Class<VO> voClazz) {
        if (list != null && list.size() > 0) {
            T obj = ColUtils.toObj(list);
            Constructor<VO> constructor = ReflectionUtil.getConstructor(voClazz, obj.getClass());
            return toList(list, e -> ReflectionUtil.newInstance(constructor, e));
        }
        return new ArrayList<>();
    }

    /**
     * 将集合{@code List<T>}实例按照参数<code>action</code>映射关系转换后 生成{@code G[]}数组
     *
     * <pre>
     *     User[] userNames = EntityUtils.toArray(userList)
     * </pre>
     *
     * @param <T>  集合元素的类型
     * @param list 集合实例 不允许为<code>null</code>
     * @return {@code G[]}数组实例
     * @since 1.6.1
     */
    public static <T> T[] toArray(final List<T> list) {
        if (ColUtils.isNotEmpty(list)) {
            Class<?> tClazz = Objects.requireNonNull(ColUtils.toObj(list)).getClass();
            ArrayCollector<T> collector = new ArrayCollector<>(ReflectionUtil.getClass(tClazz));
            return list.stream().collect(collector);
        }
        return null;
    }


    /**
     * 将集合{@code List<T>}实例按照参数<code>action</code>映射关系转换后 生成{@code G[]}数组
     *
     * <pre>
     *     String[] userNames = EntityUtils.toArray(userList, User::getUserName)
     * </pre>
     *
     * @param list   集合实例 不允许为<code>null</code>
     * @param action 映射关系 不允许为<code>null</code>
     * @param <T>    集合元素的类型
     * @param <R>    转换后数组的类型
     * @return {@code G[]}数组实例
     */
    public static <T, R> R[] toArray(final Collection<T> list, final Function<? super T, ? extends R> action) {
        Objects.requireNonNull(action);
        if (ColUtils.isNotEmpty(list)) {
            Class<?> rClazz = toObj(ColUtils.toObj(list), action).getClass();
            ArrayCollector<R> collector = new ArrayCollector<>(ReflectionUtil.getClass(rClazz));
            return list.stream().map(action).collect(collector);
        }
        return null;
    }


    /**
     * <p>将{@code T}元素类型的集合转换成另{@code G}元素类型的数组</p>
     * <p>更优雅的实现参考{@link EntityUtils#toArray(Collection, Function)}</p>
     *
     * @param <T>       原始的数据类型（泛型）
     * @param <R>       目标类型（泛型）
     * @param list      以{@code T}元素为类型的集合实例
     * @param action    转换规则（方法引用表示）
     * @param generator 以{@code G}元素为类型数组{@code Class}对象
     * @return 以{@code G}元素为类型数组实例
     * @author 明日之春 赛泰
     * @see #toArray(Collection, Function, Class)
     * @since 1.6.0
     */
    public static <T, R> R[] toArray(final Collection<T> list, final Function<? super T, ? extends R> action, IntFunction<R[]> generator) {
        Objects.requireNonNull(action);
        if (ColUtils.isNotEmpty(list)) {
            return list.stream().map(action).toArray(generator);
        }
        return null;
    }

    /**
     * <p>将{@code T}元素类型的集合转换成另{@code G}元素类型的数组</p>
     * <p>更优雅的实现参考{@link EntityUtils#toArray(Collection, Function)}</p>
     * <pre>
     *     EntityUtils.toArray(userList, User::getUserName, String[].class)
     * </pre>
     *
     * @param <T>    原始的数据类型（泛型）
     * @param <R>    目标类型（泛型）
     * @param list   以{@code T}元素为类型的集合实例
     * @param action 转换规则（方法引用表示）
     * @param clazz  以{@code G}元素为类型数组{@code Class}对象
     * @return 以{@code G}元素为类型数组实例
     * @author 明日之春 赛泰
     * @since 1.6.0
     */
    @SuppressWarnings("unchecked")
    public static <T, R> R[] toArray(final Collection<T> list, final Function<? super T, ? extends R> action, final Class<R[]> clazz) {
        Objects.requireNonNull(action);
        Objects.requireNonNull(clazz);
        Class<?> itemClazz = clazz.isArray() ? clazz.getComponentType() : clazz;
        IntFunction<R[]> generator = e -> (R[]) Array.newInstance(itemClazz, e);
        return toArray(list, action, generator);
    }

    /**
     * 将IPaged对象以一种类型转换成另一种类型
     *
     * @param page   源Page
     * @param action 转换规则
     * @param <E>    源Page类型泛型
     * @param <T>    源实体类
     * @param <R>    目标Page类型泛型
     * @return 变换后的分页类型
     */
    public static <E extends IPage<T>, T, R> IPage<R> toPage(E page, final Function<? super T, ? extends R> action) {
        Objects.requireNonNull(page);
        Objects.requireNonNull(action);
        return page.convert(action);
    }


    /**
     * <p>将{@code T}类型实体类对象转化为{@code Map}实例</p>
     *
     * @param obj POJO对象实例
     * @param <T> 对象类型
     * @return 以字段名称为Key 以字段值为Value的{@code Map}实例
     */
    public static <T> Map<String, Object> toMap(final T obj) {
        Objects.requireNonNull(obj);
        List<Field> fieldList = ReflectionKit.getFieldList(obj.getClass());
        HashMap<String, Object> map = new HashMap<>();
        fieldList.forEach(e -> map.put(e.getName(), ReflectionUtil.getFieldValue(obj, e.getName())));
        return map;
    }

    /**
     * <p>将{@code T}类型实体类对象转化为{@code Map}实例</p>
     *
     * @param pred 断言器 方便用户完成更高级的需求 如无需要 可忽略
     * @param obj  POJO对象实例
     * @param <T>  对象类型
     * @return 以字段名称为Key 以字段值为Value的{@code Map}实例
     */
    public static <T> Map<String, Object> toMap(final T obj, final Predicate<Field> pred) {
        Objects.requireNonNull(obj);
        Objects.requireNonNull(pred);
        List<Field> fieldList = ReflectionKit.getFieldList(obj.getClass()).stream().filter(pred).collect(Collectors.toList());
        HashMap<String, Object> map = new HashMap<>();
        fieldList.forEach(e -> map.put(e.getName(), ReflectionUtil.getFieldValue(obj, e.getName())));
        return map;
    }

    /**
     * 将集合转化成Map
     *
     * @param list      集合实例
     * @param keyAction key转换规则
     * @param <T>       集合实体类泛型
     * @param <K>       Key实体类型泛型
     * @return Map实例
     * @since 1.6.0
     */
    public static <T, K> Map<K, T> toMap(final Collection<T> list, Function<? super T, ? extends K> keyAction) {
        Objects.requireNonNull(keyAction);
        return toMap(list, keyAction, Function.identity());
    }

    /**
     * 将集合转化成Map
     *
     * @param list        集合实例
     * @param keyAction   key转换规则
     * @param valueAction value转换规则
     * @param <T>         集合实体类泛型
     * @param <K>         Key实体类型泛型
     * @param <V>         Value实体类型泛型
     * @return Map实例
     */
    public static <T, K, V> Map<K, V> toMap(final Collection<T> list, Function<? super T, ? extends K> keyAction, Function<? super T, ? extends V> valueAction) {
        Objects.requireNonNull(keyAction);
        Objects.requireNonNull(valueAction);
        if (ColUtils.isNotEmpty(list)) {
            return list.stream().collect(Collectors.toMap(keyAction, valueAction));
        }
        return Collections.emptyMap();
    }

    /**
     * 将List集合以一种类型转换成Set集合
     *
     * @param <T>  源数据类型
     * @param list 源List集合
     * @return 变换后的类型集合，如果source为null,则返回空集合
     */
    public static <T> Set<T> toSet(final Collection<T> list) {
        if (ColUtils.isNotEmpty(list)) {
            // 这里不使用流 使用构造器性能更好
            return new HashSet<>(list);
        }
        return Collections.emptySet();
    }

    /**
     * 将List集合以一种类型转换成Set集合
     *
     * @param <T>    源数据类型
     * @param <R>    变换后数据类型
     * @param data   源List集合
     * @param action 映射Lambda表达式
     * @return 变换后的类型集合，如果source为null,则返回空集合
     */
    public static <T, R> Set<R> toSet(final Collection<T> data, final Function<? super T, ? extends R> action) {
        Objects.requireNonNull(action);
        if (Objects.nonNull(data)) {
            return data.stream().map(action).collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    /**
     * <p>对集合中元素按照指定列进行分组</p>
     * <p>返回值是{@code Map}，其中Key为分组列，Value为当前元素</p>
     *
     * @param list    集合实例
     * @param gColumn 分组列（方法引用表示）
     * @param <E>     集合元素泛型
     * @param <G>     分组列数据类型泛型
     * @return {@code Map}实例
     * @since 1.6.0
     */
    public static <E, G> Map<G, List<E>> groupBy(final Collection<E> list, final Function<E, G> gColumn) {
        Objects.requireNonNull(gColumn);
        if (Objects.nonNull(list)) {
            return list.stream().collect(Collectors.groupingBy(gColumn));
        }
        return Collections.emptyMap();
    }

    /**
     * <p>对集合中元素按照指定列进行分组</p>
     * <p>返回值是{@code Map}，其中Key为分组列</p>
     *
     * @param list    集合实例
     * @param gColumn 分组列（方法引用表示）
     * @param action  转换行为
     * @param <U>     Value集合元素类型泛型
     * @param <E>     集合元素泛型
     * @param <G>     分组列数据类型泛型
     * @return {@code Map}实例
     * @since 1.6.0
     */
    public static <E, G, U> Map<G, List<U>> groupBy(final Collection<E> list, final Function<E, G> gColumn, final Function<E, U> action) {
        Objects.requireNonNull(gColumn);
        if (Objects.nonNull(list)) {
            return list.stream().collect(Collectors.groupingBy(gColumn, Collectors.mapping(action, Collectors.toList())));
        }
        return Collections.emptyMap();
    }
}
