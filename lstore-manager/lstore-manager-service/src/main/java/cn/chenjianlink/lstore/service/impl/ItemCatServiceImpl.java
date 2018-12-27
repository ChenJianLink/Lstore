package cn.chenjianlink.lstore.service.impl;

import cn.chenjianlink.lstore.common.pojo.EasyUITreeNode;
import cn.chenjianlink.lstore.mapper.TbItemCatMapper;
import cn.chenjianlink.lstore.mapper.TbItemMapper;
import cn.chenjianlink.lstore.pojo.TbItemCat;
import cn.chenjianlink.lstore.pojo.TbItemCatExample;
import cn.chenjianlink.lstore.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品种类加载实现
 */
@Service
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private TbItemCatMapper itemCatMapper;
    @Autowired
    private TbItemMapper itemMapper;

    @Override
    public List<EasyUITreeNode> getItemCatlist(long parentId) {
        //根据parentId查询子节点列表
        TbItemCatExample example = new TbItemCatExample();
        TbItemCatExample.Criteria criteria = example.createCriteria();
        //设置查询条件
        criteria.andParentIdEqualTo(parentId);
        //查询
        List<TbItemCat> list = itemCatMapper.selectByExample(example);
        List<EasyUITreeNode> result = new ArrayList<>();
        for(TbItemCat tbItemCat : list){
            EasyUITreeNode node = new EasyUITreeNode();
            node.setId(tbItemCat.getId());
            node.setState(tbItemCat.getIsParent()?"closed":"open");
            node.setText(tbItemCat.getName());
            result.add(node);
        }
        return result;
    }


}
