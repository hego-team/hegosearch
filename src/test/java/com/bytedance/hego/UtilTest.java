package com.bytedance.hego;

import com.bytedance.hego.entity.Document;
import com.bytedance.hego.util.LevelDBUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class UtilTest {

    @Resource
    private LevelDBUtil levelDBUtil;

    @Test
    public void testLevelDBUtil() {
        List<String> ids = new ArrayList<String>();
        ids.add("0");
        ids.add("1");
        ids.add("2");
        for (String id: ids) {
            Document res = levelDBUtil.getDoc(id);
            res.setDocId(Integer.parseInt(id));
            System.out.println(res);
        }

        Document res = levelDBUtil.getDoc("1");
        res.setDocId(Integer.parseInt("1"));
        System.out.println(res);


        Map<Integer, Float> docIds = levelDBUtil.getDocIds("医院");
        System.out.println(docIds);
    }
}
