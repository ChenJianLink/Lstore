package cn.chenjianlink.lstore.controller;

import cn.chenjianlink.lstore.common.pojo.EasyUIDataGridResult;
import cn.chenjianlink.lstore.common.utils.LstoreResult;
import cn.chenjianlink.lstore.service.ItemParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 商品规格Controller
 */
@Controller
public class ItemParamController {
    @Autowired
    private ItemParamService itemParamService;

    /**
     *返回商品规格列表
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/item/param/list")
    @ResponseBody
    public EasyUIDataGridResult getItemParamList(Integer page, Integer rows){
        EasyUIDataGridResult result = itemParamService.getItemParamList(page, rows);
        return result;
    }

    /**
     * 回显指定id商品规格
     *
     * @param itemId
     * @return
     */
    @RequestMapping("/rest/item/param/item/query/{itemId}")
    @ResponseBody
    public LstoreResult getItemById(@PathVariable Long itemId) {
        LstoreResult result = itemParamService.getItemParamById(itemId);
        return result;
    }
}
