package com.tsingtec.admin.controller.sys;

import com.tsingtec.admin.controller.GenericController;
import com.tsingtec.admin.entity.Admin;
import com.tsingtec.admin.service.AdminService;
import com.tsingtec.admin.vo.req.admin.AdminEditReqVO;
import com.tsingtec.admin.vo.req.admin.AdminPageReqVO;
import com.tsingtec.admin.vo.req.admin.AdminPwdReqVO;
import com.tsingtec.admin.vo.req.admin.AdminRoleSetReqVO;
import com.tsingtec.admin.vo.resp.admin.AdminRespVO;
import com.tsingtec.admin.vo.resp.admin.AdminRoleRespVO;
import com.tsingtec.commons.mapper.BeanMapper;
import com.tsingtec.commons.support.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/manager")
public class AdminController extends GenericController{

    @Autowired
    private AdminService adminService;

    /**
     * 分页获取账号
     * @param vo
     * @return
     */
    @GetMapping("/admin")
    @RequiresPermissions("sys:admin:page")
    public R<Page<AdminRespVO>> pageInfo(AdminPageReqVO vo,@PageableDefault(sort = "id",
            direction = Sort.Direction.DESC) Pageable pageable) {
        Admin admin = BeanMapper.map(vo,Admin.class);
        admin.setIfSuper(this.getAdmin().getIfSuper());
        admin.setCreatedId(this.getAid());
        return R.ok(adminService.page(admin,pageable).map(a->BeanMapper.map(a,AdminRespVO.class)));
    }

    @GetMapping("/admin/{id}")
    @RequiresPermissions("sys:admin:detail")
    public R detail(@PathVariable("id")Long id) {
        return R.ok(adminService.findById(id));
    }

    /**
     * 新增账号
     * @param vo
     * @return
     */
    @PostMapping("/admin")
    @RequiresPermissions("sys:admin:add")
    public R add(@RequestBody @Valid AdminEditReqVO vo){
        Admin admin = BeanMapper.map(vo,Admin.class);
        admin.setCreatedId(this.getAid());
        adminService.addAdmin(admin);
        return R.ok();
    }


    @PutMapping("/admin")
    @RequiresPermissions("sys:admin:update")
    public R update(@RequestBody AdminEditReqVO vo){
        Admin admin = adminService.findById(vo.getId());
        if(null==admin) return R.fail("无此账号~");
        /**
         * 非超级用户不能修改超级账号
         * 不能修改不是自己创建的账号
         */
        if(!this.getAdmin().getIfSuper() && (admin.getIfSuper()|| !admin.getCreatedId().equals(this.getAid()))){
            return R.fail("您没有权限对此账号进行此操作");
        }
        BeanMapper.mapExcludeNull(vo,admin);
        adminService.updateAdmin(admin);
        return R.ok();
    }

    @PutMapping("/admin/psd")
    @RequiresPermissions("sys:admin:psd")
    public R updatePwd(@RequestBody AdminPwdReqVO vo){
        Admin admin = this.getAdmin();
        adminService.updatePwd(admin.getId(),vo.getOldPwd(),vo.getNewPwd());
        return R.ok();
    }

    /**
     * 不能删除admin 账号
     * @param aids
     * @return
     */
    @DeleteMapping("/admin")
    @RequiresPermissions("sys:admin:delete")
    public R deletedUser(@RequestBody List<Long> aids){
        Admin admin = this.getAdmin();
        adminService.deleteById(admin,aids);
        return R.ok();
    }

    /**
     * 非admin 账号无法获取到超级权限
     * @param id
     * @return
     */
    @GetMapping("/admin/role")
    @RequiresPermissions("sys:admin:getrole")
    public R<AdminRoleRespVO> getUserOwnRole(Long id){
        return R.ok(adminService.getAdminRole(this.getAdmin(),id));
    }

    /**
     * 非超级账号无法修改admin账号权限
     * @param vo
     * @return
     */
    @PutMapping("/admin/role")
    @RequiresPermissions("sys:admin:setrole")
    public R setUserOwnRole(@RequestBody AdminRoleSetReqVO vo){
        Admin admin = adminService.findById(vo.getId());
        if(!this.getAdmin().getIfSuper() && !admin.getId().equals(this.getAdmin().getId())){
            return R.fail("您没有权限对此账号进行此操作");
        }
        adminService.setAdminRole(this.getAid(),vo.getId(),vo.getRids());
        return R.ok();
    }
}
