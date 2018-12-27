package cn.chenjianlink.lstore.sso.controller;

import cn.chenjianlink.lstore.common.utils.JsonUtils;
import cn.chenjianlink.lstore.common.utils.LstoreResult;
import cn.chenjianlink.lstore.sso.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 根据Token查询用户信息Controller
 */
@Controller
public class TokenController {
    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/user/token/{token}",produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getUserByToken(@PathVariable String token, String callback) {
        LstoreResult result = tokenService.getUserByToken(token);
        //响应结果之前,判断是否为jsonp请求
        if (StringUtils.isNotBlank(callback)){
           return callback + "(" + JsonUtils.objectToJson(result) + ");";
        }
        return JsonUtils.objectToJson(result);
    }
}
