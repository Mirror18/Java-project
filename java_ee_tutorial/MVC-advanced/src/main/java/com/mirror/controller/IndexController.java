package com.mirror.controller;

import com.mirror.bean.User;
import com.mirror.framework.GetMapping;
import com.mirror.framework.ModelAndView;
import jakarta.servlet.http.HttpSession;


/**
 * @author mirror
 */
public class IndexController {

    @GetMapping("/")
    public ModelAndView index(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return new ModelAndView("/index.html", "user", user);
    }

    @GetMapping("/hello")
    public ModelAndView hello(String name) {
        if (name == null) {
            name = "World";
        }
        return new ModelAndView("/hello.html", "name", name);
    }
}
