<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 2021/9/6
  Time: 16:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>成功页面</title>
</head>

<h1>成功页面</h1>
<hr>

<body>
当前登录用户为：${user}
<a href="${pageContext.request.contextPath}/user/logout">注销</a>
</body>
</html>
