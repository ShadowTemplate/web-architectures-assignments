package bookshop.servlet.session;

import bookshop.web.Pages;
import bookshop.web.SessionAttribute;
import bookshop.web.SessionManager;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session != null) {
            SessionManager sessionManager = new SessionManager(session);
            sessionManager.removeAttribute(SessionAttribute.USER);
            sessionManager.removeAttribute(SessionAttribute.CART);
        }
        response.sendRedirect(Pages.ROOT);
    }
}
