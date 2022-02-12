package com.tsingtec.admin.vo.resp.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author lj
 * @Date 2021/12/17 10:33
 * @Version 1.0
 */
@Data
public class AdminRespVO {

    @JsonSerialize(using= ToStringSerializer.class)
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "账号名称")
    private String name;

    @ApiModelProperty(value = "登录名")
    private String loginName;

    @ApiModelProperty(value = "状态;[可用,禁用]")
    private Boolean status;

    @ApiModelProperty(value = "最后登录时间")
    @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss",timezone = "GMT+8")
    private LocalDateTime updateTime;
}
