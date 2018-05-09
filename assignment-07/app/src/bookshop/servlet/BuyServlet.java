package bookshop.servlet;

import bookshop.util.RemoteProxy;
import bookshop.common.CartTO;
import bookshop.common.ShopBeanI;
import bookshop.web.Pages;
import bookshop.web.SessionAttribute;
import bookshop.web.SessionManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/BuyServlet")
public class BuyServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        SessionManager sessionManager = new SessionManager(request.getSession());
        String user = sessionManager.getUser();
        CartTO cartTO = sessionManager.getCart();
        if (user == null || cartTO == null) {
            response.sendRedirect(Pages.LOGIN);
        } else {
            try {
                ShopBeanI shop = RemoteProxy.getShop();
                shop.buy(user, cartTO);
                sessionManager.setAttribute(SessionAttribute.CART, shop.leave(cartTO));
                response.sendRedirect(Pages.CART);
            } catch (Exception ex) {
                throw new ServletException(ex);
            }
        }
    }
}