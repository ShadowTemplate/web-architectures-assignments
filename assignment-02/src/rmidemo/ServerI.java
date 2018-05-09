package rmidemo;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerI extends Remote {

    Document addTimestamp(Document document) throws RemoteException;
}
