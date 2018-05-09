<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="bookshop.web.SessionManager" %>
<%@ page import="bookshop.common.OperationTO" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>Admin page</title>
</head>
<body>
<br>
<form action="${pageContext.request.contextPath}/AddBookServlet" method="post">
    Title: <input type="text" name="title">
    <br>
    Price: <input type="number" name="price" min="1" max="999">
    <br><br>
    <input type="submit" value="Add book">
</form>

<br>
<%
    List<OperationTO> operations = new SessionManager(session).getOperations();
    if (operations == null) {
        response.sendRedirect("/admin_login.html");
    }
%>
<br><strong>Operations list:</strong>
<br>
<ul>
    <%
        for (OperationTO op : operations) {
    %>
    <li><%=op.toString() %></li>
    <%
        }
    %>
</ul>
<br><br>
Go to <a href="${pageContext.request.contextPath}/">home page</a>
</body>
</html>