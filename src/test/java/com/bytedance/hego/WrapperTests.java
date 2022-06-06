//package com.bytedance.hego;
//
//import com.bytedance.hego.entity.User;
//import com.bytedance.hego.mapper.UserMapper;
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
//import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
//import com.baomidou.mybatisplus.core.toolkit.StringUtils;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import javax.annotation.Resource;
//import java.util.List;
//import java.util.Map;
//
//@SpringBootTest
//public class WrapperTests {
//    @Resource
//    private UserMapper userMapper;
//
//    /**
//     * 组装查询条件
//     * 查询名字中包含n，年龄大于等于10且小于等于20，email不为空的用户
//     */
//    @Test
//    public void test1() {
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//
//        //column：对应数据库表中的列名，而不是实体的属性名
//        queryWrapper
//                .like("name", "花")
//                .between("age",20,25)
//                .isNotNull("email");
//        List<User> users = userMapper.selectList(queryWrapper);
//        users.forEach(System.out::println);
//    }
//
//
//    /**
//     * 组装排序条件
//     * 按年龄降序查询用户，如果年龄相同则按id升序排列
//     */
//    @Test
//    public void test2() {
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        queryWrapper
//                .orderByDesc("age")
//                .orderByAsc("id");
//        List<User> users = userMapper.selectList(queryWrapper);
//        users.forEach(System.out::println);
//    }
//
//
//    /**
//     * 删除email为空的用户
//     */
//    @Test
//    public void test3(){
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        queryWrapper
//                .isNull("email");
//        int result = userMapper.delete(queryWrapper);
//        System.out.println("删除的记录数："+result);
//    }
//
//
//    /**
//     * 查询名字中包含n，且（年龄小于18或email为空的用户）
//     * 并将这些用户的年龄设置为18，邮箱设置为 user@atguigu.com
//     */
//    @Test
//    public void test4(){
//
//        //组装查询条件
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        queryWrapper.like("name","花")
//                .and(i-> i.lt("age",18).or().isNull("email"));  //lambda表达式内的逻辑优先运算
//
//        //组装更新条件
//        User user = new User();
//        user.setAge(18);
//        user.setEmail("user@atguigu.com");
//
//        //执行更新
//        int update = userMapper.update(user, queryWrapper);
//        System.out.println("更新结果："+update);
//
//    }
//
//    /**
//     * 查询所有用户的用户名和年龄
//     */
//    @Test
//    public void test5(){
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//
//        //组装select语句
//        queryWrapper.select("name","age");
//
////        List<User> users = userMapper.selectList(queryWrapper);
////        users.forEach(System.out::println);
//
//        //selectMaps()返回Map集合列表，通常配合select()使用，避免User对象中没有被查询到的列值为null
//        List<Map<String, Object>> maps = userMapper.selectMaps(queryWrapper);
//        maps.forEach(System.out::println);
//    }
//
//
//    /**
//     * 使用子查询：
//     * 查询id不大于3的所有用户的id列表
//     */
//    @Test
//    public  void test6(){
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
////        queryWrapper.inSql("uid","select uid from user where uid <4");
////        queryWrapper.in("uid",1,2,3);
//        queryWrapper.le("uid",3);
//
////        List<User> users = userMapper.selectList(queryWrapper);
////        users.forEach(System.out::println);
//
//        //selectObjs的使用场景：只返回一列
//        List<Object> objects = userMapper.selectObjs(queryWrapper);//返回值是Object列表
//        objects.forEach(System.out::println);
//    }
//
//
//    /**
//     *查询名字中包含n，且（年龄小于18或email为空的用户）
//     * 并将这些用户的年龄设置为18，邮箱设置为 user@atguigu.com
//     */
//    @Test
//    public void test7(){
//
//        //组装查询条件和更新条件
//        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
//        updateWrapper
//                .set("age",18)
//                .set("email","user@atguigu.com")
//                .like("name","花")
//                .and(i-> i.lt("age",18).or().isNull("email"));  //lambda表达式内的逻辑优先运算
//
//        //不使用对象的方式组装更新条件
//
////        User user = new User();
////        user.setAge(18);
////        user.setEmail("user@atguigu.com");
//
//        //执行更新。这种方式无法自动填充updateTime字段
////        int update = userMapper.update(null, updateWrapper);
////        System.out.println("更新结果："+update);
//
//        //告诉update方法，要更新的是哪个对象.更新的时候通过自动填充的功能去检查user对象有没有相关的字段有相关的fill属性。有的话，做自动填充
//        User user = new User();
//        int update = userMapper.update(user, updateWrapper);
//        System.out.println("更新结果："+update);
//    }
//
//    /**
//     * 查询名字中包含n，年龄大于10且小于20的用户，查询条件来源于用户输入，是可选的
//     */
//    @Test
//    public void test8(){
//
//        String username="花";
//        Integer agebegin=20;
//        Integer ageend=30;
//
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//
//        if (StringUtils.isNotBlank(username)){
//            queryWrapper.like("name",username);
//        }
//        if (agebegin!= null){
//            queryWrapper.ge("age",agebegin);
//        }
//        if (ageend!=null){
//            queryWrapper.le("age",ageend);
//        }
//
//        //若某一个条件输入为空，则不会在queryWrapper组装
//        List<User> users = userMapper.selectList(queryWrapper);
//        users.forEach(System.out::println);
//    }
//
//
//    /**
//     * 查询名字中包含n，年龄大于10且小于20的用户，查询条件来源于用户输入，是可选的
//     */
//    @Test
//    public void test8_2(){
//
//        String username="花";
//        Integer agebegin=20;
//        Integer ageend=30;
//
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        queryWrapper.like(StringUtils.isNotBlank(username),"name",username)
//                    .ge(agebegin!= null,"age",agebegin)
//                    .le(ageend!=null,"age",ageend);
//
//        //若某一个条件输入为空，则不会在queryWrapper组装
//        List<User> users = userMapper.selectList(queryWrapper);
//        users.forEach(System.out::println);
//    }
//
//
//    /**
//     * 查询名字中包含n，年龄大于10且小于20的用户，查询条件来源于用户输入，是可选的
//     */
//    @Test
//    public void test9(){
//
//        //定义查询条件，有可能为null（用户未输入）
//        String username="花";
//        Integer agebegin=20;
//        Integer ageend=30;
//
//        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper
//                //避免使用字符串表示数据库字段，防止运行时错误
//                .like(StringUtils.isNotBlank(username),User::getName,username)
//                .ge(agebegin!= null,User::getAge,agebegin)
//                .le(ageend!=null,User::getAge,ageend);
//
//        //若某一个条件输入为空，则不会在queryWrapper组装
//        List<User> users = userMapper.selectList(queryWrapper);
//        users.forEach(System.out::println);
//    }
//
//
//    /**
//     *查询名字中包含n，且（年龄小于18或email为空的用户）
//     * 并将这些用户的年龄设置为18，邮箱设置为 user@atguigu.com
//     */
//    @Test
//    public void test10(){
//
//        //组装查询条件和更新条件
//        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
//        updateWrapper
//                .set(User::getAge,18)
//                .set(User::getEmail,"user@atguigu.com")
//                .like(User::getName,"花")
//                .and(i-> i.lt(User::getAge,23).or().isNull(User::getEmail));  //lambda表达式内的逻辑优先运算
//
//        User user = new User();
//        int update = userMapper.update(user, updateWrapper);
//        System.out.println("更新结果："+update);
//    }
//}
