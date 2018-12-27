package cn.chenjianlink.lstore.controller;

import cn.chenjianlink.lstore.common.pojo.EasyUITreeNode;
import cn.chenjianlink.lstore.common.utils.LstoreResult;
import cn.chenjianlink.lstore.content.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 内容分类管理Controller
 */
@Controller
public class ContentCatController {

    @Autowired
    private ContentCategoryService contentCategoryService;

    /**
     * 返回分类内容
     *
     * @param parentId
     * @return
     */
    @RequestMapping("/content/category/list")
    @ResponseBody
    public List<EasyUITreeNode> getContentCatList(@RequestParam(name = "id", defaultValue = "0") Long parentId) {
        List<EasyUITreeNode> list = contentCategoryService.getContentCatList(parentId);
        return list;
    }

    /**
     * 添加分类节点
     */
    @RequestMapping(value = "/content/category/create", method = RequestMethod.POST)
    @ResponseBody
    public LstoreResult creatContentCateory(Long parentId, String name) {
        //调用服务添加节点
        LstoreResult lstoreResult = contentCategoryService.addContentCategory(parentId, name);
        return lstoreResult;
    }

    /**
     * 删除节点
     */
    @RequestMapping(value = "/content/category/delete/", method = RequestMethod.POST)
    @ResponseBody
    public LstoreResult deleteContentCateory(@RequestParam(value = "id") Long cid) {
        LstoreResult lstoreResult = contentCategoryService.deleteContentCategory(cid);
        return lstoreResult;
    }

    /**
     * 修改节点
     */
    @RequestMapping(value = "/content/category/update", method = RequestMethod.POST)
    @ResponseBody
    public LstoreResult editContentCateory(@RequestParam(value = "id") Long cid, String name) {
        LstoreResult lstoreResult = contentCategoryService.editContentCategory(cid, name);
        return lstoreResult;
    }

}
