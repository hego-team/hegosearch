package com.bytedance.hego.mapper;

import com.bytedance.hego.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * 继承通用mapper,当前mapper要管理的是User实体类（泛型：要管理的实体对象）
 * UserMapper接口负责管理User实体
 * 要管理哪个实体，就写哪个泛型
 */
public interface UserMapper extends BaseMapper<User> {
    //对通用mapper做扩展
    List<User> selectAllByName(String name);

    /**
     * 查询 : 根据年龄查询用户列表，分页显示
     *
     * @param page 分页对象,xml中可以从里面进行取值,传递参数 Page 即自动分页,必须放在第一位
     * @param age 年龄
     * @return 分页对象
     */
    //自定义一个Mapper层的方法
    IPage<User> selectPageByAge(Page<?> page, Integer age);
}
