package bookshop.client;

import bookshop.common.CartBeanI;
import bookshop.common.ShopBeanI;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

abstract class ApplicationClient {

    abstract String getDefaultMessage();
    abstract Command getCommand(BufferedReader br) throws IOException;
    abstract void executeCommand(Command cmd);

    void run() {
        System.out.println(getDefaultMessage());
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                System.out.print("> ");
                Command cmd = this.getCommand(br);
                if (cmd != null) {
                    try {
                        this.executeCommand(cmd);
                    } catch (Exception ex) {
                        System.err.println(ex.getMessage());
                        ex.printStackTrace();
                    }
                }
            }
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    static ShopBeanI getShopBeanI() throws NamingException {
        String endPoint = Constants.DEFAULT_SHOP_EJB;
        System.out.println("\nTrying to connect to EJB located at " + endPoint + "\n");
        try {
            Context initialContext = new InitialContext();
            return (ShopBeanI) initialContext.lookup(endPoint);
        } catch (NamingException ex) {
            System.err.println("Unable to connect to remote EJB...\n" + ex.getMessage());
            ex.printStackTrace();
            throw ex;
        }
    }

    static CartBeanI getCartBeanI() throws NamingException {
        String endPoint = Constants.DEFAULT_CART_EJB;
        System.out.println("\nTrying to connect to EJB located at " + endPoint + "\n");
        try {
            Context initialContext = new InitialContext();
            return (CartBeanI) initialContext.lookup(endPoint);
        } catch (NamingException ex) {
            System.err.println("Unable to connect to remote EJB...\n" + ex.getMessage());
            ex.printStackTrace();
            throw ex;
        }
    }

    static List<String> tailArgs(String[] args) {
        return Arrays.asList(Arrays.copyOfRange(args, 1, args.length));
    }
}
