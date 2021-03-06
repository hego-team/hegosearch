package com.bytedance.hego.Controller;

import com.bytedance.hego.service.UserService;
import com.bytedance.hego.entity.JsonResult;
import com.bytedance.hego.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author lianmm
 * @version 0.1
 *
 */
@Controller
@RequestMapping(value = "")
//RequestMapping是将url请求映射到类或方法上。

public class LoginAction extends CommonAction {

    @Autowired
    private UserService userService;
    //自动装配接口属性。

    //处理get .../api这个请求
    @GetMapping(value = "")
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = getUserFromSession(request);
        //判断是否登录，登录则重定向到admin界面。
        if (user != null && user.getId() != null) {
            response.sendRedirect(request.getContextPath() + "/admin"); // 重定向
        }
        return new ModelAndView("index");
        //根据index文件构造界面。
    }

    @PostMapping(value = "login")
    @ResponseBody
    public JsonResult login(String name, String password, HttpServletRequest request) {
        if (StringUtils.isEmpty(name)) {
            return new JsonResult(false, "用户名不能为空");
        } else if (StringUtils.isEmpty(password)) {
            return new JsonResult(false, "密码不能为空");
        }
//检验非空。

        User user = userService.handleLogin(name, password);
        //检索数据库中有无此项，有则返回json，无则返回错误。
        if (user != null) {
            JsonResult jsonResult = new JsonResult(true);
            jsonResult.put("user", user);
            HttpSession session = request.getSession(true);
            session.setAttribute("user", user);
            session.setMaxInactiveInterval(12 * 60 * 60); // 设置过期时间为12h
            return jsonResult;
        }
        return new JsonResult(false, "用户名或密码错误");
    }

    @PostMapping(value = "register")
    @ResponseBody
    public JsonResult register(User user, HttpServletRequest request) {
        JsonResult jsonResult = verifyUserForm(user);
        //注册时检验非空。

        if (!jsonResult.getSuccess()) {
            return jsonResult;
        }
        if(userService.findByName(user.getName())!=null){
            return new JsonResult(false, "该用户名已被注册！");
        };user.setRole(0);//通过注册只能添加0级普通用户。
        user = userService.save(user); // 返回保存的对象
        if (user != null && user.getId() != null) {
            HttpSession session = request.getSession(true);
            session.setAttribute("user", user);
            jsonResult.put("user", user);
            return jsonResult;
        } else {
            return new JsonResult(false, "注册失败");
        }
    }

    @GetMapping(value = "logout")
    @ResponseBody
    public JsonResult logout(Integer id, HttpServletRequest request) {
        if (id == null) {
            User user = getUserFromSession(request);
            if (user == null) {
                return new JsonResult(false, "请先登录");
            }
            else{id=user.getId();}
        }

        HttpSession session = request.getSession(true);
        if (session.getAttribute("user") != null) {
            session.removeAttribute("user");
        }

        return new JsonResult(true);
    }

    @GetMapping(value = "admin")
    public ModelAndView admin(HttpServletRequest request, HttpServletResponse response) {
        User user = getUserFromSession(request);
        if (user == null) {
            try {
                response.sendRedirect(request.getContextPath() + ""); // 重定向至登录页
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ModelAndView("admin");
    }

}
