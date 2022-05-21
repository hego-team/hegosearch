package com.bytedance.hego;


import com.bytedance.hego.entity.User;
import com.bytedance.hego.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class ServiceTest {

    @Resource
    private UserService userService;

    @Test
    public void testCount(){

        //SELECT COUNT( * ) FROM user
        int count = userService.count();
        System.out.println("user表记录数："+count);
    }

    @Test
    public void testSaveBatch(){

        ArrayList<User> users = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            User user = new User();
            user.setId(i+6);
            user.setAge(20+i);
            user.setName("花花"+i);
            users.add(user);
        }

        boolean b = userService.saveBatch(users);
        System.out.println("插入结果："+b);
    }

    @Test
    public void testListAllbyName(){
        List<User> users = userService.listAllByName("Tom");
        users.forEach(System.out::println);
    }
}
