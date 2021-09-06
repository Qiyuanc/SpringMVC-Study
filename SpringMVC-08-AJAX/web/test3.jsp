<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 2021/9/5
  Time: 17:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>验证登录</title>
    <script src="${pageContext.request.contextPath}/statics/jquery-3.6.0.js"></script>
    <script>
        function AJAX3(){
            $.post({
                url:"${pageContext.request.contextPath}/ajax3",
                data:{'username':$("#name").val()},
                success: function (data) {
                    if (data.toString()=='OK'){
                        $("#userInfo").css("color","green");
                    }else {
                        $("#userInfo").css("color","red");
                    }
                    $("#userInfo").html(data);
                }
            });
        }
    </script>
</head>
<body>
<p>
    用户名:<input type="text" id="name" onblur="AJAX3()"/>
    <span id="userInfo"></span>
</p>
</body>
</html>
