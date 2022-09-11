import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

class NodeFactoryImpl implements NodeFactory {

    int counter = 0;

    public void createNode() throws RemoteException, MalformedURLException {
        NodeImpl nodeObj = new NodeImpl();
        UnicastRemoteObject.exportObject(nodeObj, 0);
        Naming.rebind("//localhost/Node" + String.valueOf(counter), nodeObj);
        counter = counter + 1;
    }
}