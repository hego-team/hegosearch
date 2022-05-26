package com.bytedance.hego.Controller;

import com.bytedance.hego.entity.SearchResult;
import com.bytedance.hego.service.SearchService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
public class SearchController {

    @Resource
    private SearchService searchService;

    @RequestMapping(value="/result",method= RequestMethod.GET)
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
}
