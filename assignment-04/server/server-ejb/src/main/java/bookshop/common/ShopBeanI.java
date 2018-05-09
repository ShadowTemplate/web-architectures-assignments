package bookshop.common;

import java.util.List;

public interface ShopBeanI {

    void addBook(String adminPassword, String bookTitle, int bookPrice);
    List<BookTO> listBooks();
    List<OperationTO> listOperations(String adminPassword);
    void registerUser(String username, String password);
}
