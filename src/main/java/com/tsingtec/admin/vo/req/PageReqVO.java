package com.tsingtec.admin.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;

/**
 * @Author lj
 * @Date 2021/12/15 14:21
 * @Version 1.0
 */
@Data
public class PageReqVO {

    @Min(1)
    @ApiModelProperty(value = "页码")
    private int pageNum=1;

    @Min(1)
    @ApiModelProperty(value = "分页大小")
    private int pageSize=10;
}
