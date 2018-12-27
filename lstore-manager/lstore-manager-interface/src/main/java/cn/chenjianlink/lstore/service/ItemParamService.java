package cn.chenjianlink.lstore.service;

import cn.chenjianlink.lstore.common.pojo.EasyUIDataGridResult;
import cn.chenjianlink.lstore.common.utils.LstoreResult;

/**
 *规格参数接口
 */
public interface ItemParamService {
    //查询所有规格参数
    EasyUIDataGridResult getItemParamList(int page, int rows);

    LstoreResult getItemParamById(long itemId);
}
