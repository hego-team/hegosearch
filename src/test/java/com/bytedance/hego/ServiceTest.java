package com.bytedance.hego;


import com.bytedance.hego.entity.Document;
import com.bytedance.hego.entity.SearchResult;
import com.bytedance.hego.entity.User;
import com.bytedance.hego.service.SearchService;
import com.bytedance.hego.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.*;

@SpringBootTest
public class ServiceTest {

    @Resource
    private UserService userService;

    @Resource
    private SearchService searchService;






    @Test
    void testTokenizeQuery() {
        String query = "当然从医院患者人满为患的情况看,也同样缺少医生";
        List<String> keywords = searchService.queryToKeywords(query);
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
        // ids.add(0);
        ids.add(1);
        ids.add(2);
        List<String> keywords = new ArrayList<>();
        keywords.add("医院");
        keywords.add("患者");
        keywords.add("文章");

        List<Document> docs = searchService.findDocsByIds(ids, keywords);
        System.out.println(docs);
    }

    @Test
    void testFindDocsByQuery() {
        String query = "医院患者";
        SearchResult searchResult = searchService.findDocsByQuery(query, "", 2);
        System.out.println(searchResult);

        Map<Integer, Double> keywords = new HashMap<>();
        keywords.put(1, 0.1);
        keywords.put(2, 0.2);
        keywords.put(3, 0.3);
        keywords.put(4, 0.4);

        Map<Integer, Double> filterKeywords = new HashMap<>();
        filterKeywords.put(2, 0.2);
        filterKeywords.put(3, 0.3);

        Iterator iterator = keywords.keySet().iterator();
        while (iterator.hasNext()) {
            Integer keyword = (Integer) iterator.next();
            if (filterKeywords.containsKey(keyword)) {
                iterator.remove();
            }
        }
        System.out.println(keywords);

    }

    @Test
    void testImageToKeywords() {
        // searchService.imageToQuery();
    }

}
