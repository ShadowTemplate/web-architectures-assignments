<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="bookshop.web.SessionManager" %>
<%@ page import="bookshop.common.BookTO" %>
<%@ page import="java.util.List" %>
<%@ page import="bookshop.util.RemoteProxy" %>
<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>Bookshop Catalog</title>
</head>
<body>
<%
    String currUser = new SessionManager(session).getUser();
    if (currUser != null) {
%>
<br><em>Hi, <strong><%=currUser %>
</strong></em>
<form action="LogoutServlet" method="post">
    <a href="javascript:;" onclick="parentNode.submit();">Log out</a>
    <input type="hidden"/>
</form>
<br>
<%
    }
%>
<strong>Available books:</strong>
<br>
<ul>
    <%
        List<BookTO> books;
        try {
            books = RemoteProxy.getShop().listBooks();

            if (currUser != null) {
                for (BookTO book : books) {
    %>
    <li><%=book.getTitle() %> (<%=book.getPrice() %> $)</li>
    <form action="AddToCartServlet" method="post">
        <button name="bookId" value="<%=book.getId() %>">Add to cart</button>
    </form>
    <br>
    <%
        }
    } else {
        for (BookTO book : books) {
    %>
    <li>ID: <%=book.getId() %> - <%=book.getTitle() %> (<%=book.getPrice() %> $)</li>
    <br>
    <%
                }
            }
    %>
</ul>
<%
    } catch (Exception e) {
%>
<font color=red>Unable to load books list.<br></font>
<br>
<%
    }
%>

<%
    if (currUser != null) {
%>
<a href="${pageContext.request.contextPath}/cart.jsp">Review your cart and place an order</a>
<br><br>
<%
    }
%>
Go to <a href="${pageContext.request.contextPath}/">home page</a> or <a href="register.html">register now</a>
<br><br>
Go to <a href="${pageContext.request.contextPath}/admin/">administration panel</a>
</body>
</html>