<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 2021/8/29
  Time: 13:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>提交请求</title>
  </head>
  <body>
  <form action="${pageContext.request.contextPath}/hello" method="post">
    <input type="text" name="method">
    <input type="submit">
  </form>
  </body>
</html>
