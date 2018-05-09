package bookshop.common;

import java.io.Serializable;

public class BookTO implements Serializable {

    private int id;
    private String title;
    private int price;

    public BookTO(int id, String title, int price) {
        this.id = id;
        this.title = title;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", price=" + price +
                '}';
    }
}