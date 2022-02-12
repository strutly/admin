package com.tsingtec.admin.vo.resp.menu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author lj
 * @Date 2021/12/30 17:17
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HomeMenuRespVO {
    private HomeInfo homeInfo = new HomeInfo();
    private LogoInfo logoInfo = new LogoInfo();
    private List<MenuInfo> menuInfo;

    @Data
    public class LogoInfo{
        private String title="后台管理系统";
        private String image = "/favicon.ico";
        private String href ="/home/index";
    }

    @Data
    public class HomeInfo{
        private String title = "首页";
        private String href = "/home/about";
    }
}
