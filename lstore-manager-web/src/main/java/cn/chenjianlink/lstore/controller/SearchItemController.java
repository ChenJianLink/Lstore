package cn.chenjianlink.lstore.controller;

import cn.chenjianlink.lstore.common.utils.LstoreResult;
import cn.chenjianlink.lstore.search.service.SearchItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 导入商品数据到索引库
 */
@Controller
public class SearchItemController {

    @Autowired
    private SearchItemService searchItemService;

    @RequestMapping("/index/item/import")
    @ResponseBody
    public LstoreResult importItemList(){
        LstoreResult result = searchItemService.importAllItem();
        return result;
    }
}
