package cn.chenjianlink.lstore.content.service;

import cn.chenjianlink.lstore.common.pojo.EasyUITreeNode;
import cn.chenjianlink.lstore.common.utils.LstoreResult;

import java.util.List;

public interface ContentCategoryService {
    //加载内容分页中的内容
    List<EasyUITreeNode> getContentCatList(long parentId);

    //添加内容节点
    LstoreResult addContentCategory(long parentId, String name);

    //删除节点内容
    LstoreResult deleteContentCategory(long cid);

    //修改节点名称
    LstoreResult editContentCategory(long cid, String name);

}
