package com.tsingtec.admin.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.tsingtec.admin.constants.Constants;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


/**
 * 用户账号表
 * @author lj
 *
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "sys_admin")
@ToString(exclude = {"roles"})
@EqualsAndHashCode(exclude = {"roles"})
@Where(clause="del_status="+ Constants.NO_DELETE_FLAG)
public class Admin extends BaseEntity {

    /**名称*/
    private String name;

    /**登录帐号*/
    @Column(unique = true)
    private String loginName;

    /**密码*/
    private String password;

    /**盐*/
    private String salt;

    private Long unionId;

    private Boolean ifSuper = false;//是否超级管理员

    @JsonSerialize(using= ToStringSerializer.class)
    private Long createdId;

    /**0:有效，-1:禁止登录*/
    private Boolean status = Constants.VALID;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "sys_admin_role",
            joinColumns = @JoinColumn(name = "aid"),
            inverseJoinColumns = @JoinColumn(name = "rid"))
    @JsonIgnore
    private Set<Role> roles = new HashSet<>(0);

}
