package com.example.guanwang.service.impl;

import com.example.guanwang.Utils.RedisUtils;
import com.example.guanwang.base.util.JwtUtil;
import com.example.guanwang.base.util.MD5;
import com.example.guanwang.bean.LoginUser;
import com.example.guanwang.bean.UserInfo;
import com.example.guanwang.mapper.UserInfoMapper;
import com.example.guanwang.service.UserInfoService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 陈志浩123
 * @since 2023-01-14
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private RedisUtils redisUtils;
    @Override
    public LoginUser login(UserInfo user) {
        LoginUser loginUser=new LoginUser();
        Map<String, Object> map = new HashMap<>();
        map.put("username", user.getUsername());
        List<UserInfo>list=userInfoMapper.selectByMap(map);
        UserInfo user1=null;
        if(list.size()>0){
             user1 = list.get(0);
        }else {
            loginUser.setLogin(false);
            return loginUser;
        }
        try {
            // 校验用户名密码
            String encrypt = MD5.toMD5(user.getPassword() + user.getSalt());
            if (!StringUtils.equalsIgnoreCase(encrypt, user.getPassword())) {
                loginUser.setStade(false);
            }
        } catch (Exception e) {
           // throw new MyException("该用户名或者密码错误,请检查后再登录!");
        }

        //根据电话号码和密码加密生成token
        loginUser.setUserInfo(user1);
        String token = JwtUtil.sign(user1.getUsername(), user1.getPassword());
        redisUtils.set(token,user1,3600L);
        loginUser.setToken(token);
        loginUser.setLogin(true);
        return loginUser;
    }
}
