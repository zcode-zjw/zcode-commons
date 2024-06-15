package com.zcode.zjw.common.utils.tree;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.zcode.zjw.common.utils.reflect.ReflectionUtil;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * <p>列表转树通用工具类</p>
 *
 * <table>
 *     <caption>列表数据</caption>
 *     <tr>
 *         <th>id</th>
 *         <th>name</th>
 *         <th>pid</th>
 *     </tr>
 *     <tr>
 *         <td>310000</td>
 *         <td>电子商务</td>
 *         <td>000000</td>
 *     </tr>
 *     <tr>
 *         <td>310100</td>
 *         <td>大宗商品</td>
 *         <td>310000</td>
 *     </tr>
 *     <tr>
 *         <td>310101</td>
 *         <td>大宗商品综合</td>
 *         <td>310100</td>
 *     </tr>
 *     <tr>
 *         <td>310102</td>
 *         <td>钢铁类电商</td>
 *         <td>310100</td>
 *     </tr>
 * </table>
 *
 * <p>通过列表转树{@code TreeUtils}工具类，可快速转化为如下形式的数据结构</p>
 * <pre>
 *     {
 *       "id": "310000",
 *       "name": "电子商务",
 *       "childList": [
 *         {
 *           "id": "310100",
 *           "name": "大宗商品",
 *           "childList": [
 *             {
 *               "id": "310101",
 *               "name": "大宗商品综合",
 *               "childList": []
 *             },
 *             {
 *               "id": "310102",
 *               "name": "钢铁类电商",
 *               "childList": []
 *             }
 *           ]
 *         }
 *       ]
 *     }
 * </pre>
 *
 * @author zhangjiwei
 */
public class TreeUtils {
    private TreeUtils() {
    }

    /**
     * 列表转树
     *
     * @param data     实现{@link ITreeEntity}接口实体的集合实例
     * @param parentId 查询开始的父ID
     * @param <T>      id或者pid的类型泛型
     * @return {@link TreeNode}的集合实例
     * @see TreeUtils#createNode(List, Object, Comparator)
     */
    public static <T> List<TreeNode<T>> createNode(List<? extends ITreeEntity<T>> data, T parentId) {
        return doCreateNode(data, parentId, null);
    }

    /**
     * 列表转树
     *
     * @param data       实现{@link ITreeEntity}接口实体的集合实例
     * @param parentId   查询开始的父ID
     * @param comparator 排序器指定元素的顺序
     * @param <T>        id或者pid的类型泛型
     * @return {@link TreeNode}的集合实例
     */
    public static <T> List<TreeNode<T>> createNode(List<? extends ITreeEntity<T>> data, T parentId, Comparator<TreeNode<T>> comparator) {
        return doCreateNode(data, parentId, comparator);
    }

    /**
     * <p>列表转树</p>
     * <p>具体实现通过使用{@code Map}优化性能</p>
     *
     * <table border="1">
     *   <caption>data数据结构样例</caption>
     *     <tr>
     *         <th>id</th>
     *         <th>name</th>
     *         <th>parentId</th>
     *     </tr>
     *     <tr>
     *         <td>310000</td>
     *         <td>电子商务</td>
     *         <td>000000</td>
     *     </tr>
     *     <tr>
     *         <td>310100</td>
     *         <td>大宗商品</td>
     *         <td>310000</td>
     *     </tr>
     *     <tr>
     *         <td>310200</td>
     *         <td>综合电商</td>
     *         <td>310000</td>
     *     </tr>
     *     <tr>
     *         <td>310101</td>
     *         <td>大宗商品综合</td>
     *         <td>310100</td>
     *     </tr>
     *     <tr>
     *         <td>310102</td>
     *         <td>钢铁类电商</td>
     *         <td>310100</td>
     *     </tr>
     * </table>
     *
     * @param data       列表的全部数据集
     * @param parentId   第一级目录的父ID
     * @param idAction   指定ID列（方法引用表示）
     * @param nameAction 指定名称列（方法引用表示）
     * @param pidAction  指定PID列（方法引用表示）
     * @param <D>        列表元素实体泛型
     * @param <T>        ID列泛型
     * @return {@link TreeNode}的集合实例
     * @see TreeUtils#createNode(List, Object, Function, Function, Function, Comparator)
     */
    public static <D, T> List<TreeNode<T>> createNode(List<D> data, T parentId, Function<D, T> idAction, Function<D, String> nameAction, Function<D, T> pidAction) {
        return createNode(data, parentId, idAction, nameAction, pidAction, null);
    }

    /**
     * 列表转树
     *
     * <table border="1">
     *   <caption>data数据结构样例</caption>
     *     <tr>
     *         <th>id</th>
     *         <th>name</th>
     *         <th>parentId</th>
     *     </tr>
     *     <tr>
     *         <td>310000</td>
     *         <td>电子商务</td>
     *         <td>000000</td>
     *     </tr>
     *     <tr>
     *         <td>310100</td>
     *         <td>大宗商品</td>
     *         <td>310000</td>
     *     </tr>
     *     <tr>
     *         <td>310200</td>
     *         <td>综合电商</td>
     *         <td>310000</td>
     *     </tr>
     *     <tr>
     *         <td>310101</td>
     *         <td>大宗商品综合</td>
     *         <td>310100</td>
     *     </tr>
     *     <tr>
     *         <td>310102</td>
     *         <td>钢铁类电商</td>
     *         <td>310100</td>
     *     </tr>
     * </table>
     *
     * @param data       列表的全部数据集
     * @param parentId   第一级目录的父ID
     * @param idAction   指定ID列（方法引用表示）
     * @param nameAction 指定名称列（方法引用表示）
     * @param pidAction  指定PID列（方法引用表示）
     * @param comparator 排序器
     * @param <D>        列表元素实体泛型
     * @param <T>        ID列泛型
     * @return {@link TreeNode}的集合实例
     * @since 1.5.8.1
     */
    public static <D, T> List<TreeNode<T>> createNode(List<D> data, T parentId, Function<D, T> idAction, Function<D, String> nameAction, Function<D, T> pidAction, Comparator<TreeNode<T>> comparator) {
        Map<T, List<D>> map = data.stream().collect(Collectors.groupingBy(pidAction));
        return doCreateNode(map, parentId, idAction, nameAction, comparator);
    }

    /**
     * <p>列表转树</p>
     * <p>附带除id name pid外其它字段</p>
     *
     * <table border="1">
     *   <caption>data数据结构样例</caption>
     *     <tr>
     *         <th>id</th>
     *         <th>name</th>
     *         <th>parentId</th>
     *         <th>remark</th>
     *         <th>childList</th>
     *     </tr>
     *     <tr>
     *         <td>310000</td>
     *         <td>电子商务</td>
     *         <td>000000</td>
     *         <td>EEEEEE</td>
     *         <td></td>
     *     </tr>
     *     <tr>
     *         <td>310100</td>
     *         <td>大宗商品</td>
     *         <td>310000</td>
     *         <td>JJJJJJ</td>
     *         <td></td>
     *     </tr>
     *     <tr>
     *         <td>310200</td>
     *         <td>综合电商</td>
     *         <td>310000</td>
     *         <td>NNNNNN</td>
     *         <td></td>
     *     </tr>
     *     <tr>
     *         <td>310101</td>
     *         <td>大宗商品综合</td>
     *         <td>310100</td>
     *         <td>IIIIII</td>
     *         <td></td>
     *     </tr>
     *     <tr>
     *         <td>310102</td>
     *         <td>钢铁类电商</td>
     *         <td>310100</td>
     *         <td>PPPPPP</td>
     *         <td></td>
     *     </tr>
     * </table>
     *
     * @param data        列表的全部数据集 data的泛型不是DO 是BO
     * @param parentId    第一级目录的父ID
     * @param idAction    指定ID列（方法引用表示）
     * @param childAction 指定Child列（方法引用表示）
     * @param <B>         DO转BO后实体类泛型
     * @param <T>         ID列泛型
     * @param <L>         Child列泛型（List）
     * @return {@link B}的集合实例
     */
    public static <B, T, L> List<B> createNodeDetail(List<B> data, T parentId, Function<B, T> idAction, Function<B, T> pidAction, SFunction<B, L> childAction) {
        Map<T, List<B>> map = data.stream().collect(Collectors.groupingBy(pidAction));
        return doCreateNode(map, parentId, idAction, childAction);
    }

    /**
     * 列表转树
     *
     * @param root     列表的全部数据集
     * @param parentId 第一级目录的父ID
     * @return 树形列表
     */
    private static <T> List<TreeNode<T>> doCreateNode(List<? extends ITreeEntity<T>> root, T parentId, Comparator<TreeNode<T>> comparator) {
        List<TreeNode<T>> lists = root.stream()
                .filter(e -> e.getParentId().equals(parentId))
                .map(e -> new TreeNode<>(e.getId(), e.getName()))
                .collect(Collectors.toList());
        Optional.ofNullable(comparator).ifPresent(lists::sort);
        lists.forEach(e -> e.setChildList(doCreateNode(root, e.getId(), comparator)));
        return lists;
    }


    private static <T> List<TreeNode<T>> doCreateNode(Map<T, List<? extends ITreeEntity<T>>> map, T parentId, Comparator<TreeNode<T>> comparator) {
        List<TreeNode<T>> lists = EntityUtils.toList(map.get(parentId), e -> new TreeNode<>(e.getId(), e.getName()));
        Optional.ofNullable(comparator).ifPresent(lists::sort);
        lists.forEach(e -> e.setChildList(doCreateNode(map, e.getId(), comparator)));
        return lists;
    }


    /**
     * 列表转树
     *
     * @param root       列表的全部数据集
     * @param parentId   第一级目录的父ID
     * @param idAction   指定ID列（方法引用表示）
     * @param nameAction 指定名称列（方法引用表示）
     * @param pidAction  指定PID列（方法引用表示）
     * @param comparator 比较器
     * @param <W>        列表元素实体泛型
     * @param <T>        ID列泛型
     * @return 树形列表
     */
    private static <W, T> List<TreeNode<T>> doCreateNode(List<W> root, T parentId, Function<W, T> idAction, Function<W, String> nameAction, Function<W, T> pidAction, Comparator<TreeNode<T>> comparator) {
        List<TreeNode<T>> lists = root.stream()
                .filter(e -> EntityUtils.toObj(e, pidAction).equals(parentId))
                .map(e -> new TreeNode<>(EntityUtils.toObj(e, idAction), EntityUtils.toObj(e, nameAction)))
                .collect(Collectors.toList());
        Optional.ofNullable(comparator).ifPresent(lists::sort);
        lists.forEach(e -> e.setChildList(doCreateNode(root, e.getId(), idAction, nameAction, pidAction, comparator)));
        return lists;
    }

    /**
     * 使用{@code Map}替换{@code filter}优化性能
     *
     * @param map        原始列表以PID分组后形成的{@code Map}实例
     * @param parentId   第一级目录的父ID
     * @param idAction   指定ID列（方法引用表示）
     * @param nameAction 指定名称列（方法引用表示）
     * @param comparator 比较器
     * @param <W>        列表元素实体泛型
     * @param <T>        ID列泛型
     * @return 树形列表
     * @since 1.5.8.2
     */
    private static <W, T> List<TreeNode<T>> doCreateNode(Map<T, List<W>> map, T parentId, Function<W, T> idAction, Function<W, String> nameAction, Comparator<TreeNode<T>> comparator) {
        List<TreeNode<T>> lists = EntityUtils.toList(map.get(parentId), e -> new TreeNode<>(EntityUtils.toObj(e, idAction), EntityUtils.toObj(e, nameAction)));
        Optional.ofNullable(comparator).ifPresent(lists::sort);
        lists.forEach(e -> e.setChildList(doCreateNode(map, e.getId(), idAction, nameAction, comparator)));
        return lists;
    }

    private static <B, T, L> List<B> doCreateNode(Map<T, List<B>> map, T parentId, Function<B, T> idAction, SFunction<B, L> childAction) {
        List<B> lists = Optional.ofNullable(map.get(parentId)).orElse(new ArrayList<>());
        if (lists.size() > 0) {
            lists.forEach(e -> ReflectionUtil.setFiledValue(e, childAction, doCreateNode(map, EntityUtils.toObj(e, idAction), idAction, childAction)));
        }
        return lists;
    }

    public static void main(String[] args) {
//        List<Object> list = new ArrayList<>();
//        TreeUtils.createNode(list, "00024");
    }

}
