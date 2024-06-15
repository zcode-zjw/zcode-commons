package com.zcode.zjw.common.utils.tree;

/**
 * 列表实体类实现{@code ITreeEntity}接口抽象方法
 *
 * @param <T> id或者pid的泛型
 * @author zhangjiwei
 **/
public interface ITreeEntity<T> {
    /**
     * 获取ID的值
     *
     * @return ID
     */
    T getId();

    /**
     * 获取名称值
     *
     * @return 名称
     */
    String getName();

    /**
     * 获取父ID的值
     *
     * @return 父ID
     */
    T getParentId();

}
