package com.zcode.zjw.common.file.upload.chuck.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Transient;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * @author zhangjiwei
 * @description 分块文件实体
 * @date 2022年11月18日 下午 11:02
 */

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("chuck")
public class Chuck extends ChuckFile implements Serializable {

    /**
     * 当前文件块，从1开始
     */
    @TableField("CHUNK_NUMBER")
    private Integer chunkNumber;
    /**
     * 分块大小
     */
    @TableField("CHUNK_SIZE")
    private Long chunkSize;
    /**
     * 当前分块大小
     */
    @TableField("CURRENT_CHUNK_SIZE")
    private Long currentChunkSize;

    /**
     * 相对路径
     */
    @TableField("RELATIVE_PATH")
    private String relativePath;
    /**
     * 总块数
     */
    @TableField("TOTAL_CHUNKS")
    private Integer totalChunks;

    /**
     * form表单的file对象，为了不让Fastjson序列化，注解设置false
     */
    @Transient
    @JSONField(serialize = false)
    @TableField(exist = false)
    private MultipartFile file;
}