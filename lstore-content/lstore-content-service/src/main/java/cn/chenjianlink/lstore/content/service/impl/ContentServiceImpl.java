package cn.chenjianlink.lstore.content.service.impl;

import cn.chenjianlink.lstore.common.jedis.JedisClient;
import cn.chenjianlink.lstore.common.pojo.EasyUIDataGridResult;
import cn.chenjianlink.lstore.common.utils.JsonUtils;
import cn.chenjianlink.lstore.common.utils.LstoreResult;
import cn.chenjianlink.lstore.content.service.ContentService;
import cn.chenjianlink.lstore.mapper.TbContentMapper;
import cn.chenjianlink.lstore.pojo.TbContent;
import cn.chenjianlink.lstore.pojo.TbContentExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 内容管理service
 */
@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private TbContentMapper tbContentMapper;

    @Autowired
    private JedisClient jedisClient;

    @Value("${CONTENT_LIST}")
    private String CONTENT_LIST;

    /**
     * 添加内容
     *
     * @param content
     * @return
     */
    @Override
    public LstoreResult addContent(TbContent content) {
        //将内容数据插入到内容表
        content.setCreated(new Date());
        content.setUpdated(new Date());
        tbContentMapper.insert(content);
        //缓存同步
        jedisClient.hdel(CONTENT_LIST, content.getCategoryId().toString());
        return LstoreResult.ok();
    }

    /**
     * 获得内容管理列表
     *
     * @param page
     * @param rows
     * @return
     */
    @Override
    public EasyUIDataGridResult getContentList(int page, int rows, long categoryId) {
        //设置分页信息
        PageHelper.startPage(page, rows);
        //查询
        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
        List<TbContent> list = tbContentMapper.selectByExample(example);
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setRows(list);
        //获得结果
        PageInfo<TbContent> pageInfo = new PageInfo<>(list);
        //获取总记录数
        long total = pageInfo.getTotal();
        result.setTotal(total);
        return result;
    }

    /**
     * 根据内容id查询内容列表
     *
     * @param cid
     * @return
     */
    @Override
    public List<TbContent> getContentListById(long cid) {
        //查询缓存
        try {
            String json = jedisClient.hget(CONTENT_LIST, cid + "");
            if (StringUtils.isNotBlank(json)) {
                List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        //设置查询条件
        criteria.andCategoryIdEqualTo(cid);
        List<TbContent> list = tbContentMapper.selectByExampleWithBLOBs(example);
        //把结果添加到缓存
        try {
            jedisClient.hset(CONTENT_LIST, cid + "", JsonUtils.objectToJson(list));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 根据id删除内容列表
     *
     * @param ids
     * @return
     */
    @Override
    public LstoreResult deleteContentList(Long[] ids) {
        //查询cid
        long cintentId = ids[0];
        TbContent tbContent = tbContentMapper.selectByPrimaryKey(cintentId);
        //删除
        List<Long> list = new ArrayList<>();
        for (long id : ids) {
            list.add(id);
        }
        tbContentMapper.deleteByPrimaryKeys(list);
        long cid = tbContent.getCategoryId();
        //缓存同步
        jedisClient.hdel(CONTENT_LIST, cid + "");
        return LstoreResult.ok();
    }

    /**
     * 根据id修改内容列表
     *
     * @param content
     * @return
     */
    @Override
    public LstoreResult editContent(TbContent content) {
        //更新修改时间
        content.setUpdated(new Date());
        tbContentMapper.updateByPrimaryKey(content);
        //缓存同步
        jedisClient.hdel(CONTENT_LIST, content.getCategoryId().toString());
        return LstoreResult.ok();
    }
}
