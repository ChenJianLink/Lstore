package cn.chenjianlink.lstore.item.controller;

import cn.chenjianlink.lstore.item.pojo.Item;
import cn.chenjianlink.lstore.pojo.TbItem;
import cn.chenjianlink.lstore.pojo.TbItemDesc;
import cn.chenjianlink.lstore.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 商品详细页面展示
 */
@Controller
public class ItemController {

    @Autowired
    private ItemService itemService;

    @RequestMapping("/item/{itemId}")
    public String showItemInfo(@PathVariable Long itemId, Model model) {
        //调用服务查询商品
        TbItem tbItem = itemService.getItemById(itemId);
        Item item = new Item(tbItem);
        TbItemDesc itemDesc = itemService.getItemDescById(itemId);
        model.addAttribute("item", item);
        model.addAttribute("itemDesc",itemDesc);
        //返回逻辑视图
        return "item";
    }

}
