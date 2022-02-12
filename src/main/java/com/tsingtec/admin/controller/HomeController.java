package com.tsingtec.admin.controller;


import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Api(tags = "视图")
@RequestMapping("/home")
public class HomeController {

    @GetMapping("/{str}")
    public String home(@PathVariable("str") String str) {
        return "index/" + str;
    }

    @GetMapping("/{str}/{str1}/{str2}")
    public String str(@PathVariable("str") String str, @PathVariable("str1") String str1, @PathVariable("str2") String str2) {
        return "page/" + str + "/" + str1 + "/" + str2;
    }
}
