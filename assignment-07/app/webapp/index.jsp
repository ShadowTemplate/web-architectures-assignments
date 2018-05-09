<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="bookshop.web.SessionManager" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>Bookshop</title>
</head>
<body>
<br><strong>Welcome to our bookshop!</strong>
<br><br>
<a href="catalog.jsp">Take a look at our catalog</a>
<br><br>
<%
    String currUser = new SessionManager(session).getUser();
    if (currUser != null) {
%>
<em>Hi, <strong><%=currUser %></strong></em>
<form action="LogoutServlet" method="post">
    <a href="javascript:;" onclick="parentNode.submit();">Log out</a>
    <input type="hidden"/>
</form>
<%
} else {
%>
<em>Already registered?</em><br><a href="login.html">Log in</a><br>
<%
    }
%>
<br>
Or <a href="register.html">register now</a>
<br><br>
Go to <a href="${pageContext.request.contextPath}/admin/">administration panel</a>
</body>
</html>