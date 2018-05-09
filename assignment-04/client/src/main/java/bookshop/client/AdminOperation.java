package bookshop.client;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum AdminOperation implements OperationI {
    ADD_BOOK("addBook"),
    LIST_OPERATIONS("listOperations");

    private final String text;

    AdminOperation(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public static String getList() {
        return Arrays.asList(AdminOperation.values()).stream().map(Object::toString).collect(Collectors.joining("\n"));
    }
}
