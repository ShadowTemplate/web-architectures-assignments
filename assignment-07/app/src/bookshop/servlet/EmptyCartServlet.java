package bookshop.servlet;

import bookshop.util.RemoteProxy;
import bookshop.common.CartTO;
import bookshop.web.Pages;
import bookshop.web.SessionAttribute;
import bookshop.web.SessionManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/EmptyCartServlet")
public class EmptyCartServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        SessionManager sessionManager = new SessionManager(request.getSession());
        CartTO cartTO = sessionManager.getCart();
        if (cartTO == null) {
            response.sendRedirect(Pages.LOGIN);
        } else {
            try {
                sessionManager.setAttribute(SessionAttribute.CART, RemoteProxy.getShop().leave(cartTO));
                response.sendRedirect(Pages.CART);
            } catch (Exception ex) {
                throw new ServletException(ex);
            }
        }
    }
}