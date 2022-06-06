//package com.bytedance.hego;
//
//import com.bytedance.hego.entity.User;
//import com.bytedance.hego.mapper.UserMapper;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import javax.annotation.Resource;
//import java.util.List;
//
//@SpringBootTest  //自动初始化Spring初始环境[可以在SpringBoot启动类没有运行的情况下单独运行测试类也能启动整个Spring环境]
//class SpringBootApplicationTests {
//
//    // @Autowired  Spring的注解，按类型装配
//    @Resource  //J2EE的注解，默认按名称装配
//    private UserMapper userMapper;
//
//    @Test
//    void testSelectList() {
//        List<User> users = userMapper.selectList(null);
//        users.forEach(System.out::println);  //lamda表达式打印user每一个成员
//    }
//
//}
