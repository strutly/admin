package com.tsingtec.admin.vo.resp.role;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author lj
 * @Date 2021/12/17 16:27
 * @Version 1.0
 */
@Data
public class RolePageRespVO {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "简介")
    private String description;

    @ApiModelProperty(value = "状态")
    private Boolean status;

    @ApiModelProperty(value = "最后登录时间")
    @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss",timezone = "GMT+8")
    private LocalDateTime updateTime;
}
