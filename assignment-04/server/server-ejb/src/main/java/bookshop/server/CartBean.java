package bookshop.server;

import bookshop.common.CartTO;
import bookshop.common.CartBeanI;
import bookshop.server.db.Operation;
import bookshop.server.db.User;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


@Stateful
@Remote(CartBeanI.class)
public class CartBean implements CartBeanI {

    private String username;
    private CartTO cart;

    @PersistenceContext(unitName = "BookshopPU")
    private EntityManager entityManager;

    public CartBean() {

    }

    @Override
    public CartTO getCart(String username, String password) {
        List<User> users = entityManager.createQuery("select e from User e where e.username=:user and e.password=:pass", User.class)
                .setParameter("user", username)
                .setParameter("pass", password)
                .getResultList();
        if (users.isEmpty()) {
            return null;
        }
        if (this.username == null || !this.username.equals(username)) {
            this.username = username;
            this.cart = new CartTO();
        }

        return this.cart;
    }

    @Override
    public CartTO addToCart(CartTO cart, Integer bookId) {
        this.cart = cart;
        this.cart.addBook(bookId);
        return this.cart;
    }

    @Override
    public CartTO buy(CartTO cart) {
        entityManager.persist(new Operation(this.username, this.cart));
        this.cart = cart;
        this.cart.empty();
        return this.cart;
    }

    @Override
    public CartTO leave(CartTO cart) {
        this.cart = cart;
        this.cart.empty();
        return this.cart;
    }
}
