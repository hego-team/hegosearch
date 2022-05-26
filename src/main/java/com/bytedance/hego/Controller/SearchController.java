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

        String query = searchService.imageToQuery(file);
        SearchResult searchResult = searchService.findDocsByQuery(query, "", current);
        return searchResult;
    }
}
