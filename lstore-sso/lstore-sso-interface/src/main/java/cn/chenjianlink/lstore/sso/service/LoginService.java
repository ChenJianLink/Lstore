package cn.chenjianlink.lstore.sso.service;

import cn.chenjianlink.lstore.common.utils.LstoreResult;

public interface LoginService {
    LstoreResult userLogin(String username, String password);
}
