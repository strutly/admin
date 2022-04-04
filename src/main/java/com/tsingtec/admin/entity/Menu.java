package com.tsingtec.admin.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.google.common.collect.Lists;
import com.tsingtec.admin.constants.Constants;
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

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "sys_menu")
@ToString(exclude = {"roles"})
@EqualsAndHashCode(exclude = {"roles"})
@Where(clause = "del_status=" + Constants.NO_DELETE_FLAG)
public class Menu extends BaseEntity {

    private String title;//名称

    private String icon = "layui-icon-home";

    private String perms;//授权标识

    private String url;//链接

    @JsonSerialize(using = ToStringSerializer.class)
    private Long pid = Constants.PARENT;//

    @Transient
    private String pidName;

    private Integer orderNum = 0;//排序

    private Integer type = Constants.CATALOGUE;//类型

    @ManyToMany(mappedBy = "menus", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Role> roles = new HashSet<>(0);

    @Transient
    private List<Menu> children = Lists.newArrayList();

    @Transient
    private Boolean checked = false;

    @Transient
    private Boolean spread = false;

}