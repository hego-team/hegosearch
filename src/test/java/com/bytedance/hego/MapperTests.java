package com.bytedance.hego;

import com.bytedance.hego.entity.User;
import com.bytedance.hego.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class MapperTests {

    @Resource
    private UserMapper userMapper;

    @Test
    //增加
    public  void testInsert(){

        User user = new User();

        user.setName("建国500");

        user.setEmail("jiangguxxxo@qq.com");
        //user.setCreateTime(LocalDateTime.now());
        //user.setUpdadeTime(LocalDateTime.now());
        //1.可以在数据库中设置
        //2.业务层中字段自动填充
        int result = userMapper.insert(user);
        System.out.println(result);
    }

    //查询
    @Test
    public void testselect(){

        //按id查询
        //SELECT id,name,age,email FROM user WHERE id=?
        User user = userMapper.selectById(1);
        System.out.println(user);

        //按id列表查询
        //SELECT id,name,age,email FROM user WHERE id IN ( ? , ? , ? )
        List<User> users = userMapper.selectBatchIds(Arrays.asList(1, 2, 3));
        users.forEach(System.out::println);

        //按条件查询
        //SELECT id,name,age,email FROM user WHERE name = ? AND age = ?
        Map<String, Object> map = new HashMap<>();
        map.put("name","建国");  //注意此处是表中的列名，不是类中的属性名
        map.put("age","74");
        List<User> users1 = userMapper.selectByMap(map);
        users1.forEach(System.out::println);
    }

    //改
    @Test
    public void testUpdate(){

        // UPDATE User SET age=?, data_time=? WHERE uid=?
        User user = new User();
        user.setId(18);

        //注意：update时生成的sql自动是动态sql
        //其他的属性值不变
        int result = userMapper.updateById(user);
        System.out.println("结果："+result);
    }

    //删除
    @Test
    public  void testDelect(){

        int i = userMapper.deleteById(1L);
        System.out.println("结果："+i);

    }


    //测试自定义mapper
    @Test
    public void testSelectAllByName(){
        List<User> users = userMapper.selectAllByName("Jack");
        users.forEach(System.out::println);
    }

}
