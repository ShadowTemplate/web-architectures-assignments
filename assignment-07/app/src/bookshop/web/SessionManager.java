package bookshop.web;

import bookshop.common.CartTO;
import bookshop.common.OperationTO;
import bookshop.util.RemoteProxy;

import javax.naming.NamingException;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

public class SessionManager {

    public static final int TIME_TO_LIVE = 30 * 60; // 30 minutes

    private HttpSession session;

    public SessionManager(HttpSession session) {
        this.session = session;
    }

    public String getAdminPassword() {
        Object adminPassword = session.getAttribute(SessionAttribute.ADMIN_PASSWORD.value());
        return adminPassword == null ? null : (String) adminPassword;
    }

    public String getUser() {
        Object user = session.getAttribute(SessionAttribute.USER.value());
        return user == null ? null : (String) user;
    }

    public CartTO getCart() {
        Object cart = session.getAttribute(SessionAttribute.CART.value());
        return cart == null ? null : (CartTO) cart;
    }

    public List<OperationTO> getOperations() {
        String adminPassword = getAdminPassword();
        try {
            return adminPassword == null ? null : RemoteProxy.getShop().listOperations(adminPassword);
        } catch (NamingException e) {
            return new ArrayList<>();
        }
    }

    public void setAttribute(SessionAttribute attribute, Object value) {
        session.setAttribute(attribute.value(), value);
    }

    public void removeAttribute(SessionAttribute attribute) {
        session.removeAttribute(attribute.value());
    }
}
