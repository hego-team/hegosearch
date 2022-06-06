package com.bytedance.hego.Controller;

import com.bytedance.hego.service.TagsService;
import com.bytedance.hego.entity.JsonResult;
import com.bytedance.hego.entity.User;
import com.bytedance.hego.entity.Tags;
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
@RequestMapping(value = "tags")
public class TagsAction extends CommonAction {

    @Autowired
    private TagsService tagsService;

    @GetMapping(value = "list")
    @ResponseBody
    public JsonResult list(HttpServletRequest request) {
        User user = getUserFromSession(request);String owner="";
        if (user == null) {
            return new JsonResult(false, "请先登录");
        }
        else{
            owner=user.getName();
        }
        JsonResult jsonResult = new JsonResult(true);
        List<Tags> tagsList = tagsService.findAll(owner);

        jsonResult.put("tagsList", tagsList);
        return jsonResult;
    }
    //返回该用户的收藏表。

    @PostMapping(value = "save")
    public JsonResult saveAction(HttpServletRequest request,String oldname,String newname,String newurl) {
        // 编辑收藏条目。
        User user = getUserFromSession(request);
        if (user == null) {
            return new JsonResult(false, "请先登录");
        }

        Tags tags=tagsService.findByName(user.getName(),oldname);
        if(tags==null){return new JsonResult(false, "编辑对象不存在！"); }
        tags.setName(newname);
        tags.setUrl(newurl);
        tags =  tagsService.save(tags);
        if (tags != null && tags.getId() != null) {
            return new JsonResult(true);
        }
        return new JsonResult(false, "编辑失败");
    }

    @PostMapping(value = "add")
    public JsonResult add(String newname,String newurl,HttpServletRequest request) {
        // 新增条目。
        User user = getUserFromSession(request);
        if (user == null) {
            return new JsonResult(false, "请先登录");
        }
        Tags tags1=tagsService.findByName(user.getName(),newname);
        if(tags1!=null){return new JsonResult(false, "已存在同名条目");}
        Tags tags=new Tags();
        tags.setName(newname);tags.setUrl(newurl);tags.setOwner(user.getName());
        tags =  tagsService.add(tags);
        if (tags != null && tags.getId() != null) {
            return new JsonResult(true,"已添加到收藏夹");
        }
        return new JsonResult(false, "保存失败");
    }

    @DeleteMapping(value = "delete")
    public JsonResult delete(String name,Integer id, HttpServletRequest request) {
        User user = getUserFromSession(request);String owner="";
        if (user == null) {
            return new JsonResult(false, "请先登录");
        }
        else{
            owner=user.getName();
        }
        if(id==null){
        Tags tags=tagsService.findByName(owner,name);
        if(tags==null){return new JsonResult(false, "删除目标不存在！");}
        id=tags.getId();
        if (id == null) {
            return new JsonResult(false, "操作错误");
        }}
        boolean res = tagsService.delete(id);
        return new JsonResult(res, res ? null : "删除失败");
    }

}
