package bookshop.client;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum ClientOperation implements OperationI {
    REGISTER_USER("registerUser"),
    GET_CART("getCart"),
    LIST_BOOKS("listBooks"),
    ADD_TO_CART("addToCart"),
    BUY("buy"),
    LEAVE("leave");

    private final String text;

    ClientOperation(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public static String getList() {
        return Arrays.asList(ClientOperation.values()).stream().map(Object::toString).collect(Collectors.joining("\n"));
    }
}