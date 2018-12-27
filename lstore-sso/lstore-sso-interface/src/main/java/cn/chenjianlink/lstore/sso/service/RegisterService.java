package cn.chenjianlink.lstore.sso.service;

import cn.chenjianlink.lstore.common.utils.LstoreResult;
import cn.chenjianlink.lstore.pojo.TbUser;

public interface RegisterService {
    LstoreResult checkData(String param, int type);
    LstoreResult register(TbUser user);
}
