package com.zcode.zjw.log.user.entity;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * GHDQS_OAMP_MENU实体类
 *
 * @author zhangjiwei
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class GhdqsOampMenu implements Serializable {

    private static final long serialVersionUID = 7874499784639882218L;

    /**
     * ID
     */
    @ApiModelProperty(value = "* ID", dataType = "Integer", required = true)
    private Integer id;


    /**
     * 菜单名
     */
    @ApiModelProperty(value = "* 菜单名", dataType = "String", required = true)
    private String menuName;


    /**
     * 路由地址
     */
    @ApiModelProperty(value = "* 路由地址", dataType = "String", required = true)
    private String path;


    /**
     * 组件路径
     */
    @ApiModelProperty(value = "* 组件路径", dataType = "String", required = true)
    private String component;


    /**
     * 菜单状态（0显示 1隐藏）
     */
    @ApiModelProperty(value = "* 菜单状态（0显示 1隐藏）", dataType = "String", required = true)
    private String visible;


    /**
     * 菜单状态（0正常 1停用）
     */
    @ApiModelProperty(value = "* 菜单状态（0正常 1停用）", dataType = "String", required = true)
    private String status;


    /**
     * 权限标识
     */
    @ApiModelProperty(value = "* 权限标识", dataType = "String", required = true)
    private String perms;


    /**
     * 菜单图标
     */
    @ApiModelProperty(value = "* 菜单图标", dataType = "String", required = true)
    private String icon;


    /***/
    private Integer createBy;
    /***/
    private Date createTime;
    /***/
    private Integer updateBy;
    /***/
    private Date updateTime;
    /**
     * 是否删除（0未删除 1已删除）
     */
    @ApiModelProperty(value = "* 是否删除（0未删除 1已删除）", dataType = "Integer", required = true)
    private Integer delFlag;


    /**
     * 备注
     */
    @ApiModelProperty(value = "* 备注", dataType = "String", required = true)
    private String remark;



    /**
     * 实例化
     *
     * @param obj
     */
    public GhdqsOampMenu(JSONObject obj) {
        this();
        if (obj.get("id") instanceof Number) {
            this.setId(((Number) obj.get("id")).intValue());
        }
        if (obj.get("menuName") instanceof String) {
            this.setMenuName((String) obj.get("menuName"));
        }
        if (obj.get("path") instanceof String) {
            this.setPath((String) obj.get("path"));
        }
        if (obj.get("component") instanceof String) {
            this.setComponent((String) obj.get("component"));
        }
        if (obj.get("visible") instanceof String) {
            this.setVisible((String) obj.get("visible"));
        }
        if (obj.get("status") instanceof String) {
            this.setStatus((String) obj.get("status"));
        }
        if (obj.get("perms") instanceof String) {
            this.setPerms((String) obj.get("perms"));
        }
        if (obj.get("icon") instanceof String) {
            this.setIcon((String) obj.get("icon"));
        }
        if (obj.get("createBy") instanceof Number) {
            this.setCreateBy(((Number) obj.get("createBy")).intValue());
        }
        this.setCreateTime((Date) obj.get("createTime"));
        if (obj.get("updateBy") instanceof Number) {
            this.setUpdateBy(((Number) obj.get("updateBy")).intValue());
        }
        this.setUpdateTime((Date) obj.get("updateTime"));
        if (obj.get("delFlag") instanceof Number) {
            this.setDelFlag(((Number) obj.get("delFlag")).intValue());
        }
        if (obj.get("remark") instanceof String) {
            this.setRemark((String) obj.get("remark"));
        }
    }

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
        if (this.getMenuName() != null) {
            result.put("menuName", this.getMenuName());
        }
        if (this.getPath() != null) {
            result.put("path", this.getPath());
        }
        if (this.getComponent() != null) {
            result.put("component", this.getComponent());
        }
        if (this.getVisible() != null) {
            result.put("visible", this.getVisible());
        }
        if (this.getStatus() != null) {
            result.put("status", this.getStatus());
        }
        if (this.getPerms() != null) {
            result.put("perms", this.getPerms());
        }
        if (this.getIcon() != null) {
            result.put("icon", this.getIcon());
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
        if (this.getDelFlag() != null) {
            result.put("delFlag", this.getDelFlag());
        }
        if (this.getRemark() != null) {
            result.put("remark", this.getRemark());
        }
        return result;
    }


}
