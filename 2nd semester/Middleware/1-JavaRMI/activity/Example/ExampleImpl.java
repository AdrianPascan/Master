import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ExampleImpl
	extends UnicastRemoteObject
	implements Example
{
	// Serializable objects need a version attribute,
	// it is not used in this example though.
	private static final long serialVersionUID = 0L;

	// Remotely accessible objects need a constructor.
	public ExampleImpl() throws RemoteException {
		super();
	}
	
	// Trivial implementation of remote methods.
	public long getTime() {
		return System.nanoTime();
	}

	public void putString(String message) {
		System.out.println(message);
	}
}
