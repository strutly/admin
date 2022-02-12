package com.tsingtec.admin.controller;

import com.tsingtec.admin.entity.Admin;
import org.apache.shiro.SecurityUtils;

public abstract class GenericController {

    protected Admin getAdmin() {
        Admin admin = (Admin) SecurityUtils.getSubject().getPrincipal();
        return admin;
    }

    protected Long getAid() {
        return getAdmin().getId();
    }
}
