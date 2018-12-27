package cn.chenjianlink.lstore.sso.controller;

import cn.chenjianlink.lstore.common.utils.CookieUtils;
import cn.chenjianlink.lstore.common.utils.LstoreResult;
import cn.chenjianlink.lstore.sso.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 用户登录处理
 */
@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;
    @Value("{TOKEN_KEY}")
    private String TOKEN_KEY;

    @RequestMapping("/page/login")
    public String showLogin(String redirect, Model model) {
        model.addAttribute("redirect", redirect);
        return "login";
    }

    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    @ResponseBody
    public LstoreResult Login(String username, String password, HttpServletRequest request, HttpServletResponse response) {
        LstoreResult lstoreResult = loginService.userLogin(username, password);
        //判断登录是否成功
        if (lstoreResult.getStatus() == 200) {
            String token = lstoreResult.getData().toString();
            //登录成功将信息写入cookie
            CookieUtils.setCookie(request, response, TOKEN_KEY, token);
        }
        return lstoreResult;
    }

}
