package cn.chenjianlink.lstore.sso.service.impl;

import cn.chenjianlink.lstore.common.utils.LstoreResult;
import cn.chenjianlink.lstore.mapper.TbUserMapper;
import cn.chenjianlink.lstore.pojo.TbUser;
import cn.chenjianlink.lstore.pojo.TbUserExample;
import cn.chenjianlink.lstore.sso.service.RegisterService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 * 用户信息处理Service
 */
@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private TbUserMapper userMapper;

    //校验数据
    @Override
    public LstoreResult checkData(String param, int type) {
        //根据不同的type生成不同的查询条件
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        if (type == 1) {
            criteria.andUsernameEqualTo(param);
        } else if (type == 2) {
            criteria.andPhoneEqualTo(param);
        } else if (type == 3) {
            criteria.andEmailEqualTo(param);
        } else {
            return LstoreResult.build(400, "数据类型错误");
        }
        //执行查询
        List<TbUser> list = userMapper.selectByExample(example);
        //判断结果中是否包含数据
        if (list != null && list.size() > 0) {
            //如果有数据返回false
            return LstoreResult.ok(false);
        }
        //如果没有数据返回true
        return LstoreResult.ok(true);
    }

    //注册
    @Override
    public LstoreResult register(TbUser user) {
        //校验数据
        if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword()) || StringUtils.isBlank(user.getPhone())) {
            return LstoreResult.build(400, "用户数据不完整，注册失败");
        }
        LstoreResult result = checkData(user.getUsername(), 1);
        if (!(boolean) result.getData()) {
            return LstoreResult.build(400, "此用户名已被占用");
        }
        result = checkData(user.getPhone(), 2);
        if (!(boolean) result.getData()) {
            return LstoreResult.build(400, "此手机号已被占用");
        }
        user.setCreated(new Date());
        user.setUpdated(new Date());
        String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5Pass);
        userMapper.insert(user);
        return LstoreResult.ok();
    }
}
