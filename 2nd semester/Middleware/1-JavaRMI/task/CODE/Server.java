import java.rmi.Naming;
import java.rmi.server.UnicastRemoteObject;

class Server {

    public static void main(String[] args) {
        try {
            SearcherImpl searcherObj = new SearcherImpl();
            UnicastRemoteObject.exportObject(searcherObj, 0);
            Naming.rebind("//localhost/SearcherServer", searcherObj);

            NodeFactoryImpl nodeFactoryObj = new NodeFactoryImpl();
            UnicastRemoteObject.exportObject(nodeFactoryObj, 0);
            Naming.rebind("//localhost/NodeFactoryServer", nodeFactoryObj);
        } catch (Exception e) {
            System.out.println("Server Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
