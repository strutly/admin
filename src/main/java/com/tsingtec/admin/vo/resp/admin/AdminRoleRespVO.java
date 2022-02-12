package com.tsingtec.admin.vo.resp.admin;

import com.tsingtec.admin.entity.Role;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author lj
 * @Date 2021/12/14 16:59
 * @Version 1.0
 */
@Data
public class AdminRoleRespVO {

    @ApiModelProperty(value = "所有权限")
    private List<Role> allRole;//所有的可以设置的权限

    @ApiModelProperty(value = "拥有的权限")
    private List<Long> ownRole;//拥有的权限
}
