package com.bytedance.hego.Controller;
import com.bytedance.hego.service.HisService;
import com.bytedance.hego.service.TagsService;
import com.bytedance.hego.service.UserService;
import com.bytedance.hego.entity.JsonResult;
import com.bytedance.hego.entity.Tags;
import com.bytedance.hego.entity.User;
import com.bytedance.hego.entity.His;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author lianmm
 * @version 0.1
 *
 */
@RestController
@RequestMapping(value = "user")
public class UserAction extends CommonAction {

    @Autowired
    private UserService userService;
    @Autowired
    private TagsService tagsService;
    @Autowired
    private HisService hisService;

/*    @GetMapping(value = "list")
    @ResponseBody
    public JsonResult list(HttpServletRequest request) {
        User user = getUserFromSession(request);
        if (user == null) {
            return new JsonResult(false, "请先登录");
        }

        JsonResult jsonResult = new JsonResult(true);
        List<User> userList = userService.findAll();
        jsonResult.put("userList", userList);
        return jsonResult;
    }*/

    @PostMapping(value = "save")
    public JsonResult add(User user,HttpServletRequest request) {
        // 新增或编辑用户
        User user1=getUserFromSession(request);
        user.setId(user1.getId());
        user =  userService.save(user);
        if (user != null && user.getId() != null) {
            return new JsonResult(true);
        }
        return new JsonResult(false, "保存失败");
    }

    @PutMapping(value = "change_password")
    public JsonResult changePassword(HttpServletRequest request, String oldPass, String newPass, String reNewPass) {
        User user = getUserFromSession(request);
        if (user == null) {
            return new JsonResult(false, "登录过期");
        }
        user = userService.findById(user.getId());
        if (user == null) {
            return new JsonResult(false, "用户不存在或已被删除");
        } else if (!user.getPassword().equals(oldPass)) {
            return new JsonResult(false, "原密码错误");
        }

        if (StringUtils.isEmpty(oldPass)) {
            return new JsonResult(false, "原密码不能为空");
        }

        if (StringUtils.isEmpty(newPass)) {
            return new JsonResult(false, "新密码不能为空");
        }

        if (!newPass.equals(reNewPass)) {
            return new JsonResult(false, "两次输入的密码不一致");
        }

        if (oldPass.equals(newPass)) {
            return new JsonResult(false, "新密码不能与旧密码相同");
        }

        boolean res = userService.updatePassword(user.getId(), newPass);
        return new JsonResult(res, res ? null : "修改失败");
    }

    @DeleteMapping(value = "delete")
    public JsonResult delete(Integer id, HttpServletRequest request) {
        User user = getUserFromSession(request);
        if (id == null) {
            if (user == null) {
                return new JsonResult(false, "请先登录");
            }
            else{id=user.getId();}
        }int i;boolean flag=true;

        if( tagsService.findAll(user.getName())!=null){
            List<Tags> myTags=tagsService.findAll(user.getName());
            for( i=0;i<myTags.size();i++){
                flag=tagsService.delete(myTags.get(i).getId());
                if(!flag){return new JsonResult(false,  "删除个人信息失败");}
            }
        }

            List<His> myHis = hisService.findAll(user.getName());
            for(i=0;i<myHis.size();i++){
                flag=hisService.delete(myHis.get(i).getId());
                if(!flag){return new JsonResult(false,  "删除个人信息失败");}
            }

        boolean res = userService.delete(id);
        HttpSession session = request.getSession(true);
        if (session.getAttribute("user") != null) {
            session.removeAttribute("user");
        }
        return new JsonResult(res, res ? null : "注销失败");
    }

}
