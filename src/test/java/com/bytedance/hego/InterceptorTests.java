package com.bytedance.hego;

import com.bytedance.hego.entity.Product;
//import com.bytedance.hego.entity.User;
import com.bytedance.hego.mapper.ProductMapper;
import com.bytedance.hego.mapper.UserMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 *插件测试类
 */


@SpringBootTest
public class InterceptorTests {

    @Resource
    private UserMapper userMapper;  //属性注入

    @Resource
    private ProductMapper productMapper;

    /**
     * 分页配置测试方法
     */
//    @Test
//    //分页插件
//    public void testSelectPage(){
//        //new分页对象。查询第一页，每页5条记录
//        Page<User> pageparms = new Page<User>(1, 5);
//
//        //两个参数：1、分页对象  2、查询对象
//        Page<User> userPage = userMapper.selectPage(pageparms,null);
//
//        //不但完成了分页的查询，还把分好页需要展示的一些额外的辅助的内容比如总记录数，有没有上下页等内容计算好组织到userPage对象里面，方便获取
//        List<User> records = userPage.getRecords();
//        records.forEach(System.out::println);
//
//        long total = userPage.getTotal();
//        System.out.println(total);
//
//        boolean b = userPage.hasNext();
//        System.out.println("下一页？"+b);
//
//        boolean b1 = userPage.hasPrevious();
//        System.out.println("上一页？"+b1);
//
//    }

    //自定义分页
//    @Test
//    public void testSelectPageByAge(){
//        Page<User> pageparms = new Page<User>(1, 5);
//        IPage<User> userIPage = userMapper.selectPageByAge(pageparms, 30);
//        List<User> records = userIPage.getRecords();
//        records.forEach(System.out::println);
//    }


    /**
     * 乐观锁测试类
     */
    //模拟乐观锁场景
    @Test
    public void testConcurrentUpdate(){

        //1、小李取数据
        Product product1 = productMapper.selectById(1L);

        //2、小王取数据
        Product product2 = productMapper.selectById(1L);

        //3、小李将价格加了50元，存入了数据库
        product1.setPrice(product1.getPrice()+50);
        int i = productMapper.updateById(product1);
        System.out.println("小李修改的结果"+i);

        //4、小王将商品减了30元，存入了数据库
        product2.setPrice(product2.getPrice()-30);
        int j = productMapper.updateById(product2);
        System.out.println("小王修改的结果"+j);
        if (j==0){
            Product pro = productMapper.selectById(1L);
            pro.setPrice(pro.getPrice()-30);
            int i1 = productMapper.updateById(pro);
            System.out.println("小王重新修改的结果"+i1);
        }

        //5、最后的结果
        Product product3 = productMapper.selectById(1L);
        System.out.println("老板看价格"+product3.getPrice());

    }

}
