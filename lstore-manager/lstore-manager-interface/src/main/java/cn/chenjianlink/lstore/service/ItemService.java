package cn.chenjianlink.lstore.service;

import cn.chenjianlink.lstore.common.pojo.EasyUIDataGridResult;
import cn.chenjianlink.lstore.common.utils.LstoreResult;
import cn.chenjianlink.lstore.pojo.TbItem;
import cn.chenjianlink.lstore.pojo.TbItemDesc;


/**
 * 商品服务接口
 */
public interface ItemService {

    //商品分页查询
    EasyUIDataGridResult getItemList(int page, int rows);

    //添加商品
    LstoreResult addItem(TbItem item, String desc);

    //删除商品
    LstoreResult deleteItem(Long[] itemIds);

    //修改商品
    LstoreResult updateItem(TbItem item, String desc);

    //根据id查询商品描述
    TbItemDesc getItemDescById(long itemId);

    //修改商品状态
    LstoreResult updateItemsStatusById(Long[] itemIds, byte status);

    //根据id查询商品
    TbItem getItemById(long itemId);

}
