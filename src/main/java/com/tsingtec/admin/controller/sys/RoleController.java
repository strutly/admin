package com.tsingtec.admin.controller.sys;

import com.tsingtec.admin.controller.GenericController;
import com.tsingtec.admin.entity.Role;
import com.tsingtec.admin.service.RoleService;
import com.tsingtec.admin.vo.req.role.RoleEditReqVO;
import com.tsingtec.admin.vo.req.role.RolePageReqVO;
import com.tsingtec.admin.vo.resp.role.RoleDetailRespVO;
import com.tsingtec.admin.vo.resp.role.RolePageRespVO;
import com.tsingtec.commons.mapper.BeanMapper;
import com.tsingtec.commons.support.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/manager")
@Api(tags = "组织模块-角色管理")
public class RoleController extends GenericController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/role")
    @ApiOperation(value = "分页获取角色信息接口")
    @RequiresPermissions("sys:role:page")
    public R<Page<RolePageRespVO>> pageInfo(RolePageReqVO vo,@PageableDefault(sort = "id",
            direction = Sort.Direction.DESC) Pageable pageable){
        Role role = BeanMapper.map(vo,Role.class);
        role.setIfSuper(this.getAdmin().getIfSuper());
        role.setCreatedId(this.getAid());
        return R.ok(roleService.page(role,pageable).map(r->BeanMapper.map(r, RolePageRespVO.class)));
    }


    @PostMapping("/role")
    @ApiOperation(value = "新增角色接口")
    @RequiresPermissions("sys:role:add")
    public R<Role> addRole(@RequestBody @Valid RoleEditReqVO vo){
        Role role = BeanMapper.map(vo,Role.class);
        roleService.save(role,vo.getMids());
        return R.ok();
    }

    @DeleteMapping("/role")
    @ApiOperation(value = "删除角色接口")
    @RequiresPermissions("sys:role:delete")
    public R delete(@RequestBody @ApiParam(value = "id集合") List<Long> rids){
        roleService.deleteBatch(this.getAid(),rids);
        return R.ok();
    }

    @PutMapping("/role")
    @RequiresPermissions("sys:role:update")
    @ApiOperation(value = "更新角色信息接口")
    public R update(@RequestBody @Valid RoleEditReqVO vo){
        Role role = roleService.findById(vo.getId());
        if(null == role){
            return R.fail("您没有权限进行此操作");
        }
        BeanMapper.mapExcludeNull(vo,role);
        roleService.save(role,vo.getMids());
        return R.ok();
    }

    @GetMapping("/role/{id}")
    @RequiresPermissions("sys:role:detail")
    @ApiOperation(value = "查询角色详情接口")
    public R<RoleDetailRespVO> detail(@PathVariable("id") Long id){
        Role role = roleService.getByAidAndId(this.getAid(),id);
        return R.ok(BeanMapper.map(role,RoleDetailRespVO.class));
    }
}
