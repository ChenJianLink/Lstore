package cn.chenjianlink.lstore.cart.service;

import cn.chenjianlink.lstore.common.utils.LstoreResult;
import cn.chenjianlink.lstore.pojo.TbItem;

import java.util.List;

public interface CartService {
    LstoreResult addCart(long userId, long itemId, int num);
    LstoreResult mergeCart(long userId, List<TbItem> itemList);
    List<TbItem> getCartList(long userId);
    LstoreResult updateCartNum(long userId,long itemId, int num);
    LstoreResult deleteCartItem(long userId,long itemId);
    LstoreResult clearCartItem(long userId);
}
