package cn.chenjianlink.lstore.sso.controller;

import cn.chenjianlink.lstore.common.utils.LstoreResult;
import cn.chenjianlink.lstore.pojo.TbUser;
import cn.chenjianlink.lstore.sso.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 注册功能Controller
 */
@Controller
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    @RequestMapping("/page/register")
    public String showRegister() {
        return "register";
    }

    //校验信息
    @RequestMapping("/user/check/{param}/{type}")
    @ResponseBody
    public LstoreResult checkData(@PathVariable String param, @PathVariable Integer type) {
        LstoreResult result = registerService.checkData(param, type);
        return result;
    }
    //注册
    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
    @ResponseBody
    public LstoreResult register(TbUser user) {
        LstoreResult result = registerService.register(user);
        return result;
    }
}
