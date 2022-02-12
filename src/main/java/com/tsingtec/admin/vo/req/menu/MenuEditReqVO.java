package com.tsingtec.admin.vo.req.menu;

import com.tsingtec.admin.constants.Constants;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author lj
 * @Date 2021/12/15 15:41
 * @Version 1.0
 */
@Data
public class MenuEditReqVO {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "名称")
    @NotBlank(message = "菜单权限名称不能为空")
    private String title;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "权限码")
    private String perms;

    @ApiModelProperty(value = "链接")
    private String url;

    @ApiModelProperty(value = "父id")
    @NotNull(message = "所属菜单不能为空")
    private Long pid;

    @ApiModelProperty(value = "排序码")
    private Integer orderNum = 0;

    @ApiModelProperty(value = "类型")
    private Integer type;//类型

    @ApiModelProperty(value = "状态")
    private Boolean status = Constants.VALID;
}
