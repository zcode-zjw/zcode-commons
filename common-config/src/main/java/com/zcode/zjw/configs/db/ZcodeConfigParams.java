package com.zcode.zjw.configs.db;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Zcode配置（数据库）
 *
 * @author zhangjiwei
 * @date 2023/6/3
 */
@TableName("zcode_config_params")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ZcodeConfigParams {

    @TableId
    private Long id;

    private String pkey;

    private String pval;

    private String description;

    /**
     * 参数等级（越大表示等级越低）
     */
    private Integer plevel;

}
