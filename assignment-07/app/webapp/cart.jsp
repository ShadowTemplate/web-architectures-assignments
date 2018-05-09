<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="bookshop.web.SessionManager" %>
<%@ page import="bookshop.common.CartTO" %>
<%@ page import="java.util.Set" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>Bookshop Cart</title>
</head>
<body>
<br>
<%
    String currUser = new SessionManager(session).getUser();
    CartTO cart = new SessionManager(session).getCart();
%>
<em>Hi, <strong><%=currUser %>
</strong></em>
<form action="LogoutServlet" method="post">
    <a href="javascript:;" onclick="parentNode.submit();">Log out</a>
    <input type="hidden"/>
</form>

<br><strong>Your cart:</strong>
<br>
<ul>
    <%
        Set<Integer> booksIds = cart.getBooksIds();
        if (booksIds != null) {
            for (Integer id : booksIds) {
    %>
    <li>ID: <%=id %>
    </li>
    <%
            }
        }
    %>
</ul>

<%
    if (!cart.getBooksIds().isEmpty()) {
%>
<br>
<form action="EmptyCartServlet" method="post">
    <button name="emptyCart" value="">Empty cart</button>
</form>
<br>
<form action="BuyServlet" method="post">
    <button name="buy" value="">Place order</button>
</form>
<%
    }
%>
<br><br>
Go to <a href="${pageContext.request.contextPath}/">home page</a> or go back to the <a
        href="${pageContext.request.contextPath}/catalog.jsp">catalog</a>
<br><br>
Go to <a href="${pageContext.request.contextPath}/admin/">administration panel</a>
</body>
</html>