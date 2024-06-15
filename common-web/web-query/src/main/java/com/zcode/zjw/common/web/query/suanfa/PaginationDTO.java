package com.zcode.zjw.common.web.query.suanfa;

import lombok.Data;

/**
 * @author zhangjiwei
 * @version 1.0.0
 * @description 分页DTO
 * @since 1.0.0
 */
@Data
//@ApiModel(value = "分页数据对象")
public class PaginationDTO {
    /**
     * 页码
     */
    //@ApiModelProperty(value = "页码")
    private Integer page;

    /**
     * 每页大小
     */
    //@ApiModelProperty(value = "每页大小")
    private Integer size;
}
