package bookshop.common;

import java.util.List;

public interface ShopBeanI {

    Boolean addBook(String adminPassword, String bookTitle, int bookPrice);
    List<BookTO> listBooks();
    List<OperationTO> listOperations(String adminPassword);
    Boolean registerUser(String username, String password);
    CartTO getCart(String username, String password);
    CartTO addToCart(CartTO cart, Integer bookId);
    CartTO buy(String username, CartTO cart);
    CartTO leave(CartTO cart);
}
