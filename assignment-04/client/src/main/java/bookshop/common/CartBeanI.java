package bookshop.common;

public interface CartBeanI {

    CartTO getCart(String username, String password);
    CartTO addToCart(CartTO cart, Integer bookId);
    CartTO buy(CartTO cart);
    CartTO leave(CartTO cart);
}
