package com.mirror.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * @author mirror
 */
@WebServlet(urlPatterns = "/hi")
public class RedirectServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
     String name = req.getParameter("name");
     String redirectToUrl = "/hello" + (name == null ? "" : "?name="+name);
        try {
            resp.sendRedirect(redirectToUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
