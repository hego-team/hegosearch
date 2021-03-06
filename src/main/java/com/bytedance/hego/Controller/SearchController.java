package com.bytedance.hego.Controller;

import com.bytedance.hego.entity.SearchResult;
import com.bytedance.hego.service.SearchService;
import com.bytedance.hego.util.RedisKeyUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {

    @Resource
    private SearchService searchService;


    // 文字搜索接口
    @RequestMapping(value="/text", method=RequestMethod.GET)
    public SearchResult getSearchResult(@RequestParam("query") String query,
                                        @RequestParam(value = "filter", required = false) String filter,
                                        @RequestParam(value = "page") int current,
                                        @RequestParam(value = "limit") int limit) {

        long start = System.currentTimeMillis();

        if (filter == null) {
            filter = "";
        }

        // 从缓存中取数据query::filter::current::limit
        String redisKey = RedisKeyUtil.getResultKey(query, filter, current, limit);
        SearchResult cacheResult = searchService.getCache(redisKey);
        if (cacheResult != null) {
            long end = System.currentTimeMillis();
            cacheResult.setTime(end - start);
            return cacheResult;
        }


        // 翻译query中的英文
        String transQuery= searchService.transQuery(query.replaceAll(" ", ""));
        SearchResult searchResult = searchService.findDocsByQuery(transQuery, filter, current, limit);

        // 存入缓存
        searchService.initCache(redisKey, searchResult);

        // 记录查询用时
        long end = System.currentTimeMillis();
        searchResult.setTime(end - start);
        return searchResult;
    }

    // 图片搜索接口
    @RequestMapping(value="/image", method=RequestMethod.POST)
    public SearchResult getSearchResult(@RequestParam("file") MultipartFile file,
                                        @RequestParam(value = "filter", required = false) String filter,
                                        @RequestParam(value = "page") int current,
                                        @RequestParam(value = "limit") int limit) {

        long start = System.currentTimeMillis();

        if (filter == null) {
            filter = "";
        }

        // 图片转文字
        String query = searchService.imageToQuery(file);
        // 从缓存中取数据query::filter::current::limit
        String redisKey = RedisKeyUtil.getResultKey(query, filter, current, limit);
        SearchResult cacheResult = searchService.getCache(redisKey);
        if (cacheResult != null) {
            long end = System.currentTimeMillis();
            cacheResult.setTime(end - start);
            return cacheResult;
        }

        SearchResult searchResult = searchService.findDocsByQuery(query, filter, current, limit);

        // 存入缓存
        searchService.initCache(redisKey, searchResult);

        long end = System.currentTimeMillis();
        searchResult.setTime(end - start);

        return searchResult;
    }

    //提示词搜索接口
    @RequestMapping(value="/prompt", method=RequestMethod.GET)
    public List<String> getPrompt(@RequestParam("query") String query)
    {
        // 取前十个提示词返回
        List<String> prompts = searchService.findPromptByQuery(query);
        return prompts.subList(0, Math.min(10, prompts.size()));
    }


}
