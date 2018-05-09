package bookshop.servlet.session;

import bookshop.util.RemoteProxy;
import bookshop.common.CartTO;
import bookshop.web.Parameters;
import bookshop.web.Pages;
import bookshop.web.SessionAttribute;
import bookshop.web.SessionManager;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String user = request.getParameter(Parameters.USER);
        String password = request.getParameter(Parameters.PASSWORD);
        try {
            CartTO cart = RemoteProxy.getShop().getCart(user, password);
            if (cart == null) {
                RequestDispatcher rd = getServletContext().getRequestDispatcher(Pages.LOGIN);
                PrintWriter out = response.getWriter();
                out.println("<font color=red>Log in failed. Either username or password is wrong.<br></font>");
                rd.include(request, response);
            } else {
                HttpSession session = request.getSession();
                SessionManager sessionManager = new SessionManager(session);
                sessionManager.setAttribute(SessionAttribute.USER, user);
                sessionManager.setAttribute(SessionAttribute.CART, cart);
                session.setMaxInactiveInterval(SessionManager.TIME_TO_LIVE);
                response.sendRedirect(Pages.ROOT);
            }
        } catch (Exception ex) {
            throw new ServletException(ex.getMessage());
        }
    }
}
