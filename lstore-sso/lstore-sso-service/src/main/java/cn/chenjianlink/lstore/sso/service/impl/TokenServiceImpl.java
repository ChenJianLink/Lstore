package cn.chenjianlink.lstore.sso.service.impl;

import cn.chenjianlink.lstore.common.jedis.JedisClient;
import cn.chenjianlink.lstore.common.utils.JsonUtils;
import cn.chenjianlink.lstore.common.utils.LstoreResult;
import cn.chenjianlink.lstore.pojo.TbUser;
import cn.chenjianlink.lstore.sso.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 根据token取用户信息
 */
@Service
public class TokenServiceImpl implements TokenService {
    @Autowired
    private JedisClient jedisClient;
    @Value("${SESSION_EXPIRE}")
    private Integer SESSION_EXPIRE;
    @Override
    public LstoreResult getUserByToken(String token) {
        //根据token到redis取用户信息
        String json = jedisClient.get("SESSION:" + token);
        //没取到，登录过期，返回登录过期
        if (StringUtils.isBlank(json)){
            return LstoreResult.build(201, "用户登录已经过期");
        }
        //更新token的过期时间
        jedisClient.expire("SESSION:" + token, SESSION_EXPIRE);
        TbUser user = JsonUtils.jsonToPojo(json,TbUser.class);
        //返回结果
        return LstoreResult.ok(user);
    }
}
