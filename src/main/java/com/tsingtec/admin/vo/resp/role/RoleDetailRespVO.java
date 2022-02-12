package com.tsingtec.admin.vo.resp.role;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.tsingtec.admin.vo.resp.menu.MenuNodeRespVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author lj
 * @Date 2021/12/17 16:35
 * @Version 1.0
 */
@Data
public class RoleDetailRespVO {

    @ApiModelProperty(value = "名称")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "简介")
    private String description;

    @ApiModelProperty(value = "状态")
    private Boolean status;

    @ApiModelProperty(value = "权限集合")
    private List<MenuNodeRespVO> children;
}
