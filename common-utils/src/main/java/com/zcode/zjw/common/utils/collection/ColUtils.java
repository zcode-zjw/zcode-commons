
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

package com.zcode.zjw.common.utils.collection;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.ToIntFunction;

/**
 * 集合工具类
 *
 * @author zhangjiwei
 * @since 2020/06/03 23:17
 **/
public class ColUtils {
    private ColUtils() {
    }

    public static <E> boolean isEmpty(Collection<E> data) {
        if (data == null) {
            return true;
        } else {
            return data.size() == 0;
        }
    }

    public static <E> boolean isEmpty(E[] data) {
        if (data == null) {
            return true;
        } else {
            return data.length == 0;
        }
    }

    public static <E> boolean isNotEmpty(Collection<E> data) {
        return !isEmpty(data);
    }

    /**
     * <p>如果集合实例不为空 则执行{@link Consumer}函数式接口回调方法 无返回结果</p>
     *
     * @param coll     集合实例
     * @param consumer 有输入无输出函数式接口
     * @param <E>      集合实例类型
     */
    public static <E> void ifNotEmpty(Collection<E> coll, Consumer<Collection<E>> consumer) {
        Objects.requireNonNull(consumer);
        if (isNotEmpty(coll)) {
            consumer.accept(coll);
        }
    }

    /**
     * <p>如果集合实例不为空 则执行{@link Function}函数式接口回调方法 有返回结果</p>
     * <p>将以{@code E}类型为元素的集合，转换成以{@code R}类型为元素的集合</p>
     *
     * @param coll   集合实例
     * @param action 转换规则
     * @param <E>    集合实例类型
     * @param <R>    返回值集合实例类型
     * @return 以{@code R}类型为元素的集合实例
     */
    public static <E, R> List<R> ifNotEmpty(Collection<E> coll, Function<E, R> action) {
        Objects.requireNonNull(action);
        if (isNotEmpty(coll)) {
            List<R> rs = new ArrayList<>(coll.size());
            coll.forEach(e -> rs.add(action.apply(e)));
            return rs;
        }
        return Collections.emptyList();
    }

    public static <E> boolean isNotEmpty(E[] data) {
        return !isEmpty(data);
    }

    public static <E> void ifNotEmpty(E[] data, Consumer<E[]> consumer) {
        Objects.requireNonNull(consumer);
        if (isNotEmpty(data)) {
            consumer.accept(data);
        }
    }

    public static <E, R> List<R> ifNotEmpty(E[] data, Function<E, R> action) {
        Objects.requireNonNull(action);
        if (isNotEmpty(data)) {
            List<R> rs = new ArrayList<>(data.length);
            for (E e : data) {
                rs.add(action.apply(e));
            }
            return rs;
        }
        return Collections.emptyList();
    }

    /**
     * 将单个对象转化为集合
     *
     * @param obj 对象实例
     * @param <E> 对象类型
     * @return 包含对象的集合实例
     */
    public static <E> List<E> toCol(E obj) {
        if (obj == null) {
            return Collections.emptyList();
        }
        return Collections.singletonList(obj);
    }


    /**
     * 取出集合中第一个元素
     *
     * @param coll 集合实例
     * @param <E>  集合中元素类型
     * @return 泛型类型
     */
    public static <E> E toObj(Collection<E> coll) {
        if (isNotEmpty(coll)) {
            return coll.iterator().next();
        }
        return null;
    }

    /**
     * 以指定列为排序规则 获取排序列最大值所对应的对象
     *
     * @param data   集合实例
     * @param column 比较排序列
     * @param <E>    集合中元素泛型
     * @return 最大的元素对象
     * @since 1.6.0
     */
    public static <E> E max(Collection<E> data, ToIntFunction<? super E> column) {
        Objects.requireNonNull(column);
        if (isNotEmpty(data)) {
            return data.stream().max(Comparator.comparingInt(column)).orElse(null);
        }
        return null;
    }

    /**
     * @param data   集合实例
     * @param column 比较排序列
     * @param <E>    集合中元素泛型
     * @param <U>    排序列对应的对象泛型 如果是非基础数据类型 需要实现{@code Comparable}
     * @return 最大的元素对象
     * @since 1.6.0
     */
    public static <E, U extends Comparable<? super U>> E max(Collection<E> data, Function<? super E, ? extends U> column) {
        Objects.requireNonNull(column);
        if (isNotEmpty(data)) {
            return data.stream().max(Comparator.comparing(column)).orElse(null);
        }
        return null;
    }

    /**
     * @param data       集合实例
     * @param comparator 比较排序列
     * @param <E>        集合中元素泛型
     * @return 最小的元素对象
     * @since 1.6.0
     */
    public static <E> E max(Collection<E> data, Comparator<? super E> comparator) {
        Objects.requireNonNull(comparator);
        if (isNotEmpty(data)) {
            return data.stream().max(comparator).orElse(null);
        }
        return null;
    }

    /**
     * 以指定列为排序规则 获取最小元素的对象
     *
     * @param data   集合实例
     * @param column 比较排序列
     * @param <E>    集合元素泛型
     * @return 最小的元素对象
     * @since 1.6.0
     */
    public static <E> E min(Collection<E> data, ToIntFunction<? super E> column) {
        Objects.requireNonNull(column);
        if (isNotEmpty(data)) {
            return data.stream().min(Comparator.comparingInt(column)).orElse(null);
        }
        return null;
    }

    /**
     * @param data   集合实例
     * @param column 比较排序列
     * @param <E>    集合中元素泛型
     * @param <U>    排序列对应的对象泛型 如果是非基础数据类型 需要实现{@code Comparable}
     * @return 最小的元素对象
     * @since 1.6.0
     */
    public static <E, U extends Comparable<? super U>> E min(Collection<E> data, Function<? super E, ? extends U> column) {
        Objects.requireNonNull(column);
        if (isNotEmpty(data)) {
            return data.stream().min(Comparator.comparing(column)).orElse(null);
        }
        return null;
    }


    /**
     * @param data       集合实例
     * @param comparator 比较排序列
     * @param <E>        集合中元素泛型
     * @return 最小的元素对象
     * @since 1.6.0
     */
    public static <E> E min(Collection<E> data, Comparator<? super E> comparator) {
        Objects.requireNonNull(comparator);
        if (isNotEmpty(data)) {
            return data.stream().min(comparator).orElse(null);
        }
        return null;
    }
}
