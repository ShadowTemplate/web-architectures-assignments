package bookshop.server;

import bookshop.common.BookTO;
import bookshop.common.CartTO;
import bookshop.common.OperationTO;
import bookshop.common.ShopBeanI;
import bookshop.server.db.Book;
import bookshop.server.db.Operation;
import bookshop.server.db.User;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Stateless
@Remote(ShopBeanI.class)
public class ShopBean implements ShopBeanI {

    @PersistenceContext(unitName = "BookshopPU")
    private EntityManager entityManager;

    public ShopBean() {

    }

    @Override
    public Boolean addBook(String adminPassword, String bookTitle, int bookPrice) {
        return tryFunction(() -> {
            if (adminPassword.equals(Constants.ADMIN_PASSWORD)) {
                entityManager.persist(new Book(bookTitle, bookPrice));
                return true;
            } else {
                return false;
            }
        });
    }

    @Override
    public List<BookTO> listBooks() {
        return tryFunction(() -> entityManager.createQuery("select e from Book e", Book.class).getResultList().stream()
                .map(book -> new BookTO(book.getId(), book.getTitle(), book.getPrice())).collect(Collectors.toList()));
    }

    @Override
    public List<OperationTO> listOperations(String adminPassword) {
        return tryFunction(() -> {
            if (!adminPassword.equals(Constants.ADMIN_PASSWORD)) {
                return null;
            }
            return entityManager.createQuery("select e from Operation e", Operation.class).getResultList().stream()
                    .map(op -> new OperationTO(op.getId(), op.getUsername(), op.getBooksIds())).collect(Collectors.toList());
        });
    }

    @Override
    public Boolean registerUser(String username, String password) {
        return tryFunction(() -> {
            entityManager.persist(new User(username, password));
            return true;
        });
    }

    @Override
    public CartTO getCart(String username, String password) {
        return tryFunction(() -> {
            List<User> users = entityManager.createQuery("select e from User e where e.username=:user and e.password=:pass", User.class)
                    .setParameter("user", username)
                    .setParameter("pass", password)
                    .getResultList();
            return users.isEmpty() ? null : new CartTO();
        });
    }

    @Override
    public CartTO addToCart(CartTO cart, Integer bookId) {
        cart.addBook(bookId);
        return cart;
    }

    @Override
    public CartTO buy(String username, CartTO cart) {
        return tryFunction(() -> {
            entityManager.persist(new Operation(username, cart));
            cart.empty();
            return cart;
        });
    }

    @Override
    public CartTO leave(CartTO cart) {
        cart.empty();
        return cart;
    }

    private <T> T tryFunction(Supplier<T> function) {
        try {
            return function.get();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }
}
