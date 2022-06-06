package com.bytedance.hego;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bytedance.hego.service.AuthService;
import com.bytedance.hego.service.Impl.BtService;
import com.bytedance.hego.service.SearchService;
import com.bytedance.hego.util.Base64Util;
import com.bytedance.hego.util.FileUtil;
import com.bytedance.hego.util.HttpUtil;
import com.bytedance.hego.util.ZhWordCheckers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 测试百度图片识别API
 */

@SpringBootTest
public class APITest {

    @Resource
    private AuthService authService;

    @Resource
    private SearchService searchService;

    @Resource
    private BtService btService;

    /**
     * 通用物体和场景识别api测试
     */
    @Test
    public void testAdvancedGeneral() {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/image-classify/v2/advanced_general";
        try {
            // 本地文件路径
            String filePath = "D:/photo/blog.png";
            byte[] imgData = FileUtil.readFileByBytes(filePath);
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");
            String param = "image=" + imgParam;

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = authService.getAuth();
            String jsonResult = HttpUtil.post(url, accessToken, param);
            // {"result_num":5,"result":[{"keyword":"人物特写","score":0.776894,"root":"人物-人物特写"}
            Map<String, String> tmp = JSON.parseObject(jsonResult, new TypeReference<Map<String, String>>() {});
            String tmpResult = tmp.get("result");
            List<Map<String, String>> results = JSON.parseObject(tmpResult, new TypeReference<List<Map<String, String>>>() {});

            StringBuilder description = new StringBuilder();
            for (Map<String, String> result: results) {
                description.append(result.get("keyword"));
            }

            String query = description.toString();
            List<String> keywords = searchService.queryToKeywords(query);
            System.out.println(keywords);
            // return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTransAPI() {
        System.out.println("schoolstudent学习math搜索answer");
        System.out.println(btService.translate("schoolstudent学习math搜索answer"));
    }

    @Test
    public void testCheckAPI() {
        System.out.println(ZhWordCheckers.correctList("跑的飞快",10));
    }

}
