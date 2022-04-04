package com.tsingtec.admin.vo.resp.role;

import com.tsingtec.admin.entity.Role;
import com.tsingtec.admin.vo.resp.menu.MenuNodeRespVO;
import lombok.Data;

import java.util.List;

/**
 * @Author lj
 * @Date 2022/4/3 21:16
 * @Version 1.0
 */
@Data
public class RoleMenuResqVO {

    private Role role;

    private List<MenuNodeRespVO> menus;

}
