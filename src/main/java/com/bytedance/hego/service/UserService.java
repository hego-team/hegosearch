//package com.bytedance.hego.service;
//
//
//import com.bytedance.hego.entity.User;
//import com.baomidou.mybatisplus.extension.service.IService;
//
//import java.util.List;
//
////运行时，MP会根据UserService接口动态生成代理对象。通过代理对象调用IService里面的方法
//public interface UserService extends IService<User> {
//    List<User> listAllByName(String name);
//}
package com.bytedance.hego.service;

import com.bytedance.hego.entity.User;

import java.util.List;

public interface UserService {

    /**
     * 查询所有用户
     * @return
     */
    List<User> findAll();

    /**
     * 更新或保存用户
     * @param user
     * @return
     */
    User save(User user);

    /**
     * 登录操作
     * 成功则返回新user，失败则返回null
     * @param name
     * @param password
     * @return
     */
    User handleLogin(String name, String password);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    User findById(int id);

    /**
     * 根据id查询
     * @param name
     * @return
     */
    User findByName(String name);

    /**
     * 更新密码
     * @param id
     * @param newPass
     * @return
     */
    boolean updatePassword(int id, String newPass);

    /**
     * 删除用户
     * @param id
     * @return
     */
    boolean delete(int id);
}