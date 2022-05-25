package com.bytedance.hego.Controller;

import com.bytedance.hego.entity.SearchResult;
import com.bytedance.hego.service.SearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/result")
public class SearchController {

    @Resource
    private SearchService searchService;

    @GetMapping("/{query}")
    public SearchResult getSearchResult(@PathVariable("query") String query) {
        long start = System.currentTimeMillis();
        SearchResult searchResult = searchService.findDocsByQuery(query);
        long end = System.currentTimeMillis();
        // 记录查询用时
        searchResult.setTime(end - start);

        return searchResult;
    }
}
