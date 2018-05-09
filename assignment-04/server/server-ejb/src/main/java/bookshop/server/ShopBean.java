package bookshop.server;

import bookshop.common.BookTO;
import bookshop.common.OperationTO;
import bookshop.common.ShopBeanI;
import bookshop.server.db.Book;
import bookshop.server.db.Operation;
import bookshop.server.db.User;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Stateless
@Remote(ShopBeanI.class)
public class ShopBean implements ShopBeanI {

    @PersistenceContext(unitName = "BookshopPU")
    private EntityManager entityManager;

    public ShopBean() {

    }

    @Override
    public void addBook(String adminPassword, String bookTitle, int bookPrice) {
        if (adminPassword.equals(Constants.ADMIN_PASSWORD)) {
            entityManager.persist(new Book(bookTitle, bookPrice));
        }
    }

    @Override
    public List<BookTO> listBooks() {
        List<BookTO> bookTos = new ArrayList<>();
        for (Book book : entityManager.createQuery("select e from Book e", Book.class).getResultList()) {
            bookTos.add(new BookTO(book.getId(), book.getTitle(), book.getPrice()));
        }
        return bookTos;
    }

    @Override
    public List<OperationTO> listOperations(String adminPassword) {
        if (!adminPassword.equals(Constants.ADMIN_PASSWORD)) {
            return null;
        }
        List<OperationTO> operationTOs = new ArrayList<>();
        for (Operation op : entityManager.createQuery("select e from Operation e", Operation.class).getResultList()) {
            operationTOs.add(new OperationTO(op.getId(), op.getUsername(), op.getBooksIds()));
        }
        return operationTOs;
    }

    @Override
    public void registerUser(String username, String password) {
        entityManager.persist(new User(username, password));
    }
}
