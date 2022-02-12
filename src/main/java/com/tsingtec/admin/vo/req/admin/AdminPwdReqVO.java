package com.tsingtec.admin.vo.req.admin;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author lj
 * @Date 2021/12/15 15:01
 * @Version 1.0
 */
@Data
public class AdminPwdReqVO {

    @ApiModelProperty(value = "旧密码")
    @NotBlank(message = "旧密码不能为空")
    private String oldPwd;

    @ApiModelProperty(value = "newPwd")
    @NotBlank(message = "新密码不能为空")
    private String newPwd;
}
