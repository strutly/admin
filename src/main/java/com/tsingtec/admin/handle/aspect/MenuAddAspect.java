package com.tsingtec.admin.handle.aspect;

import com.tsingtec.admin.entity.Menu;
import com.tsingtec.admin.entity.Role;
import com.tsingtec.admin.service.MenuService;
import com.tsingtec.admin.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 *  新增权限时
 *  给所有超级角色 新增该权限
 */
@Slf4j
@Aspect
@Component
public class MenuAddAspect {

    @Autowired
    private MenuService menuService;

    @Autowired
    private RoleService roleService;
    /**
     * 此处的切点是注解的方式
     * 只要出现 @LogAnnotation注解都会进入
     */
    @Pointcut("@annotation(com.tsingtec.admin.handle.annotation.MenuAdd)")
    public void addPointCut() {

    }
    /**
     * 环绕增强,相当于MethodInterceptor
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("addPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        //执行方法
        Object result = point.proceed();
        log.info("修改admin 账号的权限!");
        List<Role> roles = roleService.findByIfSuper(true);
        List<Long> mids = menuService.findAll().stream()
                .map(Menu::getId).
                        collect(Collectors.toList());
        roles.forEach(role -> {
            roleService.save(role,mids);
        });
        return result;
    }
}
