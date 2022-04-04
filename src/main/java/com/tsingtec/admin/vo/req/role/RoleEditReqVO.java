package com.tsingtec.admin.vo.req.role;

import com.google.common.collect.Lists;
import com.tsingtec.admin.constants.Constants;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author lj
 * @Date 2021/12/17 14:14
 * @Version 1.0
 */
@Data
public class RoleEditReqVO {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "简介")
    private String description;

    @ApiModelProperty(value = "状态")
    private Boolean status = Constants.UNVALID;

    @ApiModelProperty(value = "权限id集合")
    private List<Long> mids = Lists.newArrayList();

}
