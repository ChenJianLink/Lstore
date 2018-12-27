package cn.chenjianlink.lstore.service.impl;


import cn.chenjianlink.lstore.common.jedis.JedisClient;
import cn.chenjianlink.lstore.common.pojo.EasyUIDataGridResult;
import cn.chenjianlink.lstore.common.utils.IDUtils;
import cn.chenjianlink.lstore.common.utils.JsonUtils;
import cn.chenjianlink.lstore.common.utils.LstoreResult;
import cn.chenjianlink.lstore.mapper.TbItemDescMapper;
import cn.chenjianlink.lstore.mapper.TbItemMapper;
import cn.chenjianlink.lstore.pojo.TbItem;
import cn.chenjianlink.lstore.pojo.TbItemDesc;
import cn.chenjianlink.lstore.pojo.TbItemExample;
import cn.chenjianlink.lstore.service.ItemService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 商品相关服务层
 */
@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private TbItemMapper itemMapper;
    @Autowired
    private TbItemDescMapper itemDescMapper;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Resource
    private Destination topicDestination;
    @Autowired
    private JedisClient jedisClient;

    @Value("${REDIS_ITEM_PRE}")
    private String REDIS_ITEM_PRE;
    @Value("${ITEM_CACHE_EXPIRE}")
    private Integer ITEM_CACHE_EXPIRE;
    /**
     * 商品分页查询
     *
     * @param page
     * @param rows
     * @return
     */
    @Override
    public EasyUIDataGridResult getItemList(int page, int rows) {
        //设置分页信息
        PageHelper.startPage(page, rows);
        //查询
        TbItemExample example = new TbItemExample();
        List<TbItem> list = itemMapper.selectByExample(example);
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setRows(list);
        //获得结果
        PageInfo<TbItem> pageInfo = new PageInfo<>(list);
        //获取总记录数
        long total = pageInfo.getTotal();
        result.setTotal(total);
        return result;
    }

    /**
     * 添加商品
     *
     * @param item
     * @param desc
     * @return
     */
    @Override
    public LstoreResult addItem(TbItem item, String desc) {
        //生成商品id
        final long itemId = IDUtils.genItemId();
        //补全item属性
        item.setId(itemId);
        item.setStatus((byte) 1);
        item.setCreated(new Date());
        item.setUpdated(new Date());
        //向商品表插入数据
        itemMapper.insert(item);
        //创建一个商品描述表对应的pojo对象
        TbItemDesc itemDesc = new TbItemDesc();
        //补全属性
        itemDesc.setItemId(itemId);
        itemDesc.setItemDesc(desc);
        itemDesc.setCreated(new Date());
        itemDesc.setUpdated(new Date());
        //向商品描述表插入数据
        itemDescMapper.insert(itemDesc);
        //发送一个消息
        jmsTemplate.send(topicDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage message = session.createTextMessage(itemId + "");
                return message;
            }
        });
        //返回
        return LstoreResult.ok();
    }

    /**
     * 删除商品
     *
     * @param itemIds
     * @return
     */
    @Override
    public LstoreResult deleteItem(Long[] itemIds) {
        //封装itemId
        List<Long> list = new ArrayList<>();
        for (long id : itemIds) {
            list.add(id);
        }
        itemMapper.deleteByPrimaryKeys(list);
        itemDescMapper.deleteByPrimaryKeys(list);
        return LstoreResult.ok();
    }

    /**
     * 修改商品
     *
     * @param item
     * @param desc
     * @return
     */
    @Override
    public LstoreResult updateItem(TbItem item, String desc) {
        Long itemId = item.getId();
        TbItem olditem = itemMapper.selectByPrimaryKey(itemId);
        item.setUpdated(new Date());
        item.setCreated(olditem.getCreated());
        item.setStatus((byte) 1);
        itemMapper.updateByPrimaryKey(item);
        TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);
        //补全属性
        itemDesc.setItemDesc(desc);
        itemDesc.setUpdated(new Date());
        //向商品描述表插入数据
        itemDescMapper.updateByPrimaryKey(itemDesc);
        return LstoreResult.ok();
    }

    /**
     * 获得商品描述
     *
     * @param itemId
     * @return
     */
    @Override
    public TbItemDesc getItemDescById(long itemId) {
        //查询缓存
        try {
            String json = jedisClient.get(REDIS_ITEM_PRE + ":" + itemId + ":DESC");
            if(StringUtils.isNotBlank(json)) {
                TbItemDesc tbItemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
                return tbItemDesc;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);
        //把结果添加到缓存
        try {
            jedisClient.set(REDIS_ITEM_PRE + ":" + itemId + ":DESC", JsonUtils.objectToJson(itemDesc));
            //设置过期时间
            jedisClient.expire(REDIS_ITEM_PRE + ":" + itemId + ":DESC", ITEM_CACHE_EXPIRE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemDesc;
    }

    /**
     * 修改商品状态
     *
     * @param itemIds
     * @return
     */
    @Override
    public LstoreResult updateItemsStatusById(Long[] itemIds, byte status) {
        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        List<Long> list = new ArrayList<>();
        for (int i = 0; i < itemIds.length; i++) {
            list.add(itemIds[i]);
        }
        criteria.andIdIn(list);
        List<TbItem> tbItems = itemMapper.selectByExample(example);
        for (TbItem item : tbItems) {
            item.setUpdated(new Date());
            item.setStatus(status);
            itemMapper.updateByPrimaryKey(item);
        }
        return LstoreResult.ok();
    }

    //根据id查询商品
    @Override
    public TbItem getItemById(long itemId) {
        //查询缓存
        try {
            String json = jedisClient.get(REDIS_ITEM_PRE + ":" + itemId + ":BASE");
            if(StringUtils.isNotBlank(json)) {
                TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
                return tbItem;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //缓存中没有，查询数据库
        //根据主键查询
        //TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        //设置查询条件
        criteria.andIdEqualTo(itemId);
        //执行查询
        List<TbItem> list = itemMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            //把结果添加到缓存
            try {
                jedisClient.set(REDIS_ITEM_PRE + ":" + itemId + ":BASE", JsonUtils.objectToJson(list.get(0)));
                //设置过期时间
                jedisClient.expire(REDIS_ITEM_PRE + ":" + itemId + ":BASE", ITEM_CACHE_EXPIRE);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return list.get(0);
        }
        return null;
    }

}
