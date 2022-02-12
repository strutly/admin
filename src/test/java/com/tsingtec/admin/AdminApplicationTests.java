package com.tsingtec.admin;

import com.tsingtec.admin.constants.Constants;
import com.tsingtec.admin.entity.Menu;
import com.tsingtec.admin.entity.Role;
import com.tsingtec.admin.service.AdminService;
import com.tsingtec.admin.service.MenuService;
import com.tsingtec.admin.service.RoleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

//@EnableJpaAuditing
@SpringBootTest
class AdminApplicationTests {

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuService menuService;

    @Test
    void contextLoads() {


//        String[] mes = {"权限新增","权限修改","权限删除","权限详细"};
//        String[] perms = {"sys:menu:add","sys:menu:update","sys:menu:delete","sys:menu:detail"};
//        String[] urls = {"/manager/menu","/manager/menu","/manager/menu","/manager/menu/*"};
//        for (int i = 0; i < mes.length; i++) {
//            Menu menu = new Menu();
//            menu.setName(mes[i]);
//            menu.setPid(1471769595406716928L);
//            menu.setIcon("layui-icon-home");
//            menu.setOrderNum(i);
//            menu.setPerms(perms[i]);
//            menu.setUrl(urls[i]);
//            menu.setType(MenuEnum.BTN);
//            menuService.save(menu);
//        }

        Role role = new Role();
        role.setCreatedId(1471763885440634880L);
        role.setIfSuper(true);
        role.setDescription("超级管理员拥有所有权限");
        role.setName("超级管理员");
        role.setStatus(Constants.VALID);

        List<Menu> menus = menuService.findAll();

        roleService.save(role,menus.stream().map(menu -> menu.getId()).collect(Collectors.toList()));
        
        
    }

}
