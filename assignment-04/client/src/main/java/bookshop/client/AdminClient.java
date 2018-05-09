package bookshop.client;

import bookshop.common.OperationTO;
import bookshop.common.ShopBeanI;

import javax.naming.NamingException;
import java.io.BufferedReader;
import java.io.IOException;

public class AdminClient extends ApplicationClient {

    private final ShopBeanI shop;

    public static void main(String[] args) throws NamingException {
        new AdminClient().run();
    }

    private AdminClient() throws NamingException {
        this.shop = ApplicationClient.getShopBeanI();
    }

    @Override
    String getDefaultMessage() {
        return "Please specify one of the following operations:\n" + AdminOperation.getList();
    }

    Command getCommand(BufferedReader br) throws IOException {
        String[] args = br.readLine().split(" ");
        if (args.length < 1) {
            System.out.println(getDefaultMessage());
        } else if (args[0].equals(AdminOperation.ADD_BOOK.toString())) {
            if (args.length != 4) {
                System.out.println("Usage: " + AdminOperation.ADD_BOOK.toString() + " adminPassword bookTitle bookPrice");
            } else {
                return new Command(AdminOperation.ADD_BOOK, tailArgs(args));
            }
        } else if (args[0].equals(AdminOperation.LIST_OPERATIONS.toString())) {
            if (args.length != 2) {
                System.out.println("Usage: " + AdminOperation.LIST_OPERATIONS.toString() + " password");
            } else {
                return new Command(AdminOperation.LIST_OPERATIONS, tailArgs(args));
            }
        } else {
            System.out.println(getDefaultMessage());
        }
        return null;
    }

    void executeCommand(Command cmd) {
        if (cmd.getOp().toString().equals(AdminOperation.ADD_BOOK.toString())) {
            shop.addBook(cmd.getArgs().get(0), cmd.getArgs().get(1), Integer.parseInt(cmd.getArgs().get(2)));
            System.out.println("Book added.");
        } else if (cmd.getOp().toString().equals(AdminOperation.LIST_OPERATIONS.toString())) {
            System.out.println("Operations:");
            for (OperationTO op : shop.listOperations(cmd.getArgs().get(0))) {
                System.out.println(op);
            }
        }
    }
}
