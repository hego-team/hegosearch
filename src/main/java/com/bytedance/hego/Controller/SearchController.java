package com.bytedance.hego.Controller;

import com.bytedance.hego.entity.SearchResult;
import com.bytedance.hego.service.SearchService;
import com.bytedance.hego.util.RedisServiceUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@RequestMapping("/search")
public class SearchController {

    @Resource
    private SearchService searchService;

    @Resource
    private RedisServiceUtil redisServiceUtil;

    // 文字搜索接口
    @RequestMapping(value="/text", method=RequestMethod.GET)
    public SearchResult getSearchResult(@RequestParam("query") String query,
                                        @RequestParam(value = "filter", required = false) String filter,
                                        @RequestParam(value = "page") int current) {
        if (filter == null) {
            filter = "";
        }

        long start = System.currentTimeMillis();
        Object result = redisServiceUtil.get(query);
        if(result != null){
            return (SearchResult)result;
        }else {
            SearchResult searchResult = searchService.findDocsByQuery(query, filter, current);
            // 记录查询用时
            long end = System.currentTimeMillis();
            searchResult.setTime(end - start);
            redisServiceUtil.set(query,searchResult);
            return searchResult;
        }
    }

    // 图片搜索接口
    @RequestMapping(value="/image", method=RequestMethod.POST)
    public SearchResult getSearchResult(@RequestParam("file") MultipartFile file,
                                  @RequestParam(value = "page") int current) {
        long start = System.currentTimeMillis();

        String query = searchService.imageToQuery(file);
        System.out.println(query);
        SearchResult searchResult = searchService.findDocsByQuery(query, "", current);

        long end = System.currentTimeMillis();
        searchResult.setTime(end - start);
        return searchResult;
    }
}
