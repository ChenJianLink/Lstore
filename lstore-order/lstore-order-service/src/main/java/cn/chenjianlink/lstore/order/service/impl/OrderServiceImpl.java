package cn.chenjianlink.lstore.order.service.impl;

import cn.chenjianlink.lstore.common.jedis.JedisClient;
import cn.chenjianlink.lstore.common.utils.LstoreResult;
import cn.chenjianlink.lstore.mapper.TbOrderItemMapper;
import cn.chenjianlink.lstore.mapper.TbOrderMapper;
import cn.chenjianlink.lstore.mapper.TbOrderShippingMapper;
import cn.chenjianlink.lstore.order.pojo.OrderInfo;
import cn.chenjianlink.lstore.order.service.OrderService;
import cn.chenjianlink.lstore.pojo.TbOrderItem;
import cn.chenjianlink.lstore.pojo.TbOrderShipping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 订单处理服务
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private TbOrderMapper orderMapper;
    @Autowired
    private TbOrderItemMapper orderItemMapper;
    @Autowired
    private TbOrderShippingMapper orderShippingMapper;
    @Autowired
    private JedisClient jedisClient;
    @Value("${ORDER_ID_GEN_KEY}")
    private String ORDER_ID_GEN_KEY;
    @Value("${ORDER_ID_START}")
    private String ORDER_ID_START;
    @Value("${ORDER_DETAIL_ID_GEN_KEY}")
    private String ORDER_DETAIL_ID_GEN_KEY;

    @Override
    public LstoreResult createOrder(OrderInfo orderInfo) {
        //生成订单号
        if (jedisClient.exists(ORDER_ID_GEN_KEY)) {
            jedisClient.set(ORDER_ID_GEN_KEY, ORDER_ID_START);
        }
        String orderId = jedisClient.incr(ORDER_ID_GEN_KEY).toString();
        //补全tborder属性
        orderInfo.setOrderId(orderId);
        orderInfo.setStatus(1); //1.未付款 2.已付款 3.未发货 4.已发货 5.交易成功 6.交易关闭
        orderInfo.setCreateTime(new Date());
        orderInfo.setUpdateTime(new Date());
        //插入订单表
        orderMapper.insert(orderInfo);
        //向订单明细表插入数据
        List<TbOrderItem> orderItems = orderInfo.getOrderItems();
        for (TbOrderItem orderItem : orderItems) {
            String orderDetailId = jedisClient.incr(ORDER_DETAIL_ID_GEN_KEY).toString();
            //补全属性
            orderItem.setId(orderDetailId);
            orderItem.setOrderId(orderId);
            //向明细表插入数据
            orderItemMapper.insert(orderItem);
        }
        //向订单物流表插入数据
        TbOrderShipping orderShipping = orderInfo.getOrderShipping();
        orderShipping.setOrderId(orderId);
        orderShipping.setCreated(new Date());
        orderShipping.setUpdated(new Date());
        orderShippingMapper.insert(orderShipping);
        return LstoreResult.ok(orderId);
    }
}
