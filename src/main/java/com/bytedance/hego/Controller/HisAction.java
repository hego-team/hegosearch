package com.bytedance.hego.Controller;

import com.bytedance.hego.service.HisService;
import com.bytedance.hego.entity.JsonResult;
import com.bytedance.hego.entity.User;
import com.bytedance.hego.entity.His;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author lianmm
 * @version 0.1
 *
 */
@RestController
@RequestMapping(value = "his")
public class HisAction extends CommonAction {

    @Autowired
    private HisService hisService;

    @GetMapping(value = "list/default")
    @ResponseBody
    public JsonResult listdefault(HttpServletRequest request) {
        User user = getUserFromSession(request);
        String owner="";
        if (user == null) {
            return new JsonResult(false);
        }

        else{
            owner=user.getName();
        }

        JsonResult jsonResult = new JsonResult(true);
        List<His> hisList = hisService.findAll(owner);

        jsonResult.put("hisList", hisList);
        return jsonResult;
    }

    @GetMapping(value = "list/sort")
    @ResponseBody
    public JsonResult listsort(HttpServletRequest request) {
        User user = getUserFromSession(request);String owner="";
        if (user == null) {
            return new JsonResult(false);
        }
        else{
            owner=user.getName();
        }
        JsonResult jsonResult = new JsonResult(true);
        List<His> hisList = hisService.findAll(owner);
        hisList=hisService.sortHis(hisList);
        jsonResult.put("hisList", hisList);
        return jsonResult;
    }

    //返回该用户的收藏表。

   
    @PostMapping(value = "add")
    public JsonResult add(String newcontent,HttpServletRequest request) {
        // 新增条目。
        User user = getUserFromSession(request);
        if (user == null) {
            return new JsonResult(false);
        }
        His his1=hisService.findByContent(user.getName(),newcontent);
        if(his1!=null){
            his1.setTimes(his1.getTimes()+1);
            hisService.save(his1);
            return new JsonResult(true);
        }
        His his=new His();
        his.setContent(newcontent);his.setTimes(1);his.setOwner(user.getName());
        his =  hisService.add(his);
        if (his != null && his.getId() != null) {
            return new JsonResult(true);
        }
        return new JsonResult(false);
    }

    @DeleteMapping(value = "delete")
    public JsonResult delete(String content,Integer id, HttpServletRequest request) {
        User user = getUserFromSession(request);String owner="";
        if (user == null) {
            return new JsonResult(false);
        }
        else{
            owner=user.getName();
        }
        if(id==null){
            His his=hisService.findByContent(owner,content);
            if(his==null){return new JsonResult(false, "删除目标不存在！");}
            id=his.getId();
            if (id == null) {
                return new JsonResult(false, "操作错误");
            }}
        boolean res = hisService.delete(id);
        return new JsonResult(res, res ? null : "删除失败");
    }

}
