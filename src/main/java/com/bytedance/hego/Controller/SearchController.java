package com.bytedance.hego.Controller;

import com.bytedance.hego.entity.SearchResult;
import com.bytedance.hego.service.SearchService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@RequestMapping("/search")
public class SearchController {

    @Resource
    private SearchService searchService;

    // 文字搜索接口
    @RequestMapping(value="/text", method=RequestMethod.GET)
    public SearchResult getSearchResult(@RequestParam("query") String query,
                                        @RequestParam(value = "filter", required = false) String filter,
                                        @RequestParam(value = "page") int current) {
        if (filter == null) {
            filter = "";
        }

        long start = System.currentTimeMillis();
        SearchResult searchResult = searchService.findDocsByQuery(query, filter, current);
        // 记录查询用时
        long end = System.currentTimeMillis();
        searchResult.setTime(end - start);
        return searchResult;
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

    //提示词搜索接口
    @RequestMapping(value="/prompt", method=RequestMethod.GET)
    public SearchResult getSearchResult(@RequestParam("query") String query)
    {
        long start = System.currentTimeMillis();
        SearchResult searchResult = searchService.findPromptByQuery(query);
        // 记录查询前缀树用时
        long end = System.currentTimeMillis();
        searchResult.setTime(end - start);
        return searchResult;
    }

    //拼写检查功能
    @RequestMapping(value="/check", method=RequestMethod.GET)
    public SearchResult getSearchResult(@RequestParam("query") String query,
                                        @RequestParam(value="check") int check)
    {
        long start = System.currentTimeMillis();
        SearchResult searchResult = searchService.CheckByQuery(query);
        // 记录拼写检测用时
        long end = System.currentTimeMillis();
        searchResult.setTime(end - start);
        return searchResult;
    }

    //英文翻译功能
    @RequestMapping(value="/trans", method=RequestMethod.GET)
    public SearchResult getSearchResult(@RequestParam("query") String query,
                                        @RequestParam(value="trans") String trans)
    {
        long start = System.currentTimeMillis();
        SearchResult searchResult = searchService.TransByQuery(query);
        // 记录翻译用时
        long end = System.currentTimeMillis();
        searchResult.setTime(end - start);
        return searchResult;
    }

}
