package com.bytedance.hego.service;


import com.bytedance.hego.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

//运行时，MP会根据UserService接口动态生成代理对象。通过代理对象调用IService里面的方法
public interface UserService extends IService<User> {
    List<User> listAllByName(String name);
}
