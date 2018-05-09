package bookshop.servlet;

import bookshop.util.RemoteProxy;
import bookshop.common.CartTO;
import bookshop.common.ShopBeanI;
import bookshop.web.Pages;
import bookshop.web.Parameters;
import bookshop.web.SessionAttribute;
import bookshop.web.SessionManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String user = request.getParameter(Parameters.USER);
        String password = request.getParameter(Parameters.PASSWORD);
        try {
            ShopBeanI shop = RemoteProxy.getShop();
            shop.registerUser(user, password);
            HttpSession session = request.getSession();
            SessionManager sessionManager = new SessionManager(session);
            sessionManager.setAttribute(SessionAttribute.USER, user);
            CartTO cart = shop.getCart(user, password);
            sessionManager.setAttribute(SessionAttribute.CART, cart);
            session.setMaxInactiveInterval(SessionManager.TIME_TO_LIVE);
            response.sendRedirect(Pages.ROOT);
        } catch (Exception ex) {
            RequestDispatcher rd = getServletContext().getRequestDispatcher(Pages.REGISTER);
            PrintWriter out = response.getWriter();
            out.println("<font color=red>Registration failed. Either the server is down or another account with " +
                    "the same username exists.<br></font>");
            rd.include(request, response);
        }
    }
}
