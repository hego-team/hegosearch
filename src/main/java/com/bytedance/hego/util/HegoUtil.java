package com.bytedance.hego.util;

import com.huaban.analysis.jieba.JiebaSegmenter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    // replaceAll method for StringBuilder
    public void replaceAll(StringBuilder sb, String find, String replace){

        //compile pattern from find string
        Pattern p = Pattern.compile(find);

        //create new Matcher from StringBuilder object
        Matcher matcher = p.matcher(sb);

        //index of StringBuilder from where search should begin
        int startIndex = 0;

        while( matcher.find(startIndex) ){

            sb.replace(matcher.start(), matcher.end(), replace);

            //set next start index as start of the last match + length of replacement
            startIndex = matcher.start() + replace.length();
        }
    }

}
