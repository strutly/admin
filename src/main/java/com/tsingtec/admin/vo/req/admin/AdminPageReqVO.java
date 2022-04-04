package com.tsingtec.admin.vo.req.admin;

import com.tsingtec.admin.vo.req.PageReqVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author lj
 * @Date 2021/12/15 14:20
 * @Version 1.0
 */
@Data
public class AdminPageReqVO extends PageReqVO {

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "账号")
    private String loginName;
}
