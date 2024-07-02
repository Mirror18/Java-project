package com.mirror.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author mirror
 */
@WebServlet(urlPatterns = "/")
public class IndexServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String user = (String) req.getSession().getAttribute("user");
        String lang = parseLanguageFromCookie(req);
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter pw = resp.getWriter();
        if ("zh".equals(lang)) {
            pw.write("<h1>你好, " + (user != null ? user : "Guest") + "</h1>");
            if (user == null) {
                pw.write("<p><a href=\"/signin\">登录</a></p>");
            } else {
                pw.write("<p><a href=\"/signout\">登出</a></p>");
            }
        } else {
            pw.write("<h1>Welcome, " + (user != null ? user : "Guest") + "</h1>");
            if (user == null) {
                pw.write("<p><a href=\"/signin\">Sign In</a></p>");
            } else {
                pw.write("<p><a href=\"/signout\">Sign Out</a></p>");
            }
        }
        pw.write("<p><a href=\"/pref?lang=en\">English</a> | <a href=\"/pref?lang=zh\">中文</a>");
        pw.flush();
    }

    private String parseLanguageFromCookie(HttpServletRequest req) {
        // 获取请求附带的所有Cookie:
        Cookie[] cookies = req.getCookies();
        // 如果获取到Cookie:
        if (cookies != null) {
            // 循环每个Cookie:
            for (Cookie cookie : cookies) {
                // 如果Cookie名称为lang:
                if ("lang".equals(cookie.getName())) {
                    // 返回Cookie的值:
                    return cookie.getValue();
                }
            }
        }
        // 返回默认值:
        return "en";
    }
}

