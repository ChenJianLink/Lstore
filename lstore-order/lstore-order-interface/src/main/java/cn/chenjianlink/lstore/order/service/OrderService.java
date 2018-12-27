package cn.chenjianlink.lstore.order.service;

import cn.chenjianlink.lstore.common.utils.LstoreResult;
import cn.chenjianlink.lstore.order.pojo.OrderInfo;

public interface OrderService {
    LstoreResult createOrder(OrderInfo orderInfo);
}
