package com.bytedance.hego.service;

import com.bytedance.hego.entity.Document;
import com.bytedance.hego.entity.Page;
import com.bytedance.hego.entity.SearchResult;
import com.bytedance.hego.util.HegoUtil;
import com.bytedance.hego.util.LevelDBUtil;
import com.huaban.analysis.jieba.JiebaSegmenter;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class SearchService {

    @Resource
    private LevelDBUtil levelDBUtil;

    @Resource
    private HegoUtil hegoUtil;


    /**
     * 将用户输入的query清洗并分词得到keywords
     * @param query
     * @return keywords list
     */
    public List<String> tokenizeQuery(String query) {
        // 去除中文以外字符
        String sentences = query.replaceAll("[^\u4E00-\u9FA5]", "");
        // 分词
        JiebaSegmenter segmenter = new JiebaSegmenter();
        List<String> keywords = hegoUtil.tokenize(sentences);
        return keywords;
    }

    /**
     * 从倒排索引中提取keywords的docIds并合并
     * @param keywords
     * @return merge_docIds
     */
    public Map<Integer, Float> findIdsByKeywords(List<String> keywords) {
        Map<Integer, Float> allIds = new HashMap<>();

        // 将每个doc id的score加起来
        for(String keyword : keywords) {
            Map<Integer, Float> docIds = levelDBUtil.getDocIds(keyword);

            if(docIds != null) {
                allIds = hegoUtil.addTo(allIds, docIds);
            }
        }

        return allIds;
    }

    /**
     * 将docId按score排序
     * @param ids
     * @return rank_ids
     */
    public List<Integer> rankIdsByScore(Map<Integer, Float> ids) {
        List<Integer> rankIds = new ArrayList<>();

        List<Map.Entry<Integer, Float>> tmp = new ArrayList<Map.Entry<Integer, Float>>(ids.entrySet());
        tmp.sort(new Comparator<Map.Entry<Integer, Float>>() {
            @Override
            public int compare(Map.Entry<Integer, Float> o1, Map.Entry<Integer, Float> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        for (Map.Entry<Integer, Float> entry : tmp) {
            Integer id = entry.getKey();
            rankIds.add(id);
        }
        return rankIds;
    }

    /**
     * 查询docIds对应的documents
     * @param ids
     * @return documents list
     */
    public List<Document> findDocsByIds(List<Integer> ids) {
        List<Document> docs = new ArrayList<>();
        for (Integer id: ids) {
            Document doc = levelDBUtil.getDoc(Integer.toString(id));
            doc.setDocId(id);
            docs.add(doc);
        }
        return docs;
    }

    /**
     * 根据query与page查询document
     * @param query
     * @return
     */
    public SearchResult findDocsByQuery(String query, int current) {

        // 将query清洗分词得到keywords
        List<String> keywords = this.tokenizeQuery(query);
        // 找到keywords对应的docIds并合并
        Map<Integer, Float> ids = this.findIdsByKeywords(keywords);
        // 将docIds按score排序
        List<Integer> rankIds = this.rankIdsByScore(ids);

        // 提取当前页的docIds
        Page page = new Page();
        page.setCurrent(current);
        page.setRows(rankIds.size());
        // 当前页的起止document
        int start = page.getStart();
        int end = page.getEnd();
        List<Integer> pageIds = rankIds.subList(start, end);

        // 根据docIds查询documents
        List<Document> documents = this.findDocsByIds(pageIds);

        //将查询到的documents封装进searchResult
        SearchResult searchResult = new SearchResult();
        searchResult.setDocuments(documents);
        searchResult.setTotal(ids.size());
        searchResult.setPage(page);

        return searchResult;
    }
}
