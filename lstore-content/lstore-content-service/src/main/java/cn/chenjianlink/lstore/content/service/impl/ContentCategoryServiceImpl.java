package cn.chenjianlink.lstore.content.service.impl;

import cn.chenjianlink.lstore.common.pojo.EasyUITreeNode;
import cn.chenjianlink.lstore.common.utils.LstoreResult;
import cn.chenjianlink.lstore.content.service.ContentCategoryService;
import cn.chenjianlink.lstore.mapper.TbContentCategoryMapper;
import cn.chenjianlink.lstore.mapper.TbContentMapper;
import cn.chenjianlink.lstore.pojo.TbContentCategory;
import cn.chenjianlink.lstore.pojo.TbContentCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 内容分类管理Service
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {
    @Autowired
    private TbContentCategoryMapper tbContentCategoryMapper;
    @Autowired
    private TbContentMapper tbContentMapper;

    /**
     * 加载内容分页中的内容
     *
     * @param parentId
     * @return
     */
    @Override
    public List<EasyUITreeNode> getContentCatList(long parentId) {
        //根据parentid查询子节点列表
        TbContentCategoryExample example = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        //设置查询条件
        criteria.andParentIdEqualTo(parentId);
        //执行查询
        List<TbContentCategory> catList = tbContentCategoryMapper.selectByExample(example);
        //转换成列表
        List<EasyUITreeNode> nodeList = new ArrayList<>();
        for (TbContentCategory tbContentCategory : catList) {
            EasyUITreeNode node = new EasyUITreeNode();
            node.setId(tbContentCategory.getId());
            node.setText(tbContentCategory.getName());
            node.setState(tbContentCategory.getIsParent() ? "closed" : "open");
            //添加到列表
            nodeList.add(node);
        }
        return nodeList;
    }

    /**
     * 添加内容节点
     *
     * @param parentId
     * @param name
     * @return
     */
    @Override
    public LstoreResult addContentCategory(long parentId, String name) {
        //创建对应的pojo
        TbContentCategory contentCategory = new TbContentCategory();
        //设置pojo属性
        contentCategory.setParentId(parentId);
        contentCategory.setName(name);
        contentCategory.setStatus(1);//1 正常,2 删除
        contentCategory.setSortOrder(1);//默认排序为1
        contentCategory.setCreated(new Date());
        contentCategory.setUpdated(new Date());
        contentCategory.setIsParent(false);//新添加的加点一定是页子节点
        //插入到数据库
        tbContentCategoryMapper.insert(contentCategory);
        //判断父节点的isparent属性
        TbContentCategory parent = tbContentCategoryMapper.selectByPrimaryKey(parentId);
        if (!parent.getIsParent()) {
            parent.setIsParent(true);
            //更新
            tbContentCategoryMapper.updateByPrimaryKey(parent);
        }
        //返回结果
        return LstoreResult.ok(contentCategory);
    }

    /**
     * 删除节点内容
     *
     * @param cid
     * @return
     */
    @Override
    public LstoreResult deleteContentCategory(long cid) {
        TbContentCategory contentCategory = tbContentCategoryMapper.selectByPrimaryKey(cid);
        //判断是否有子分类
        if (contentCategory.getIsParent()) {
            int status = 500;
            String msg = "该分类下还有其他分类，请先删除该分类的子分类";
            return LstoreResult.build(status, msg);
        }
        //删除节点
        tbContentCategoryMapper.deleteByPrimaryKey(cid);
        //查询删除节点的父节点下的其他子节点
        long parentId = contentCategory.getParentId();
        TbContentCategoryExample example = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        int i = tbContentCategoryMapper.countByExample(example);
        //判断父节点是否含有子节点,若没有，则设置为普通节点
        TbContentCategory parent = tbContentCategoryMapper.selectByPrimaryKey(parentId);
        if (i == 0) {
            parent.setIsParent(false);
            //更新
            tbContentCategoryMapper.updateByPrimaryKey(parent);
        }
        return LstoreResult.ok();
    }

    /**
     * 修改节点名称
     * @param cid
     * @param name
     * @return
     */
    @Override
    public LstoreResult editContentCategory(long cid, String name) {
        TbContentCategory contentCategory = tbContentCategoryMapper.selectByPrimaryKey(cid);
        contentCategory.setName(name);
        contentCategory.setUpdated(new Date());
        tbContentCategoryMapper.updateByPrimaryKey(contentCategory);
        return LstoreResult.ok();
    }

}
