package com.tsingtec.admin.vo.resp.menu;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author lj
 * @Date 2021/12/15 15:18
 * @Version 1.0
 */
@Data
public class MenuNodeRespVO {

    @ApiModelProperty(value = "id")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "名称")
    private String title;//名称

    private String icon = "layui-icon-home";

    private String perms;//授权标识

    private String url;//链接

    @JsonSerialize(using= ToStringSerializer.class)
    private Long pid;//

    private String pidName;

    private Integer orderNum = 0;//排序

    private Integer type;//类型

    private List<MenuNodeRespVO> children;

    private Boolean spread = false;

    private Boolean checked = false;

}
