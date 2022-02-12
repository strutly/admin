package com.tsingtec.admin.controller;

import com.tsingtec.admin.utils.RandomValidateCodeUtil;
import com.tsingtec.admin.vo.req.index.LoginReqVO;
import com.tsingtec.commons.support.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
@Controller
@Api(tags = "基础模块")
public class IndexController {

    @GetMapping(value = {"/","/login"})
    public String login(){
        Subject subject = SecurityUtils.getSubject();
        if(subject.isAuthenticated()){
            return "redirect:/home/index";
        };
        return "index/login";
    }

    @GetMapping("/logout")
    public String logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "index/login";
    }

    @GetMapping("/kaptcha")
    public void defaultKaptcha(HttpServletResponse response) throws Exception{
        try {
            response.setContentType("image/jpeg");//设置相应类型,告诉浏览器输出的内容为图片
            response.setHeader("Pragma", "No-cache");//设置响应头信息，告诉浏览器不要缓存此内容
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expire", 0);
            RandomValidateCodeUtil randomValidateCode = new RandomValidateCodeUtil();
            randomValidateCode.getRandcode(response);//输出验证码图片方法
        } catch (Exception e) {
            log.error("获取验证码失败>>>> ", e);
        }
    }

    @ResponseBody
    @PostMapping(value = "/login")
    @ApiOperation(value = "用户登录接口")
    public R login(@RequestBody @Valid LoginReqVO vo) {
        log.info("vo:{}",vo);
        Subject subject = SecurityUtils.getSubject();
        String code = (String)subject.getSession().getAttribute("vrifyCode");
        if(!StringUtils.hasText(code)||!code.equals(vo.getVercode())){
            return R.fail("验证码错误,请重新输入");
        }
        subject.logout();
        UsernamePasswordToken token = new UsernamePasswordToken(vo.getUsername(),vo.getPassword());
        try {
            subject.login(token);
            return R.ok();
        }catch (UnknownAccountException e) {
            return R.fail("你被禁止登录了,造吗?");
        }catch(ExcessiveAttemptsException e1) {
            return R.fail("尝试登录超过5次，账号已冻结，30分钟后再试?");
        }catch(IncorrectCredentialsException e2) {
            return R.fail("估计密码错误哦!");
        }catch(AccountException e0){
            return R.fail("账号密码错误!");
        }
    }

    @GetMapping("/403")
    public String error403(){
        return "error/403";
    }

    @GetMapping("/404")
    public String error404(){
        return "error/404";
    }

    @GetMapping("/500")
    public String error405(){
        return "error/500";
    }
}
