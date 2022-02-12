package com.tsingtec.admin.vo.req.admin;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author lj
 * @Date 2021/12/15 15:09
 * @Version 1.0
 */
@Data
public class AdminRoleSetReqVO {

    @ApiModelProperty(value = "旧密码")
    @NotNull(message = "id不能为空")
    private Long id;

    @ApiModelProperty(value = "权限id集合")
    private List<Long> rids = Lists.newArrayList();
}
