package bookshop.client;

public class Constants {
    public static final String DEFAULT_SHOP_EJB;
    public static final String DEFAULT_CART_EJB;

    static {
        DEFAULT_SHOP_EJB = System.getProperty("shop-ejb",
                "server-ear/server-ejb/ShopBean!bookshop.common.ShopBeanI");
        DEFAULT_CART_EJB = System.getProperty("cart-ejb",
                "server-ear/server-ejb/CartBean!bookshop.common.CartBeanI");
    }
}