package com.tsingtec.admin.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.tsingtec.admin.constants.Constants;
import com.tsingtec.admin.vo.resp.menu.MenuNodeRespVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 角色表
 * @author lj
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "sys_role")
@ToString(exclude = {"admins", "menus"})
@EqualsAndHashCode(exclude = {"admins", "menus"})
@Where(clause="del_status="+ Constants.NO_DELETE_FLAG)
public class Role extends BaseEntity {

	private String name;

    private String description;

    private Boolean status = Constants.VALID;

    @JsonSerialize(using= ToStringSerializer.class)
    private Long createdId;

    private Boolean ifSuper = false;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "sys_role_menu",joinColumns = @JoinColumn(name = "rid"),inverseJoinColumns = @JoinColumn(name = "mid"))
    private Set<Menu> menus = new HashSet<>(0);

    @JsonIgnore
    @ManyToMany(mappedBy = "roles", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Admin> admins = new HashSet<>(0);

    @Transient
    private List<MenuNodeRespVO> chilidren;

}