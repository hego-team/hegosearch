package com.bytedance.hego.Controller;

import com.bytedance.hego.entity.SearchResult;
import com.bytedance.hego.service.Impl.BtService;
import com.bytedance.hego.service.SearchService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {

    @Resource
    private SearchService searchService;

    @Resource
    private BtService btService;


    // 文字搜索接口
    @RequestMapping(value="/text", method=RequestMethod.GET)
    public SearchResult getSearchResult(@RequestParam("query") String query,
                                        @RequestParam(value = "filter", required = false) String filter,
                                        @RequestParam(value = "page") int current,
                                        @RequestParam(value = "limit") int limit) {
        if (filter == null) {
            filter = "";
        }

        long start = System.currentTimeMillis();
        SearchResult searchResult;

        // 判断用户输入语言
        if (query.matches("[\u4E00-\u9FA5]+")) {
            searchResult = searchService.findDocsByQuery(query, filter, current, limit);
        }
        else {
            String transQuery= btService.translate(query);
            searchResult = searchService.findDocsByQuery(transQuery, filter, current, limit);
        }

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

        String query = searchService.imageToQuery(file);
        SearchResult searchResult = searchService.findDocsByQuery(query, filter, current, limit);

        long end = System.currentTimeMillis();
        searchResult.setTime(end - start);
        return searchResult;
    }

    //提示词搜索接口
    @RequestMapping(value="/prompt", method=RequestMethod.GET)
    public List<String> getPrompt(@RequestParam("query") String query)
    {
        // 取前十个提示词返回
        return searchService.findPromptByQuery(query).subList(0, 10);
    }


}
