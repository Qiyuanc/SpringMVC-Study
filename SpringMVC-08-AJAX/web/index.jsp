<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 2021/9/5
  Time: 13:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>AJAX测试</title>
    <script src="${pageContext.request.contextPath}/statics/jquery-3.6.0.js"></script>
    <script>
      /* $ 符号能点出东西来说明 jQuery 导入成功了 */
      function ATEST(){
        /* post 方法封装了 ajax 方法*/
        $.post({
          /* 请求的路径 */
          url: "${pageContext.request.contextPath}/ajax",
          /* 携带的数据 */
          /* 这里将 username 的值命名为 name 发送，后端接收的是 name */
          data: {'name': $("#username").val()},
          /* 成功后执行回调函数 */
          success: function (data, status) {
            alert(data);
            alert(status);
          }
        });
      }
    </script>
  </head>
  <body>
  <%-- onblur 失去焦点触发 ATEST 事件--%>
  用户名:<input type="text" id="username" onblur="ATEST()"/>
  </body>
</html>
