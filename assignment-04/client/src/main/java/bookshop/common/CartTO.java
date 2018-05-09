package bookshop.common;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class CartTO implements Serializable {

    private Set<Integer> booksIds;

    public CartTO() {
        booksIds = new HashSet<>();
    }

    public void addBook(Integer bookId) {
        this.booksIds.add(bookId);
    }

    public Set<Integer> getBooksIds() {
        return this.booksIds;
    }

    public void empty() {
        this.booksIds.clear();
    }

}
