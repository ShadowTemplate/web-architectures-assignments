package bookshop.common;

import java.io.Serializable;

public class OperationTO implements Serializable {

    private int id;
    private String username;
    private String booksIds;

    public OperationTO(int id, String username, String booksIds) {
        this.id = id;
        this.username = username;
        this.booksIds = booksIds;
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
