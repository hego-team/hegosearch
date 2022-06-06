package com.bytedance.hego.service;

import com.bytedance.hego.service.Impl.BtService;
import com.bytedance.hego.util.ZhWordCheckers;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bytedance.hego.entity.Document;
import com.bytedance.hego.entity.Page;
import com.bytedance.hego.entity.SearchResult;
import com.bytedance.hego.util.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.util.*;

@Service
public class SearchService {

    @Resource
    private LevelDBUtil levelDBUtil;

    @Resource
    private HegoUtil hegoUtil;

    @Resource
    private AuthService authService;

    @Resource
    private RedisServiceUtil redisServiceUtil;


    /**
     * 将用户输入的query清洗并分词得到keywords
     * @param query 用户输入的查询内容
     * @return keywords list
     */
    public List<String> queryToKeywords(String query) {
        // 去除中文以外字符
        String sentences = query.replaceAll("[^\u4E00-\u9FA5]", "");
        // 分词
        return hegoUtil.tokenize(sentences);
    }

    public String imageToQuery(MultipartFile file) {

        // 请求url: 调用百度通用物体识别api识别图片
        String url = "https://aip.baidubce.com/rest/2.0/image-classify/v2/advanced_general";
        StringBuilder description = new StringBuilder();
        try {
            byte[] imgData = file.getBytes();
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");
            String param = "image=" + imgParam;

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = authService.getAuth();
            String jsonResult = HttpUtil.post(url, accessToken, param);
            // {"result_num":5,"result":[{"keyword":"人物特写","score":0.776894,"root":"人物-人物特写"}
            // 从JSON中提取keyword并串联成文字描述query
            Map<String, String> tmp = JSON.parseObject(jsonResult, new TypeReference<Map<String, String>>() {});
            String tmpResult = tmp.get("result");
            List<Map<String, String>> results = JSON.parseObject(tmpResult, new TypeReference<List<Map<String, String>>>() {});
            for (Map<String, String> result: results) {
                description.append(result.get("keyword"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 删除一些无用且影响搜索结果的词
        hegoUtil.replaceAll(description, "截图", "");
        hegoUtil.replaceAll(description, "屏幕", "");
        hegoUtil.replaceAll(description, "矢量图", "");
        hegoUtil.replaceAll(description, "人物", "");

        return description.toString();

    }

    /**
     * 从倒排索引中提取keywords的docIds并合并
     * @param keywords query分词得到的keywords
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
     * @param ids 根据keywords查询到的所有docId
     * @return rank_ids
     */
    public List<Integer> rankIdsByScore(Map<Integer, Float> ids) {
        List<Integer> rankIds = new ArrayList<>();

        List<Map.Entry<Integer, Float>> tmp = new ArrayList<>(ids.entrySet());
        tmp.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));

        for (Map.Entry<Integer, Float> entry : tmp) {
            Integer id = entry.getKey();
            rankIds.add(id);
        }
        return rankIds;
    }

    /**
     * 查询docIds对应的documents
     * @param ids 排序后的docIds
     * @return documents list
     */
    public List<Document> findDocsByIds(List<Integer> ids, List<String> keywords) {
        List<Document> docs = new ArrayList<>();
        for (Integer id: ids) {
            Document doc = levelDBUtil.getDoc(Integer.toString(id));

            // 处理关键词高亮
            String text = doc.getContent();
            StringBuilder tmp = new StringBuilder(text);
            for (String keyword: keywords) {
                if (tmp.indexOf(keyword) != -1) {
                    hegoUtil.replaceAll(tmp, keyword, "<em>" + keyword + "</em>");
                }
            }
            String hightlightText = tmp.toString();

            doc.setContent(hightlightText);
            doc.setDocId(id);
            docs.add(doc);
        }

        return docs;
    }

    /**
     * 根据query查询document
     * @param query 用户输入的查询内容
     * @return searchResult 查询结果
     */
    public SearchResult findDocsByQuery(String query, String filter, int current, int limit) {


        // 从缓存中取数据query::filter::current
        String redisKey = RedisKeyUtil.getResultKey(query, filter, current, limit);
        SearchResult cacheResult = getCache(redisKey);

        if (cacheResult != null) {
            return cacheResult;
        }


        // 将query清洗分词得到keywords
        List<String> keywords = this.queryToKeywords(query);

        // 找到keywords对应的docIds并合并，docIds为空直接return
        Map<Integer, Float> ids = findIdsByKeywords(keywords);

        // 关键词过滤: 将filter中的keyword对应的docIds从docIds结果集中删除
        if (!filter.equals("")) {
            List<String> filterKeywords = this.queryToKeywords(filter);
            Map<Integer, Float> filterIds = this.findIdsByKeywords(filterKeywords);
            Iterator iterator = ids.keySet().iterator();
            while (iterator.hasNext()) {
                Integer keyword = (Integer) iterator.next();
                if (filterIds.containsKey(keyword)) {
                    iterator.remove();
                }
            }
        }

        // 将docIds按score排序
        List<Integer> rankIds = this.rankIdsByScore(ids);
        // 提取当前页的docIds
        Page page = new Page();
        page.setRows(rankIds.size());
        page.setCurrent(Math.min(current, page.getTotal()));
        page.setLimit(Math.max(limit, 0));
        // 当前页的起止document
        int start = page.getStart();
        int end = page.getEnd();
        List<Integer> pageIds = rankIds.subList(start, end);

        // 根据docIds查询documents
        List<Document> documents = this.findDocsByIds(pageIds, keywords);

        // 将查询到的documents封装进searchResult
        SearchResult searchResult = new SearchResult();
        searchResult.setDocuments(documents);
        searchResult.setTotal(ids.size());
        searchResult.setPage(page);

        // 如果查到的documents为0，开启拼写检查
        // 否则将searchResult放入缓存
        if (ids.size() == 0) {
            searchResult.setCheck(CheckByQuery(query));
        }
        else {
            initCache(redisKey, searchResult);
        }

        return searchResult;
    }

    // 优先从缓存中取值
    private SearchResult getCache(String redisKey) {
        return (SearchResult) redisServiceUtil.get(redisKey);
    }

    // 缓存中取不到时初始化缓存数据
    private void initCache(String redisKey, SearchResult searchResult) {
        redisServiceUtil.set(redisKey, searchResult);
    }

    /**
     * 根据query查询提示词
     * @param query
     * @return
     */
    public List<String> findPromptByQuery(String query) {

        if (hegoUtil.startsWith(query)) {
            return hegoUtil.PromptString(query);
        }
        else {
            return  new ArrayList<>();
        }
    }

    /**
     * 根据query纠正结果
     * @param query
     * @return
     */
    public List<String> CheckByQuery(String query) {

        return  ZhWordCheckers.correctList(query,10);
    }

}
