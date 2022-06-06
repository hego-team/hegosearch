//package com.bytedance.hego.service.Impl;
//
//
//import com.bytedance.hego.entity.User;
//import com.bytedance.hego.mapper.UserMapper;
//import com.bytedance.hego.service.UserService;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
//
////    @Resource
////    private UserMapper userMapper;
//
//    //自定义service方法
//    @Override
//    public List<User> listAllByName(String name) {
//        return baseMapper.selectAllByName(name);
//    }
//}
package com.bytedance.hego.service.Impl;

import com.bytedance.hego.entity.User;
import com.bytedance.hego.dao.UserDAO;
import com.bytedance.hego.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public List<User> findAll() {
        return userDAO.findAll();
    }

    @Override
    public User save(User user) {
        if (user.getId() != null) {
            // 更新用户信息
            User oldUser = userDAO.findById(user.getId());
            if (oldUser == null) {
                return null;
            }
            return userDAO.update(user) ? userDAO.findById(user.getId()) : null; // 更新成功则返回user，失败则返回null
        } else {
            userDAO.insert(user);
            return user.getId() != null ? userDAO.findById(user.getId()) : null; // 保存成功则返回user，失败则返回null
        }
    }

    @Override
    public User handleLogin(String name, String password) {
        User user = userDAO.find(name, password);
        if (user != null) {
            user.setLastLoginTime(new Date());
            userDAO.update(user);
        }
        return user;
    }

    @Override
    public User findById(int id) {
        return userDAO.findById(id);
    }

    @Override
    public User findByName(String name) {
        return userDAO.findByName(name);
    }

    @Override
    public boolean updatePassword(int id, String newPass) {
        User user = new User();
        user.setId(id);
        user.setPassword(newPass);
        return userDAO.update(user);
    }

    @Override
    public boolean delete(int id) {
        User user = userDAO.findById(id);
        if (user == null) {
            return false;
        }

        return userDAO.deleteById(id);
    }
}