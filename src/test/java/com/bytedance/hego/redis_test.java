package com.bytedance.hego;

import com.bytedance.hego.entity.Redis_Result;
import com.bytedance.hego.util.RedisServiceUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author swb
 * @create 2022-05-29 17:20
 */
@SpringBootTest
public class redis_test {
    @Autowired
    private RedisServiceUtil redisServiceUtil;

    @Test
    public void redisConnectTest(){
        String testKey = "search_suoyin_1";
        String testKey_2 = "sss";
        Redis_Result result1 = new Redis_Result();
        result1.setTime(90);
        result1.setTotal(1000);
        redisServiceUtil.set(testKey,result1 );
        Object result = redisServiceUtil.get(testKey);
        System.out.println("获取redis测试数据：" + result);
    }
}
