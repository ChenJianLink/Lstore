package cn.chenjianlink.lstore.portal.controller;

import cn.chenjianlink.lstore.content.service.ContentService;
import cn.chenjianlink.lstore.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


/**
 * 首页展示Controller
 */
@Controller
public class IndexController {

    @Autowired
    private ContentService contentService;

    @Value("${CONTENT_LUNBO_ID}")
    private Long CONTENT_LUNBO_ID;

    @RequestMapping("/index")
    public String showIndex(Model model){
        //查询内容列表
        List<TbContent> ad1List = contentService.getContentListById(CONTENT_LUNBO_ID);
        model.addAttribute("ad1List", ad1List);
        return "index";
    }
}
