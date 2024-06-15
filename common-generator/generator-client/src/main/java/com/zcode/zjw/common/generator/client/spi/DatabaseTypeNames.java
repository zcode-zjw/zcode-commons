package com.zcode.zjw.common.generator.client.spi;

import com.zcode.zjw.common.generator.client.common.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库名字
 * 
 * @author zhangjiwei
 *
 */
public class DatabaseTypeNames {
	/**
	 * 数据库的名称集合
	 * 
	 * @return
	 */
	public static List<String> dbTypeNames() {
		List<String> result = new ArrayList<>();
		result.add(Constant.MYSQL);
		result.add(Constant.POSTGRE_SQL);
		result.add(Constant.ORACLE);
		result.add(Constant.SQL_SERVER);
		result.add(Constant.SQLITE);
		return result;
	}
}
