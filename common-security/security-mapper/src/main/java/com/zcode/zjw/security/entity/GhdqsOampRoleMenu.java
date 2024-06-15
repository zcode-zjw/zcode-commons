package com.zcode.zjw.security.entity;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * GHDQS_OAMP_ROLE_MENU实体类
 * 
 * @author zhangjiwei
 *
 */
@Data
@ToString
public class GhdqsOampRoleMenu implements Serializable {

	/**
     * 角色ID
     */
    @ApiModelProperty(value = "角色ID", dataType = "Integer", required = true)
    private Integer roleId;



	/**
     * 菜单id
     */
    @ApiModelProperty(value = "菜单id", dataType = "Integer", required = true)
    private Integer menuId;



	
	/**
	 * 将当前对象转换为JsonObject
	 * 
	 * @return
	 */
	public JSONObject toJson() {
		JSONObject result = new JSONObject();
		if (this.getRoleId() != null) {
			result.put("roleId",this.getRoleId());
		}
		if (this.getMenuId() != null) {
			result.put("menuId",this.getMenuId());
		}
		return result;
	}
	
}
