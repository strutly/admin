package com.tsingtec.admin.vo.req.admin;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author lj
 * @Date 2021/12/15 14:28
 * @Version 1.0
 */
@Data
public class AdminEditReqVO {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "名称")
    @NotBlank(message = "显示名称不能为空")
    private String name;

    @ApiModelProperty(value = "账号")
    @NotBlank(message = "账号不能为空")
    private String loginName;

    @ApiModelProperty(value = "密码")
    @NotBlank(message = "密码不能为空")
    private String password;

    @ApiModelProperty(value = "状态")
    @NotNull(message = "状态不能为空")
    private Boolean status;
}
