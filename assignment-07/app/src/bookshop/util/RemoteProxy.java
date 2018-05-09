package bookshop.util;

import bookshop.common.ShopBeanI;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Hashtable;

public class RemoteProxy {

    private static final String DEFAULT_SHOP_EJB;

    static {
        DEFAULT_SHOP_EJB = System.getProperty("shop-ejb", "server-ear/server-ejb/ShopBean!bookshop.common.ShopBeanI");
    }

    public static ShopBeanI getShop() throws NamingException {
        try {
            return (ShopBeanI) getContext().lookup(DEFAULT_SHOP_EJB);
        } catch (Exception ex) {
            System.err.println("Unable to connect to remote EJB...\n" + ex.getMessage());
            ex.printStackTrace();
            throw ex;
        }
    }

    private static Context getContext() throws NamingException {
        final Hashtable jndiProperties = new Hashtable();
        jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
        jndiProperties.put(Context.PROVIDER_URL, "http-remoting://localhost:8080");
        jndiProperties.put("jboss.naming.client.ejb.context", true);
        return new InitialContext(jndiProperties);
    }

}