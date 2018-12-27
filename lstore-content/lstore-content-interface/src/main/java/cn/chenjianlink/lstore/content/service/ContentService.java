package cn.chenjianlink.lstore.content.service;

import cn.chenjianlink.lstore.common.pojo.EasyUIDataGridResult;
import cn.chenjianlink.lstore.common.utils.LstoreResult;
import cn.chenjianlink.lstore.pojo.TbContent;

import java.util.List;

public interface ContentService {
    //添加内容
    LstoreResult addContent(TbContent content);

    //获得内容管理列表
    EasyUIDataGridResult getContentList(int page, int rows, long categoryId);

    //根据id查找内容列表
    List<TbContent> getContentListById(long cid);

    //根据id删除内容列表
    LstoreResult deleteContentList(Long[] ids);

    //根据id修改内容列表
    LstoreResult editContent(TbContent content);
}
