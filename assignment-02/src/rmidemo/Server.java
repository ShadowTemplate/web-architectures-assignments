package rmidemo;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Server implements ServerI {

    private static final String DEFAULT_PORT = String.valueOf(Registry.REGISTRY_PORT);
    private static final String DEFAULT_NAME = "rmi:///rmiserver";

    public Document addTimestamp(Document document) throws RemoteException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String newString = "Viewed on " + dateFormat.format(new Date());
        System.out.println("Going to add on remote document \"" + newString + "\"");
        document.addString(newString);
        return document;
    }

    public static void main(String[] args) {
        String port = System.getProperty("port", DEFAULT_PORT),
                name = System.getProperty("name", DEFAULT_NAME);
        try {
            Registry registry = LocateRegistry.createRegistry(Integer.parseInt(port));
            ServerI rmiServer = (ServerI) UnicastRemoteObject.exportObject(new Server(), 0);
            System.out.println("Trying to bind server on RMI port " + port + " on the address " + name);
            registry.bind(name, rmiServer);
            System.out.println("RMI server ready...");
        } catch (AlreadyBoundException | RemoteException e) {
            System.err.println("Unable to start RMI server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
