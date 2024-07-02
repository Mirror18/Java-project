package com.mirror;

import java.io.File;
import org.apache.catalina.Context;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

/**
 * @author mirror
 */
public class Main {

    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(Integer.getInteger("port", 8080));
        tomcat.getConnector();

        // 确认 webapp 目录存在
        File webappDir = new File("src/main/webapp");
        if (!webappDir.exists()) {
            System.out.println("Webapp directory does not exist: " + webappDir.getAbsolutePath());
            // 创建 webapp 目录
            webappDir.mkdirs();
            System.out.println("Webapp directory created: " + webappDir.getAbsolutePath());
        }

        // 确认 target/classes 目录存在
        File classesDir = new File("target/classes");
        if (!classesDir.exists()) {
            System.out.println("Classes directory does not exist: " + classesDir.getAbsolutePath());
            // 创建 classes 目录 (这通常不需要，应该通过 mvn clean install 来生成)
            classesDir.mkdirs();
            System.out.println("Classes directory created: " + classesDir.getAbsolutePath());
        }

        Context ctx = tomcat.addWebapp("", webappDir.getAbsolutePath());
        WebResourceRoot resources = new StandardRoot(ctx);
        resources.addPreResources(
                new DirResourceSet(resources, "/WEB-INF/classes", classesDir.getAbsolutePath(), "/"));
        ctx.setResources(resources);
        tomcat.start();
        tomcat.getServer().await();
    }
}
