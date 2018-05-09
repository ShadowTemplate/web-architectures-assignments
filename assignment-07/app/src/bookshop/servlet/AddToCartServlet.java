package bookshop.servlet;

import bookshop.util.RemoteProxy;
import bookshop.common.CartTO;
import bookshop.web.Pages;
import bookshop.web.Parameters;
import bookshop.web.SessionAttribute;
import bookshop.web.SessionManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/AddToCartServlet")
public class AddToCartServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        SessionManager sessionManager = new SessionManager(request.getSession());
        String bookId = request.getParameter(Parameters.BOOK_ID);
        CartTO cartTO = sessionManager.getCart();
        if (cartTO == null) {
            response.sendRedirect(Pages.LOGIN);
        } else {
            try {
                CartTO newCart = RemoteProxy.getShop().addToCart(cartTO, Integer.parseInt(bookId));
                sessionManager.setAttribute(SessionAttribute.CART, newCart);
                response.sendRedirect(Pages.CATALOG);
            } catch (Exception ex) {
                throw new ServletException(ex);
            }
        }
    }
}