package cn.chenjianlink.lstore.service;

import cn.chenjianlink.lstore.common.pojo.EasyUITreeNode;

import java.util.List;

/**
 * 商品种类加载接口
 */
public interface ItemCatService {
    //显示所有商品规格
    List<EasyUITreeNode> getItemCatlist(long parentId);

}
