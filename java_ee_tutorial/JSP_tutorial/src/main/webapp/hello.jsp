<%--
  Created by IntelliJ IDEA.
  User: mirror
  Date: 2024/6/24
  Time: 23:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Hello World - JSP</title>
    <%@ page import="java.io.*" %>
    <%@ page import="java.util.*" %>
</head>
<body>
<%-- JSP Comment --%>
<h1>Hello World!</h1>
<p>
    <%
        out.println("Your IP address is ");
    %>
    <span style="color:red">
        <%= request.getRemoteAddr() %>
    </span>
</p>
</body>
</html>
