package bookshop.servlet.session;

import bookshop.util.RemoteProxy;
import bookshop.common.OperationTO;
import bookshop.web.Parameters;
import bookshop.web.Pages;
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
import java.util.List;

@WebServlet("/AdminLoginServlet")
public class AdminLoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String password = request.getParameter(Parameters.PASSWORD);
        try {
            List<OperationTO> operationTOs = RemoteProxy.getShop().listOperations(password);
            if (operationTOs == null) {
                RequestDispatcher rd = getServletContext().getRequestDispatcher(Pages.ADMIN_LOGIN);
                PrintWriter out = response.getWriter();
                out.println("<font color=red>Log in failed: wrong password.<br></font>");
                rd.include(request, response);
            } else {
                HttpSession session = request.getSession();
                new SessionManager(session).setAttribute(SessionAttribute.ADMIN_PASSWORD, password);
                session.setMaxInactiveInterval(SessionManager.TIME_TO_LIVE);
                response.sendRedirect(Pages.ADMIN_ROOT);
            }
        } catch (Exception ex) {
            throw new ServletException(ex.getMessage());
        }
    }
}
