<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 2021/9/5
  Time: 16:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>获取数据</title>
    <script src="${pageContext.request.contextPath}/statics/jquery-3.6.0.js"></script>
    <script>
        function AJAX2(){
            console.log("点击按钮")
            $.post("${pageContext.request.contextPath}/ajax2",function (data) {
                console.log(data)
                /* 把拿到的数据拼接到页面上 吐了！ */
                var html="";
                for (var i = 0; i <data.length ; i++) {
                    html+= "<tr>" +
                        "<td>" + data[i].name + "</td>" +
                        "<td>" + data[i].age + "</td>" +
                        "<td>" + data[i].gender + "</td>" +
                        "</tr>"
                }
                $("#content").html(html);
            });
        }
    </script>
</head>
<body>

<input type="button" id="btn" value="获取数据" onclick="AJAX2()"/>
<table width="80%" align="center">
    <tr>
        <td>姓名</td>
        <td>年龄</td>
        <td>性别</td>
    </tr>
    <tbody id="content">
        <%--通过请求获取！--%>
    </tbody>
</table>

</body>
</html>
