package cn.chenjianlink.lstore.cart.controller;

import cn.chenjianlink.lstore.cart.service.CartService;
import cn.chenjianlink.lstore.common.utils.CookieUtils;
import cn.chenjianlink.lstore.common.utils.JsonUtils;
import cn.chenjianlink.lstore.common.utils.LstoreResult;
import cn.chenjianlink.lstore.pojo.TbItem;
import cn.chenjianlink.lstore.pojo.TbUser;
import cn.chenjianlink.lstore.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车处理Controller
 */
@Controller
public class CartController {

    @Autowired
    private ItemService itemService;

    @Value("${COOKIE_CART_EXPIRE}")
    private int COOKIE_CART_EXPIRE;
    @Autowired
    private CartService cartService;

    //将商品加入购物车
    @RequestMapping("/cart/add/{itemId}")
    public String addCart(@PathVariable Long itemId, @RequestParam(defaultValue = "1") Integer num, HttpServletRequest request, HttpServletResponse response) {
        //判断用户是否登录
        TbUser user = (TbUser) request.getAttribute("user");
        if (user != null){
            //保存到服务端
            cartService.addCart(user.getId(), itemId, num);
            return "cartSuccess";
        }
        //从cookie中取出购物车列表
        List<TbItem> cartList = getCartListFromCookie(request);
        //判断商品在商品列表中是否存在
        //存在则数量相加
        boolean flag = false;
        for (TbItem tbItem : cartList) {
            if (tbItem.getId() == itemId.longValue()) {
                flag = true;
                tbItem.setNum(tbItem.getNum() + num);
                break;
            }
        }
        if (!flag) {
            TbItem tbItem = itemService.getItemById(itemId);
            //设置商品数量
            tbItem.setNum(num);
            //取一张图片
            String image = tbItem.getImage();
            if (StringUtils.isNotBlank(image)) {
                tbItem.setImage(image.split(",")[0]);
            }
            //添加商品到商品列表
            cartList.add(tbItem);
        }
        //写入cookie
        CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartList), COOKIE_CART_EXPIRE, true);
        return "cartSuccess";
    }

    /**
     * 从cookie中取购物车列表处理
     *
     * @return
     */
    private List<TbItem> getCartListFromCookie(HttpServletRequest request) {
        String json = CookieUtils.getCookieValue(request, "cart", true);
        //json为空
        if (StringUtils.isBlank(json)) {
            return new ArrayList<>();
        }
        //将json转换为商品列表
        List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
        return list;
    }

    //展示购物车列表
    @RequestMapping("/cart/cart")
    public String showCatList(HttpServletRequest request,HttpServletResponse response) {
        List<TbItem> cartList = getCartListFromCookie(request);
        //判断用户是否为登录状态
        TbUser user = (TbUser) request.getAttribute("user");
        //如果是登录
        if (user != null){
            cartService.mergeCart(user.getId(), cartList);
            CookieUtils.deleteCookie(request, response, "cart");
            cartList = cartService.getCartList(user.getId());
        }
        request.setAttribute("cartList", cartList);
        return "cart";
    }

    //更新购物车商品数量
    @RequestMapping("/cart/update/num/{itemId}/{num}")
    @ResponseBody
    public LstoreResult updateCartNum(@PathVariable Long itemId, @PathVariable Integer num, HttpServletRequest request, HttpServletResponse response) {
        //判断用户是否登录
        TbUser user = (TbUser)request.getAttribute("user");
        if (user!=null){
            cartService.updateCartNum(user.getId(), itemId, num);
            return LstoreResult.ok();
        }
        //从cookie中取出购物车列表
        List<TbItem> cartList = getCartListFromCookie(request);
        //遍历商品列表找到对应的商品
        for (TbItem tbItem : cartList) {
            if (tbItem.getId().longValue() == itemId) {
                //更新数量
                tbItem.setNum(num);
                break;
            }
        }
        //把购物车列表回写cookie
        CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartList), COOKIE_CART_EXPIRE, true);
        //返回成功
        return LstoreResult.ok();
    }

    //删除购物车商品
    @RequestMapping("/cart/delete/{itemId}")
    public String deleteCartItem(@PathVariable Long itemId, HttpServletRequest request, HttpServletResponse response) {
        //判断用户是否登录
        TbUser user = (TbUser)request.getAttribute("user");
        if (user!=null){
            cartService.deleteCartItem(user.getId(), itemId);
            return "redirect:/cart/cart.html";
        }
        //从cookie中获取购物车列表
        List<TbItem> cartList = getCartListFromCookie(request);
        //遍历商品列表找到对应的商品
        for (TbItem tbItem : cartList) {
            if (tbItem.getId().longValue() == itemId) {
                //更新数量
                cartList.remove(tbItem);
                break;
            }
        }
        //写入cookie
        CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartList), COOKIE_CART_EXPIRE, true);
        //返回
        return "redirect:/cart/cart.html";
    }
}
