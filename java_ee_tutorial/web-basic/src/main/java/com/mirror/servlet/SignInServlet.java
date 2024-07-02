package com.mirror.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * @author mirror
 */
@WebServlet(urlPatterns = "/signin")
public class SignInServlet extends HttpServlet {
    //模拟数据库
    protected Map<String,String> users = Map.of("bob", "bob123", "alice", "alice123", "tom", "tomcat");
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
        resp.setContentType("text/html");
        try {
            PrintWriter pw = resp.getWriter();
            pw.write("<h1>Sign In</h1>");
            pw.write("<form action=\"/signin\" method=\"post\">");
            pw.write("<p>Username: <input name=\"username\"></p>");
            pw.write("<p>Password: <input name=\"password\" type=\"password\"></p>");
            pw.write("<p><button type=\"submit\">Sign In</button> <a href=\"/\">Cancel</a></p>");
            pw.write("</form>");
            pw.flush();
        }catch (IOException ignored){}

    }


    // POST请求时处理用户登录:
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("username");
        String password = req.getParameter("password");
        String expectedPassword = users.get(name.toLowerCase());
        if (expectedPassword != null && expectedPassword.equals(password)) {
            // 登录成功:
            req.getSession().setAttribute("user", name);
            resp.sendRedirect("/");
        } else {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}
