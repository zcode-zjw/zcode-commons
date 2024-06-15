package com.zcode.zjw.security.entity;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * GHDQS_OAMP_USER_ROLE实体类
 * 
 * @author zhangjiwei
 *
 */
@Data
@ToString
public class GhdqsOampUserRole implements Serializable {

	/**
     * 用户id
     */
    @ApiModelProperty(value = "用户id", dataType = "Integer", required = true)
    private Integer userId;



	/**
     * 角色id
     */
    @ApiModelProperty(value = "角色id", dataType = "Integer", required = true)
    private Integer roleId;



	
	/**
	 * 将当前对象转换为JsonObject
	 * 
	 * @return
	 */
	public JSONObject toJson() {
		JSONObject result = new JSONObject();
		if (this.getUserId() != null) {
			result.put("userId",this.getUserId());
		}
		if (this.getRoleId() != null) {
			result.put("roleId",this.getRoleId());
		}
		return result;
	}
	
}
