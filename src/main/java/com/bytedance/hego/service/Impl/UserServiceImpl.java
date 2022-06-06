package com.bytedance.hego.service.Impl;


import com.bytedance.hego.entity.User;
import com.bytedance.hego.mapper.UserMapper;
import com.bytedance.hego.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

//    @Resource
//    private UserMapper userMapper;

    //自定义service方法
    @Override
    public List<User> listAllByName(String name) {
        return baseMapper.selectAllByName(name);
    }
}
