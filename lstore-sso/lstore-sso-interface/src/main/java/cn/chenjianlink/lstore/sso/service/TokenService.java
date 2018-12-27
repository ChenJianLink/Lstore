package cn.chenjianlink.lstore.sso.service;

import cn.chenjianlink.lstore.common.utils.LstoreResult;

/**
 * 根据token查询用户信息
 */
public interface TokenService {
    LstoreResult getUserByToken(String token);
}
