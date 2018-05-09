package bookshop.client;

import bookshop.common.*;

import javax.naming.NamingException;
import java.io.BufferedReader;
import java.io.IOException;

public class UserClient extends ApplicationClient {

    private final ShopBeanI shop;
    private final CartBeanI cart;
    private CartTO currCart;

    public static void main(String[] args) throws NamingException {
        new UserClient().run();
    }

    private UserClient() throws NamingException {
        this.shop = ApplicationClient.getShopBeanI();
        this.cart = ApplicationClient.getCartBeanI();
        this.currCart = null;
    }

    @Override
    String getDefaultMessage() {
        return "Please specify one of the following operations:\n" + ClientOperation.getList();
    }

    Command getCommand(BufferedReader br) throws IOException {
        String[] args = br.readLine().split(" ");
        if (args.length < 1) {
            System.out.println(getDefaultMessage());
        } else if (args[0].equals(ClientOperation.REGISTER_USER.toString())) {
            if (args.length != 3) {
                System.out.println("Usage: " + ClientOperation.REGISTER_USER.toString() + " username password");
            } else {
                return new Command(ClientOperation.REGISTER_USER, tailArgs(args));
            }
        } else if (args[0].equals(ClientOperation.GET_CART.toString())) {
            if (args.length != 3) {
                System.out.println("Usage: " + ClientOperation.GET_CART.toString() + " username password");
            } else {
                return new Command(ClientOperation.GET_CART, tailArgs(args));
            }            
        } else if (args[0].equals(ClientOperation.LIST_BOOKS.toString())) {
            if (args.length != 1) {
                System.out.println("Usage: " + ClientOperation.LIST_BOOKS.toString());
            } else {
                return new Command(ClientOperation.LIST_BOOKS, tailArgs(args));
            }
        } else if (args[0].equals(ClientOperation.ADD_TO_CART.toString())) {
            if (args.length != 2) {
                System.out.println("Usage: " + ClientOperation.ADD_TO_CART.toString() + " bookId");
            } else {
                return new Command(ClientOperation.ADD_TO_CART, tailArgs(args));
            }
        } else if (args[0].equals(ClientOperation.BUY.toString())) {
            if (args.length != 1) {
                System.out.println("Usage: " + ClientOperation.BUY.toString());
            } else {
                return new Command(ClientOperation.BUY, tailArgs(args));
            }
        } else if (args[0].equals(ClientOperation.LEAVE.toString())) {
            if (args.length != 1) {
                System.out.println("Usage: " + ClientOperation.LEAVE.toString());
            } else {
                return new Command(ClientOperation.LEAVE, tailArgs(args));
            }
        } else {
            System.out.println(getDefaultMessage());
        }
        return null;
    }

    void executeCommand(Command cmd) {
        if (cmd.getOp().toString().equals(ClientOperation.REGISTER_USER.toString())) {
            shop.registerUser(cmd.getArgs().get(0), cmd.getArgs().get(1));
            System.out.println("User registered.");
        } else if (cmd.getOp().toString().equals(ClientOperation.GET_CART.toString())) {
            this.currCart = cart.getCart(cmd.getArgs().get(0), cmd.getArgs().get(1));
        } else if (cmd.getOp().toString().equals(ClientOperation.LIST_BOOKS.toString())) {
            System.out.println("Books available:");
            for(BookTO book : shop.listBooks()) {
                System.out.println(book);
            }
        } else if (cmd.getOp().toString().equals(ClientOperation.ADD_TO_CART.toString())) {
            this.currCart = cart.addToCart(this.currCart, Integer.parseInt(cmd.getArgs().get(0)));
        } else if (cmd.getOp().toString().equals(ClientOperation.BUY.toString())) {
            this.currCart = cart.buy(this.currCart);
        } else if (cmd.getOp().toString().equals(ClientOperation.LEAVE.toString())) {
            this.currCart = cart.leave(this.currCart);
        }
    }
}
