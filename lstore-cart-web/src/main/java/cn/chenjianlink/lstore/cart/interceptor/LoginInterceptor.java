package cn.chenjianlink.lstore.cart.interceptor;

import cn.chenjianlink.lstore.common.utils.CookieUtils;
import cn.chenjianlink.lstore.common.utils.LstoreResult;
import cn.chenjianlink.lstore.pojo.TbUser;
import cn.chenjianlink.lstore.sso.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户登录处理拦截器
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        //前处理,执行之前
        //返回true放行
        String token = CookieUtils.getCookieValue(httpServletRequest, "cart");
        if (StringUtils.isBlank(token)) {
            return true;
        }
        LstoreResult result = tokenService.getUserByToken(token);
        if (result.getStatus() != 200) {
            return true;
        }
        TbUser user = (TbUser)result.getData();
        httpServletRequest.setAttribute("user", user);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
