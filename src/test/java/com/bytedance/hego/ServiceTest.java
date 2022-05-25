package com.bytedance.hego;


import com.bytedance.hego.entity.Document;
import com.bytedance.hego.entity.SearchResult;
import com.bytedance.hego.entity.User;
import com.bytedance.hego.service.SearchService;
import com.bytedance.hego.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class ServiceTest {

    @Resource
    private UserService userService;

    @Resource
    private SearchService searchService;

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
    public void testListAllbyName() {
        List<User> users = userService.listAllByName("Tom");
        users.forEach(System.out::println);
    }

    @Test
    void testTokenizeQuery() {
        String query = "当然从医院患者人满为患的情况看,也同样缺少医生";
        List<String> keywords = searchService.tokenizeQuery(query);
        System.out.println(keywords);
    }

    @Test
    public void testFindIdsByKeywords() {
        List<String> keywords = new ArrayList<>();
        keywords.add("医院");
        keywords.add("患者");
        keywords.add("文章");
        Map<Integer, Float> ids = searchService.findIdsByKeywords(keywords);

    }

    @Test
    public void testRankIdsByScore() {
        Map<Integer, Float> ids = new HashMap<>();
        ids.put(1, (float) 0.707567);
        ids.put(2, (float) 0.535);
        ids.put(3, (float) 0.99);

        List<Integer> rankIds = searchService.rankIdsByScore(ids);
        System.out.println(rankIds);
    }

    @Test
    void testFindDocsByIds() {
        List<Integer> ids = new ArrayList<Integer>();
        ids.add(0);
        ids.add(1);
        ids.add(2);

        List<Document> docs = searchService.findDocsByIds(ids);
        System.out.println(docs);
    }

    @Test
    void testFindDocsByQuery() {
        String query = "医院患者";
        SearchResult searchResult = searchService.findDocsByQuery(query);
        System.out.println(searchResult);
    }

}
