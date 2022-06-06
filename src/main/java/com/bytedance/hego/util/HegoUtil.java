package com.bytedance.hego.util;

import com.bytedance.hego.entity.Document;
import com.huaban.analysis.jieba.JiebaSegmenter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class HegoUtil {

    private JiebaSegmenter segmenter;
//    public ArrayList<String> records;
    private Trie TrieTree;

//    private ZhWordCheckers checkService;

    // Merge Hashmap
    public Map<Integer,Float> addTo(Map<Integer,Float> target, Map<Integer,Float> plus) {
        for (Integer key : plus.keySet()) {
            if (target.containsKey(key)) {
                target.put(key, target.get(key) + plus.get(key));
            } else {
                target.put(key, plus.get(key));
            }
        }
        return target;
    }

    // 判断字符串是否包含中文
    public boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        return m.find();
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

    //初始化高频索引序列
    @PostConstruct
    public void initTireIndex()
    {
        TrieTree=new Trie();
        File csv = new File("data/tireIndex/dictionary.csv");
        csv.setReadable(true);
        csv.setWritable(true);
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            isr = new InputStreamReader(new FileInputStream(csv), "UTF-8");
            br = new BufferedReader(isr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String line = "";
//        records = new ArrayList<>();
        try {
            while ((line = br.readLine()) != null) {
                // System.out.println(line);
                if(line.equals("keyword,count"))
                {
                    continue;
                }
//                records.add(line);
                String[] str=line.split(",");
                int count=Integer.valueOf(str[1]).intValue();
                if(count>4)
                {
                    TrieTree.insert(str[0]);
                }
            }
//            System.out.println("csv表格读取行数：" + records.size());
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    public boolean startsWith(String prefix)
    {
        return TrieTree.startsWith(prefix);
    }

    public List<String> PromptString(String prefix)
    {
        return  TrieTree.PromptString(prefix);
    }


}

//前缀树节点
class TrieNode {
    boolean isleaf;
    Map<Character,TrieNode> next;//用来存，下一个结点的信息，K是存储字符，Value存储结点
    public TrieNode(){
        next=new HashMap<Character ,TrieNode>();
    }

}

//构建Trie树，并实现search、insert、startwith方法
class Trie{
    private TrieNode root;//定义根节点
    public Trie(){
        root = new TrieNode();
    }
    //方法：插入方法
    public void insert(String word){
        if(word == null || word.length() == 0){
            return ;
        }
        TrieNode node = root;
        int len=word.length();
        for(int i=0;i<len;i++){
            Character c = word.charAt(i);
            TrieNode tempNode = node.next.get(c);
            if(tempNode == null){
                tempNode =  new TrieNode();
                node.next.put(c,tempNode);
            }
            node=tempNode;
        }
        node.isleaf = true;
    }

    //search方法
    public boolean search(String word){
        if(word == null || word.length() == 0){
            return false;
        }
        TrieNode node =root;
        int len=word.length();
        for(int i=0;i<len;i++){
            Character c = word.charAt(i);
            TrieNode tempNode = node.next.get(c);
            if(tempNode == null){
                return false;
            }
            node = tempNode;
        }
        return node.isleaf;
    }
    //返回trie中是否有给定前缀开头的单词
    public boolean startsWith(String prefix){
        if(prefix == null || prefix.length() == 0) return false;
        TrieNode node = root;
        int len=prefix.length();
        for(int i=0;i<len;i++){
            TrieNode tempNode = node.next.get(prefix.charAt(i));
            if(tempNode == null){
                return false;
            }
            node=tempNode;
        }
        return true;//不需要判断是否是叶结点

    }

    //返回所有给定前缀开头的单词
    public List<String> PromptString(String prefix)
    {
        List<String> resStr = new ArrayList<>();
        int len=prefix.length();
        TrieNode node = root;
        for(int i=0;i<len;i++){
            TrieNode tempNode = node.next.get(prefix.charAt(i));
            node=tempNode;
        }
        dfs(node,resStr,prefix);
        return  resStr;
    }

    void dfs(TrieNode node,List<String> resStr,String prefix)
    {
        for(char key:node.next.keySet())
        {
            TrieNode tempNode = node.next.get(key);
            if(tempNode.isleaf)
            {
                resStr.add(prefix+key);
            }
            dfs(tempNode,resStr,prefix+key);
        }
    }
}
