package cn.chenjianlink.lstore.sso.service.impl;

import cn.chenjianlink.lstore.common.jedis.JedisClient;
import cn.chenjianlink.lstore.common.utils.JsonUtils;
import cn.chenjianlink.lstore.common.utils.LstoreResult;
import cn.chenjianlink.lstore.mapper.TbUserMapper;
import cn.chenjianlink.lstore.pojo.TbUser;
import cn.chenjianlink.lstore.pojo.TbUserExample;
import cn.chenjianlink.lstore.sso.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.UUID;

/**
 * 用户登录处理
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private TbUserMapper userMapper;
    @Autowired
    private JedisClient jedisClient;
    @Value("${SESSION_EXPIRE}")
    private Integer SESSION_EXPIRE;

    @Override
    public LstoreResult userLogin(String username, String password) {
        //判断用户名密码的正确性
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<TbUser> list = userMapper.selectByExample(example);
        if (list == null || list.size() == 0){
            return LstoreResult.build(400, "用户名或密码错误");
        }
        TbUser user = list.get(0);
        if (DigestUtils.md5DigestAsHex(password.getBytes()).equals(user)){
            return LstoreResult.build(400, "用户名或密码错误");
        }
        //生成token
        String token = UUID.randomUUID().toString();
        //把用户信息写入redis
        user.setPassword(null);//密码不带回客户端
        jedisClient.set("SESSION:" + token, JsonUtils.objectToJson(user));
        jedisClient.expire("SESSION:" + token, SESSION_EXPIRE);
        return LstoreResult.ok(token);
    }
}
