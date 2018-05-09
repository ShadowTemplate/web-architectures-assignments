package rmidemo;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    private static final String DEFAULT_PORT = String.valueOf(Registry.REGISTRY_PORT);
    private static final String DEFAULT_NAME = "rmi:///rmiserver";

    public static void main(String[] args) {
        try {
            String port = System.getProperty("port", DEFAULT_PORT),
                    host = System.getProperty("host", null),
                    name = System.getProperty("name", DEFAULT_NAME);
            System.out.println("Creating empty document.");
            Document doc = new Document();
            System.out.println("Querying registry to build server stub...");
            Registry registry = LocateRegistry.getRegistry(host, Integer.parseInt(port));
            ServerI server = (ServerI) registry.lookup(name);
            System.out.println("Calling remote server method...");
            doc = server.addTimestamp(doc);
            System.out.println(doc);
        } catch (RemoteException | NotBoundException e) {
            System.err.println("Connection error with remote server:" + e.getMessage());
            e.printStackTrace();
        }
    }
}
