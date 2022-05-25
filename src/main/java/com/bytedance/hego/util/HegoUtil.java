package com.bytedance.hego.util;

import com.huaban.analysis.jieba.JiebaSegmenter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class HegoUtil {

    private JiebaSegmenter segmenter;

    // Merge Hashmap
    public static Map<Integer,Float> addTo(Map<Integer,Float> target, Map<Integer,Float> plus) {
        for (Integer key : plus.keySet()) {
            if (target.containsKey(key)) {
                target.put(key, target.get(key) + plus.get(key));
            } else {
                target.put(key, plus.get(key));
            }
        }
        return target;
    }

    // 初始化结巴分词字典
    @PostConstruct
    public void initJieba() {
        this.segmenter = new JiebaSegmenter();
    }

    public List<String> tokenize(String sentence) {
        List<String> keywords = this.segmenter.sentenceProcess(sentence);
        return keywords;
    }

}
