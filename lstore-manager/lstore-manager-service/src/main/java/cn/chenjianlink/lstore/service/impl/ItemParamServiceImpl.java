package cn.chenjianlink.lstore.service.impl;

import cn.chenjianlink.lstore.common.pojo.EasyUIDataGridResult;
import cn.chenjianlink.lstore.common.utils.LstoreResult;
import cn.chenjianlink.lstore.mapper.TbItemParamItemMapper;
import cn.chenjianlink.lstore.mapper.TbItemParamMapper;
import cn.chenjianlink.lstore.pojo.*;
import cn.chenjianlink.lstore.service.ItemParamService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 规格参数Service
 */
@Service
public class ItemParamServiceImpl implements ItemParamService {

    @Autowired
    private TbItemParamMapper tbItemParamMapper;
    @Autowired
    private TbItemParamItemMapper tbItemParamItemMapper;

    /**
     * 查询所有商品规格
     *
     * @param page
     * @param rows
     * @return
     */
    @Override
    public EasyUIDataGridResult getItemParamList(int page, int rows) {
        //设置分页信息
        PageHelper.startPage(page, rows);
        TbItemParamExample example = new TbItemParamExample();
        List<TbItemParam> list = tbItemParamMapper.selectByExampleWithBLOBs(example);
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setRows(list);
        //获得结果
        PageInfo<TbItemParam> pageInfo = new PageInfo<TbItemParam>(list);
        //获取总记录数
        long total = pageInfo.getTotal();
        result.setTotal(total);
        return result;
    }

    @Override
    public LstoreResult getItemParamById(long itemId) {
        //设置查询条件
        TbItemParamItemExample example = new TbItemParamItemExample();
        TbItemParamItemExample.Criteria criteria = example.createCriteria();
        criteria.andItemIdEqualTo(itemId);
        List<TbItemParamItem> list = tbItemParamItemMapper.selectByExampleWithBLOBs(example);
        return LstoreResult.ok(list);
    }
}
