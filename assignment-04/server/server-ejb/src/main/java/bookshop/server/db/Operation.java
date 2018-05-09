package bookshop.server.db;

import bookshop.common.CartTO;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "OPERATIONS")
public class Operation {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    @Column(name = "username")
    private String username;

    @Column(name = "booksIds")
    private String booksIds;


    public Operation() {

    }

    public Operation(String username, CartTO cart) {
        this.username = username;
        List<String> ids = new ArrayList<>();
        for (Integer id : cart.getBooksIds()) {
            ids.add(id.toString());
        }
        this.booksIds = String.join(",", ids);
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getBooksIds() {
        return booksIds;
    }

    @Override
    public String toString() {
        return "Operation{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", booksIds='" + booksIds + '\'' +
                '}';
    }
}
