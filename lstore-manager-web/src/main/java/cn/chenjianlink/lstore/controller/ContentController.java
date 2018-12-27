package cn.chenjianlink.lstore.controller;

import cn.chenjianlink.lstore.common.pojo.EasyUIDataGridResult;
import cn.chenjianlink.lstore.common.utils.LstoreResult;
import cn.chenjianlink.lstore.content.service.ContentService;
import cn.chenjianlink.lstore.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 内容管理Controller
 */
@Controller
public class ContentController {

    @Autowired
    private ContentService contentService;

    /**
     * 分页查询内容分页中的内容
     *
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/content/query/list")
    @ResponseBody
    public EasyUIDataGridResult getContentList(Integer page, Integer rows, Long categoryId) {
        EasyUIDataGridResult result = contentService.getContentList(page, rows, categoryId);
        return result;
    }

    /**
     * 添加内容分页中的内容
     * @param content
     * @return
     */
    @RequestMapping(value = "/content/save", method = RequestMethod.POST)
    @ResponseBody
    public LstoreResult addContent(TbContent content){
        //调用服务把数据插入数据库
        LstoreResult result = contentService.addContent(content);
        return result;
    }

    /**
     * 删除内容分页中的内容
     * @param ids
     * @return
     */
    @RequestMapping(value = "/content/delete", method = RequestMethod.POST)
    @ResponseBody
    public LstoreResult deleteContent(Long[] ids){
        LstoreResult result = contentService.deleteContentList(ids);
        return result;
    }

    /**
     * 修改内容分页中的内容
     * @param content
     * @return
     */
    @RequestMapping(value = "/rest/content/edit", method = RequestMethod.POST)
    @ResponseBody
    public LstoreResult editContent(TbContent content){
        LstoreResult result = contentService.editContent(content);
        return result;
    }
}
