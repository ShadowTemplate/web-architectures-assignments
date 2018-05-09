package bookshop.servlet;

import bookshop.util.RemoteProxy;
import bookshop.web.Pages;
import bookshop.web.Parameters;
import bookshop.web.SessionManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/AddBookServlet")
public class AddBookServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String title = request.getParameter(Parameters.TITLE);
        String price = request.getParameter(Parameters.PRICE);
        String adminPassword = new SessionManager(request.getSession()).getAdminPassword();
        if (title != null && price != null && adminPassword != null) {
            try {
                RemoteProxy.getShop().addBook(adminPassword, title, Integer.parseInt(price));
            } catch (Exception ex) {
                throw new ServletException(ex);
            }
        }
        response.sendRedirect(Pages.ADMIN_ROOT);
    }
}