package com.zcode.zjw.log.user.pojo;

import com.alibaba.fastjson.JSONObject;
import com.zcode.zjw.log.user.entity.GhdqsOampMenu;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * GHDQS_OAMP_ROLE DTO
 *
 * @author zhangjiwei
 */
@Data
@ToString
public class GhdqsOampRoleDTO implements Serializable {

    private static final long serialVersionUID = 5553646056675473759L;

    /**
     * ID
     */
    @ApiModelProperty(value = "ID", dataType = "Integer", required = true)
    private Integer id;



    /**
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称", dataType = "String", required = true)
    private String name;



    /**
     * 角色权限字符串
     */
    @ApiModelProperty(value = "角色权限字符串", dataType = "String", required = true)
    private String roleKey;



    /**
     * 1停用）
     */
    @ApiModelProperty(value = "1停用）", dataType = "String", required = true)
    private String status;



    /**
     * 删除标识
     */
    @ApiModelProperty(value = "删除标识", dataType = "Integer", required = true)
    private Integer delFlag;



    /**
     * *
     */
    @ApiModelProperty(value = "*", dataType = "Integer", required = true)
    private Integer createBy;



    /**
     * *
     */
    @ApiModelProperty(value = "*", dataType = "Date", required = true)
    private Date createTime;



    /**
     * *
     */
    @ApiModelProperty(value = "*", dataType = "Integer", required = true)
    private Integer updateBy;



    /**
     * *
     */
    @ApiModelProperty(value = "*", dataType = "Date", required = true)
    private Date updateTime;



    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", dataType = "String", required = true)
    private String remark;



    /**
     * 拥有的权限菜单
     */
    @ApiModelProperty(value = "拥有的权限菜单", dataType = "List<GhdqsOampMenu>", required = true)
    private List<GhdqsOampMenu> permissionList;




    /**
     * 将当前对象转换为JsonObject
     *
     * @return
     */
    public JSONObject toJson() {
        JSONObject result = new JSONObject();
        if (this.getId() != null) {
            result.put("id", this.getId());
        }
        if (this.getName() != null) {
            result.put("name", this.getName());
        }
        if (this.getRoleKey() != null) {
            result.put("roleKey", this.getRoleKey());
        }
        if (this.getStatus() != null) {
            result.put("status", this.getStatus());
        }
        if (this.getDelFlag() != null) {
            result.put("delFlag", this.getDelFlag());
        }
        if (this.getCreateBy() != null) {
            result.put("createBy", this.getCreateBy());
        }
        if (this.getCreateTime() != null) {
            result.put("createTime", this.getCreateTime());
        }
        if (this.getUpdateBy() != null) {
            result.put("updateBy", this.getUpdateBy());
        }
        if (this.getUpdateTime() != null) {
            result.put("updateTime", this.getUpdateTime());
        }
        if (this.getRemark() != null) {
            result.put("remark", this.getRemark());
        }
        return result;
    }

}
