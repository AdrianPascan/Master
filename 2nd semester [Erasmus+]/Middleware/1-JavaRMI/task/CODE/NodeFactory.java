import java.net.MalformedURLException;
import java.rmi.Remote;
import java.rmi.RemoteException;

interface NodeFactory extends Remote {
    void createNode() throws RemoteException, MalformedURLException;
}