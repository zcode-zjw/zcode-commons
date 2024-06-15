package com.zcode.zjw.common.file.upload.chuck.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zhangjiwei
 * @description 分块文件实体
 * @date 2022年11月18日 下午 11:00
 */
@Data
@TableName("chuck_file")
public class ChuckFile implements Serializable {
    @TableId(value = "ID", type = IdType.AUTO)
    protected Long id;

    @TableField("FILENAME")
    protected String filename;

    @TableField("IDENTIFIER")
    protected String identifier;

    @TableField("TOTAL_SIZE")
    protected Long totalSize;

    @TableField("TYPE")
    protected String type;

    @TableField("LOCATION")
    protected String location;
}