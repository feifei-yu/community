package com.comumunity.fei.controller;

import com.comumunity.fei.mapper.UserMapper;
import com.comumunity.fei.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @Autowired
    private UserMapper userMapper ;

    @GetMapping("/")
    public String index(HttpServletRequest request) {//主页面获取cookie
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie:cookies){
            if(cookie.getName().equals("token")){ //登录成功则可以找到cookie中的token
                String token = cookie.getValue();
                User user = userMapper .findByToken(token); //从数据库通过此token记录找到user信息
                if(user !=null){
                    request .getSession().setAttribute("user",user);//有就放入session，控制前端展示"我"界面
                }
                break;
            }
        }
        return "index";
    }
}
