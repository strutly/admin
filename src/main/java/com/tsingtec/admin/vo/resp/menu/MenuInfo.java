package com.tsingtec.admin.vo.resp.menu;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class MenuInfo{

        private String title;//名称

        private String icon;

        @JsonProperty(value="href")
        private String url;

        private String target="_self";

        @JsonProperty(value="child")
        private List<MenuInfo> children;

        public MenuInfo(){}
    }