<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 2021/8/31
  Time: 14:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>提交名字</title>
  </head>
  <body>
    <form action="${pageContext.request.contextPath}/encoding" method="post">
      <input type="text" name="name">
      <input type="submit">
    </form>
  </body>
</html>
