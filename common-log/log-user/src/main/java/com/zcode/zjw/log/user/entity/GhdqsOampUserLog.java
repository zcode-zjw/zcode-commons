package com.zcode.zjw.log.user.entity;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * GHDQS_OAMP_USER_LOG实体类
 *
 * @author zhangjiwei
 */
@Data
@ToString
@TableName("sys_user_log")
public class GhdqsOampUserLog {

    /**
     * ID
     */
    @TableId(value = "ID", type = IdType.AUTO)
    @ApiModelProperty(value = "id", dataType = "Integer", required = true)
    private Integer id;
    /**
     * 模块代码
     */
    @ApiModelProperty(value = "模块代码", dataType = "String", required = true)
    private String moduleCode;


    /**
     * 类型
     */
    @ApiModelProperty(value = "类型", dataType = "Integer", required = true)
    private Integer type;


    /**
     * 标题
     */
    @ApiModelProperty(value = "标题", dataType = "String", required = true)
    private String title;


    /**
     * 操作ID
     */
    @ApiModelProperty(value = "操作ID", dataType = "Integer", required = true)
    private Integer operatorId;


    /**
     * 操作时间
     */
    @ApiModelProperty(value = "操作时间", dataType = "Date", required = true)
    private Date operateTime;


    /**
     * 内容
     */
    @ApiModelProperty(value = "内容", dataType = "String", required = true)
    private String content;



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
        if (this.getModuleCode() != null) {
            result.put("moduleCode", this.getModuleCode());
        }
        if (this.getType() != null) {
            result.put("type", this.getType());
        }
        if (this.getTitle() != null) {
            result.put("title", this.getTitle());
        }
        if (this.getOperatorId() != null) {
            result.put("operatorId", this.getOperatorId());
        }
        if (this.getOperateTime() != null) {
            result.put("operateTime", this.getOperateTime());
        }
        if (this.getContent() != null) {
            result.put("content", this.getContent());
        }
        return result;
    }


}
