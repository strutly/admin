package com.tsingtec.admin.config.start;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.tsingtec.admin.entity.Admin;
import com.tsingtec.admin.entity.Menu;
import com.tsingtec.admin.entity.Role;
import com.tsingtec.admin.repository.AdminRepository;
import com.tsingtec.admin.repository.MenuRepository;
import com.tsingtec.admin.repository.RoleRepository;
import com.tsingtec.commons.mapper.BeanMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * 项目启动时初始化数据库
 */
@Slf4j
@Component
public class StartupListener implements ApplicationRunner{

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private RoleRepository roleRepository;


    private static String sqlStr = "{\"admin\":{\"id\":\"1471763885440634880\",\"delStatus\":\"1\",\"createdId\":\"-1\",\"ifSuper\":\"1\",\"loginName\":\"tsingtec\",\"name\":\"超级管理员\",\"password\":\"ef0720d35cb8ab22725723397b7b9c4de074d3365a0da77a69a7b533a1702d13\",\"salt\":\"a6946509\",\"status\":\"1\",\"union_id\":\"\"},\"menu\":[{\"id\":\"1471768436394692608\",\"delStatus\":\"1\",\"icon\":\"layui-icon-set\",\"title\":\"系统管理\",\"orderNum\":\"0\",\"perms\":\"\",\"pid\":\"-1\",\"status\":\"1\",\"type\":\"0\",\"url\":\"\"},{\"id\":\"1471768437304856576\",\"delStatus\":\"1\",\"icon\":\"layui-icon-template-1\",\"title\":\"组织管理\",\"orderNum\":\"1\",\"perms\":\"\",\"pid\":\"-1\",\"status\":\"1\",\"type\":\"0\",\"url\":\"\"},{\"id\":\"1471768437392936960\",\"delStatus\":\"1\",\"icon\":\"layui-icon-util\",\"title\":\"应用管理\",\"orderNum\":\"0\",\"perms\":\"\",\"pid\":\"-1\",\"status\":\"1\",\"type\":\"0\",\"url\":\"\"},{\"id\":\"1471769594425249792\",\"delStatus\":\"1\",\"icon\":\"layui-icon-home\",\"title\":\"账号管理\",\"orderNum\":\"0\",\"perms\":\"sys:admin:page\",\"pid\":\"1471768437304856576\",\"status\":\"1\",\"type\":\"1\",\"url\":\"/home/sys/admin/page\"},{\"id\":\"1471769595339608064\",\"delStatus\":\"1\",\"icon\":\"layui-icon-home\",\"title\":\"角色管理\",\"orderNum\":\"1\",\"perms\":\"sys:role:page\",\"pid\":\"1471768437304856576\",\"status\":\"1\",\"type\":\"1\",\"url\":\"/home/sys/role/page\"},{\"id\":\"1471769595406716928\",\"delStatus\":\"1\",\"icon\":\"layui-icon-home\",\"title\":\"权限管理\",\"orderNum\":\"2\",\"perms\":\"sys:menu:page\",\"pid\":\"1471768437304856576\",\"status\":\"1\",\"type\":\"1\",\"url\":\"/home/sys/menu/page\"},{\"id\":\"1471770864569552896\",\"delStatus\":\"1\",\"icon\":\"layui-icon-home\",\"title\":\"新增账号\",\"orderNum\":\"0\",\"perms\":\"sys:admin:add\",\"pid\":\"1471769594425249792\",\"status\":\"1\",\"type\":\"2\",\"url\":\"/manager/admin\"},{\"id\":\"1471770865517465600\",\"delStatus\":\"1\",\"icon\":\"layui-icon-home\",\"title\":\"修改账号\",\"orderNum\":\"1\",\"perms\":\"sys:admin:update\",\"pid\":\"1471769594425249792\",\"status\":\"1\",\"type\":\"2\",\"url\":\"/manager/admin\"},{\"id\":\"1471770865639100416\",\"delStatus\":\"1\",\"icon\":\"layui-icon-home\",\"title\":\"删除账号\",\"orderNum\":\"2\",\"perms\":\"sys:admin:delete\",\"pid\":\"1471769594425249792\",\"status\":\"1\",\"type\":\"2\",\"url\":\"/manager/admin\"},{\"id\":\"1471770865769123840\",\"delStatus\":\"1\",\"icon\":\"layui-icon-home\",\"title\":\"账号详细\",\"orderNum\":\"3\",\"perms\":\"sys:admin:detail\",\"pid\":\"1471769594425249792\",\"status\":\"1\",\"type\":\"2\",\"url\":\"/manager/admin/*\"},{\"id\":\"1471771942040113152\",\"delStatus\":\"1\",\"icon\":\"layui-icon-home\",\"title\":\"密码修改\",\"orderNum\":\"0\",\"perms\":\"sys:admin:psd\",\"pid\":\"1471769594425249792\",\"status\":\"1\",\"type\":\"2\",\"url\":\"/manager/admin/psd\"},{\"id\":\"1471771943017385984\",\"delStatus\":\"1\",\"icon\":\"layui-icon-home\",\"title\":\"获取账号角色\",\"orderNum\":\"1\",\"perms\":\"sys:admin:getrole\",\"pid\":\"1471769594425249792\",\"status\":\"1\",\"type\":\"2\",\"url\":\"/manager/admin/role\"},{\"id\":\"1471771943147409408\",\"delStatus\":\"1\",\"icon\":\"layui-icon-home\",\"title\":\"修改账号角色\",\"orderNum\":\"2\",\"perms\":\"sys:admin:setrole\",\"pid\":\"1471769594425249792\",\"status\":\"1\",\"type\":\"2\",\"url\":\"/manager/admin/role\"},{\"id\":\"1471773972687228928\",\"delStatus\":\"1\",\"icon\":\"layui-icon-home\",\"title\":\"角色新增\",\"orderNum\":\"0\",\"perms\":\"sys:role:add\",\"pid\":\"1471769595339608064\",\"status\":\"1\",\"type\":\"2\",\"url\":\"/manager/role\"},{\"id\":\"1471773973744193536\",\"delStatus\":\"1\",\"icon\":\"layui-icon-home\",\"title\":\"角色修改\",\"orderNum\":\"1\",\"perms\":\"sys:role:update\",\"pid\":\"1471769595339608064\",\"status\":\"1\",\"type\":\"2\",\"url\":\"/manager/role\"},{\"id\":\"1471773973811302400\",\"delStatus\":\"1\",\"icon\":\"layui-icon-home\",\"title\":\"角色删除\",\"orderNum\":\"2\",\"perms\":\"sys:role:delete\",\"pid\":\"1471769595339608064\",\"status\":\"1\",\"type\":\"2\",\"url\":\"/manager/role\"},{\"id\":\"1471773973916160000\",\"delStatus\":\"1\",\"icon\":\"layui-icon-home\",\"title\":\"角色详细\",\"orderNum\":\"3\",\"perms\":\"sys:role:detail\",\"pid\":\"1471769595339608064\",\"status\":\"1\",\"type\":\"2\",\"url\":\"/manager/role/*\"},{\"id\":\"1471774114899300352\",\"delStatus\":\"1\",\"icon\":\"layui-icon-home\",\"title\":\"权限新增\",\"orderNum\":\"0\",\"perms\":\"sys:menu:add\",\"pid\":\"1471769595406716928\",\"status\":\"1\",\"type\":\"2\",\"url\":\"/manager/menu\"},{\"id\":\"1471774115901739008\",\"delStatus\":\"1\",\"icon\":\"layui-icon-home\",\"title\":\"权限修改\",\"orderNum\":\"1\",\"perms\":\"sys:menu:update\",\"pid\":\"1471769595406716928\",\"status\":\"1\",\"type\":\"2\",\"url\":\"/manager/menu\"},{\"id\":\"1471774116136620032\",\"delStatus\":\"1\",\"icon\":\"layui-icon-home\",\"title\":\"权限删除\",\"orderNum\":\"2\",\"perms\":\"sys:menu:delete\",\"pid\":\"1471769595406716928\",\"status\":\"1\",\"type\":\"2\",\"url\":\"/manager/menu\"},{\"id\":\"1471774116258254848\",\"delStatus\":\"1\",\"icon\":\"layui-icon-home\",\"title\":\"权限详细\",\"orderNum\":\"3\",\"perms\":\"sys:menu:detail\",\"pid\":\"1471769595406716928\",\"status\":\"1\",\"type\":\"2\",\"url\":\"/manager/menu/*\"}],\"role\":{\"id\":\"1478258504093405184\",\"delStatus\":\"1\",\"createdId\":\"1471763885440634880\",\"description\":\"超级管理员拥有所有权限\",\"ifSuper\":\"1\",\"name\":\"超级管理员\",\"status\":\"1\"}}";

    /**
     * 初始化系统账号,在未存在admin时,直接生成
     * @param args
     */
    @Override
    public void run(ApplicationArguments args){
        log.info("检测是否存在超级管理员");
        Admin admin =adminRepository.findByLoginName("tsingtec");
        if(Objects.isNull(admin)){
            JSONObject object = JSONUtil.parseObj(sqlStr);
            JSONArray menuArr = object.getJSONArray("menu");
            menuArr.forEach(o -> {
                Menu menu = BeanMapper.map(o,Menu.class);
                System.out.println();
                menuRepository.save(menu);
            });

            Object roleObj = object.get("role");
            Role role = BeanMapper.map(roleObj,Role.class);
            role.setMenus(new HashSet<>(menuRepository.findAll()));
            roleRepository.save(role);

            Object adminObj = object.get("admin");
            Admin saveAdmin = BeanMapper.map(adminObj,Admin.class);
            Set<Role> roles = new HashSet<>();
            roles.add(role);
            saveAdmin.setRoles(roles);
            adminRepository.save(saveAdmin);
        }
    }
}