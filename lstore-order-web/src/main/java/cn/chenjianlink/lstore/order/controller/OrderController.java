package cn.chenjianlink.lstore.order.controller;

import cn.chenjianlink.lstore.cart.service.CartService;
import cn.chenjianlink.lstore.common.utils.LstoreResult;
import cn.chenjianlink.lstore.order.pojo.OrderInfo;
import cn.chenjianlink.lstore.order.service.OrderService;
import cn.chenjianlink.lstore.pojo.TbItem;
import cn.chenjianlink.lstore.pojo.TbUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 订单管理controller
 */
@Controller
public class OrderController {

    @Autowired
    private CartService cartService;
    @Autowired
    private OrderService orderService;

    @RequestMapping("/order/order-cart")
    public String showOrderCart(HttpServletRequest request, HttpServletResponse response) {
        TbUser user = (TbUser) request.getAttribute("user");
        List<TbItem> cartList = cartService.getCartList(user.getId());
        request.setAttribute("cartList", cartList);
        return "order-cart";
    }

    @RequestMapping(value = "/order/create", method = RequestMethod.POST)
    public String createOrder(OrderInfo orderInfo, HttpServletRequest request) {
        //取用户信息
        TbUser user = (TbUser) request.getAttribute("user");
        //把用户信息添加到orderInfo
        orderInfo.setUserId(user.getId());
        orderInfo.setBuyerNick(user.getUsername());
        //生成订单
        LstoreResult result = orderService.createOrder(orderInfo);
        //生成成功，删除购物车
        if (result.getStatus() == 200){
            //清空购物车
            cartService.clearCartItem(user.getId());
        }
        //返回订单号
        request.setAttribute("orderId", result.getData());
        request.setAttribute("payment", orderInfo.getPayment());
        return "success";
    }
}
