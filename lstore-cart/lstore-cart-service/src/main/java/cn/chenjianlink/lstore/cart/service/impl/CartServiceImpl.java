package cn.chenjianlink.lstore.cart.service.impl;

import cn.chenjianlink.lstore.cart.service.CartService;
import cn.chenjianlink.lstore.common.jedis.JedisClient;
import cn.chenjianlink.lstore.common.utils.JsonUtils;
import cn.chenjianlink.lstore.common.utils.LstoreResult;
import cn.chenjianlink.lstore.mapper.TbItemMapper;
import cn.chenjianlink.lstore.pojo.TbItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 购物车处理服务
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private JedisClient jedisClient;
    @Value("${REDIS_CART_PRE}")
    private String REDIS_CART_PRE;
    @Autowired
    private TbItemMapper itemMapper;

    @Override
    public LstoreResult addCart(long userId, long itemId, int num) {
        //向redis添加购物车
        //数据类型是hash key是用户名,value是商品id
        //判断商品是否存在
        Boolean hexists = jedisClient.hexists(REDIS_CART_PRE + ":" + userId, itemId + "");
        //存在则添加数量
        if (hexists) {
            String json = jedisClient.hget(REDIS_CART_PRE + ":" + userId, itemId + "");
            TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
            item.setNum(num);
            //回写redis
            jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(item));
            return LstoreResult.ok();
        }
        //不存在则查询商品
        TbItem item = itemMapper.selectByPrimaryKey(itemId);
        //设置购物车数量
        item.setNum(num);
        String image = item.getImage();
        if (StringUtils.isNotBlank(image)) {
            item.setImage(image.split(",")[0]);
        }
        jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(item));
        return LstoreResult.ok();
    }

    @Override
    public LstoreResult mergeCart(long userId, List<TbItem> itemList) {
        //遍历商品列表
        for (TbItem tbItem : itemList) {
            addCart(userId, tbItem.getId(), tbItem.getNum());
        }
        //把列表添加到购物车
        return LstoreResult.ok();
    }

    @Override
    public List<TbItem> getCartList(long userId) {
        //根据用户id查询购物车列表
        List<String> jsonList = jedisClient.hvals(REDIS_CART_PRE + ":" + userId);
        List<TbItem> itemList = new ArrayList<>();
        for (String string : jsonList) {
            TbItem item = JsonUtils.jsonToPojo(string, TbItem.class);
            itemList.add(item);
        }
        return itemList;
    }

    @Override
    public LstoreResult updateCartNum(long userId, long itemId, int num) {
        String json = jedisClient.hget(REDIS_CART_PRE + ":" + userId, itemId + "");
        TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
        tbItem.setNum(num);
        //写入redis
        jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(tbItem));
        return LstoreResult.ok();
    }

    @Override
    public LstoreResult deleteCartItem(long userId, long itemId) {
        //删除购物车商品
        jedisClient.hdel(REDIS_CART_PRE + ":" + userId, itemId + "");
        return LstoreResult.ok();
    }

    @Override
    public LstoreResult clearCartItem(long userId) {
        //删除购物车信息
        jedisClient.del(REDIS_CART_PRE + ":" + userId);
        return LstoreResult.ok();
    }
}
