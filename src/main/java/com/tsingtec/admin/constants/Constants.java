package com.tsingtec.admin.constants;

/**
 * @Author lj
 * @Date 2021/11/26 11:41
 * @Version 1.0
 */
public class Constants {
    /**
     * 数据库记录是否删除
     */
    public static final boolean DELETE_FLAG = false;

    public static final boolean NO_DELETE_FLAG = true;

    //0:禁用
    public static final Boolean UNVALID = false;
    //1:有效
    public static final Boolean VALID = true;

    /*
    * 父级id 为-1,为了方便前端插件使用
    * */
    public static final Long PARENT = -1L;
    /**
     * 目录
     * 菜单
     * 按钮
     */
    public static final Integer CATALOGUE = 0;
    public static final Integer MENU = 1;
    public static final Integer BTN = 2;
}
