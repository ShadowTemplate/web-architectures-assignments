package bookshop.web;

public enum SessionAttribute {
    ADMIN_PASSWORD("admin_password"),
    CART("cart"),
    USER("user");

    private String value;

    SessionAttribute(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
