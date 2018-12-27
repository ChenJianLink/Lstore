package cn.chenjianlink.lstore.controller;

import cn.chenjianlink.lstore.common.pojo.EasyUIDataGridResult;
import cn.chenjianlink.lstore.common.utils.LstoreResult;
import cn.chenjianlink.lstore.pojo.TbItem;
import cn.chenjianlink.lstore.pojo.TbItemDesc;
import cn.chenjianlink.lstore.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


/**
 * 商品管理
 */
@Controller
public class ItemController {
    @Autowired
    private ItemService itemService;

    /**
     * 分页查询商品
     *
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/item/list")
    @ResponseBody
    public EasyUIDataGridResult getItemList(Integer page, Integer rows) {
        EasyUIDataGridResult result = itemService.getItemList(page, rows);
        return result;
    }

    /**
     * 添加商品
     */
    @RequestMapping(value = "/item/save", method = RequestMethod.POST)
    @ResponseBody
    public LstoreResult addItem(TbItem item, String desc) {
        LstoreResult result = itemService.addItem(item, desc);
        return result;
    }

    /**
     * 删除商品
     *
     * @param itemId
     */
    @RequestMapping(value = "/rest/item/delete", method = RequestMethod.POST)
    @ResponseBody
    public LstoreResult deleteItemList(@RequestParam(value = "ids") Long[] itemId) {
        LstoreResult result = itemService.deleteItem(itemId);
        return result;
    }


    /**
     * 回显商品描述
     *
     * @param itemId
     * @return
     */
    @RequestMapping("/rest/item/query/item/desc/{itemId}")
    @ResponseBody
    public LstoreResult getItemDescById(@PathVariable Long itemId) {
        TbItemDesc result = itemService.getItemDescById(itemId);
        return LstoreResult.ok(result);
    }

    /**
     * 修改商品
     */
    @RequestMapping(value = "/rest/item/update", method = RequestMethod.POST)
    @ResponseBody
    public LstoreResult updateItem(TbItem item, String desc) {
        LstoreResult result = itemService.updateItem(item, desc);
        return result;
    }

    /**
     * 上架商品
     */
    @RequestMapping(value = "/rest/item/reshelf", method = RequestMethod.POST)
    @ResponseBody
    public LstoreResult reshelfItem(Long[] ids) {
        LstoreResult result = itemService.updateItemsStatusById(ids, (byte) 2);
        return result;
    }

    /**
     * 下架商品
     */
    @RequestMapping(value = "/rest/item/instock", method = RequestMethod.POST)
    @ResponseBody
    public LstoreResult instockItem(Long[] ids) {
        LstoreResult result = itemService.updateItemsStatusById(ids, (byte) 3);
        return result;
    }

}
