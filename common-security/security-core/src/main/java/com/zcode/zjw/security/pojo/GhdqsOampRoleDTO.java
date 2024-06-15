package com.zcode.zjw.security.pojo;

import com.alibaba.fastjson.JSONObject;
import com.zcode.zjw.security.entity.GhdqsOampMenu;
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
    private Integer id;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色权限字符串
     */
    private String roleKey;

    /**
     * 角色状态（0正常 1停用）
     */
    private String status;

    /**
     * 删除标识
     */
    private Integer delFlag;

    /**
     *
     */
    private Integer createBy;

    /**
     *
     */
    private Date createTime;

    /**
     *
     */
    private Integer updateBy;

    /**
     *
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 拥有的权限菜单
     */
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
