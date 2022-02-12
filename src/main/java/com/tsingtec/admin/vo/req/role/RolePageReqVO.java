package com.tsingtec.admin.vo.req.role;

import com.tsingtec.admin.vo.req.PageReqVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author lj
 * @Date 2021/12/15 14:20
 * @Version 1.0
 */
@Data
public class RolePageReqVO{

    @ApiModelProperty(value = "名称")
    private String name;

}
