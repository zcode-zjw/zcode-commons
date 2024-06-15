package com.zcode.zjw.common.generator.client.options;

import com.zcode.zjw.common.generator.client.models.TableAttributeKeyValue;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义属性的配置文件
 * 
 * @author zhangjiwei
 *
 */
public class CustomPropertyConfig {
	/** 设置的tableItem */
	private List<TableAttributeKeyValue> tableItem = new ArrayList<>();

	/**
	 * 初始化
	 */
	public CustomPropertyConfig() {
		super();
	}

	/**
	 * 通过 ObservableList<TableAttributeKeyValue>初始化
	 * 
	 * @param tableItem
	 */
	public CustomPropertyConfig(ObservableList<TableAttributeKeyValue> item) {
		super();
		if (item != null && !item.isEmpty()) {
			tableItem.addAll(item);
		}
	}

	/**
	 * 初始化默认参数
	 * 
	 * @return
	 */
	public CustomPropertyConfig initDefaultValue() {
		return this;
	}

	/**
	 * 获取属性集合
	 * 
	 * @return
	 */
	public List<TableAttributeKeyValue> getTableItem() {
		return tableItem;
	}

	/**
	 * 设置属性集合
	 * 
	 * @param tableItem
	 */
	public void setTableItem(List<TableAttributeKeyValue> tableItem) {
		this.tableItem = tableItem;
	}

	@Override
	public String toString() {
		return "CustomPropertyConfig [tableItem=" + tableItem + "]";
	}

}
