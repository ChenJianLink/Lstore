package cn.chenjianlink.lstore.controller;

import cn.chenjianlink.lstore.common.pojo.EasyUITreeNode;
import cn.chenjianlink.lstore.common.utils.LstoreResult;
import cn.chenjianlink.lstore.service.ItemCatService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 商品规格管理Controller
 */
@Controller
public class ItemCatController {

    @Autowired
    private ItemCatService itemCatService;

    /**
     * 显示商品规格
     *
     * @param parentId
     * @return
     */
    @RequestMapping("/item/cat/list")
    @ResponseBody
    public List<EasyUITreeNode> getItemCatList(@RequestParam(name = "id", defaultValue = "0") Long parentId) {
        //查询节点列表
        List<EasyUITreeNode> list = itemCatService.getItemCatlist(parentId);
        return list;
    }


}
