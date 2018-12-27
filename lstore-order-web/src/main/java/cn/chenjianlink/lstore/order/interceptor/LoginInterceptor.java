package cn.chenjianlink.lstore.order.interceptor;

import cn.chenjianlink.lstore.cart.service.CartService;
import cn.chenjianlink.lstore.common.utils.CookieUtils;
import cn.chenjianlink.lstore.common.utils.JsonUtils;
import cn.chenjianlink.lstore.common.utils.LstoreResult;
import cn.chenjianlink.lstore.pojo.TbItem;
import cn.chenjianlink.lstore.pojo.TbUser;
import cn.chenjianlink.lstore.sso.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录拦截器
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Value("${SSO_URL}")
    private String SSO_URL;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private CartService cartService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断用户是否登录
        //从cookie中取token
        String token = CookieUtils.getCookieValue(request, "token");
        //token不存在，未登录，调用sso系统登录页面，登录后跳转会请求的url
        if (StringUtils.isBlank(token)){
            response.sendRedirect(SSO_URL + "/page/login?redirect=" + request.getRequestURL());
            return false;
        }
        //token存在，则取token中的内容，取不到则已经过期，重新登录
        LstoreResult result = tokenService.getUserByToken(token);
        if (result.getStatus() != 200){
            response.sendRedirect(SSO_URL + "/page/login?redirect=" + request.getRequestURL());
            return false;
        }
        //取到则把用户登录信息写如request中
        TbUser user = (TbUser)result.getData();
        request.setAttribute("user", user);
        //判断cookie中是否有购物车数据，有则合并
        String jsonCartList = CookieUtils.getCookieValue(request, "cart", true);
        if (StringUtils.isNotBlank(jsonCartList)){
            cartService.mergeCart(user.getId(), JsonUtils.jsonToList(jsonCartList, TbItem.class));
        }
        //放行
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) throws Exception {

    }
}
