package com.bytedance.hego.Controller;

import com.bytedance.hego.entity.User;
import com.bytedance.hego.service.UserService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController  //返回json数据
@RequestMapping("/user")  //给一个路径
@CrossOrigin
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/list")
    public List<User> list (){
        //返回数据库中的用户列表
        return userService.list();
    }
}
