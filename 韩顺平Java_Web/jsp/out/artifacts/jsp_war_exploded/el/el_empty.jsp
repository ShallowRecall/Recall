<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %><%--
  Created by IntelliJ IDEA.
  User: 韩顺平
  Version: 1.0
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>el empty的运算</title>
</head>
<body>
<h1>el empty的运算</h1>
<%
    request.setAttribute("k1", null);
    request.setAttribute("k2", "");
    request.setAttribute("k3", new Object[]{});
    request.setAttribute("k4", new ArrayList<>());
    request.setAttribute("k5", new HashMap<>());

    request.setAttribute("score", 70);
%>
k1是否为空= ${empty k1}<br/>
k2是否为空= ${empty k2}<br/>
k3是否为空= ${empty k3}<br/>
k4是否为空= ${empty k4}<br/>
k5是否为空= ${empty k5}<br/>
是否及格= ${score >= 60 ? "及格": "不及格"}
</body>
</html>
