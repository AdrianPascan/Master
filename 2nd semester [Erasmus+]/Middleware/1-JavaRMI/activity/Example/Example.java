import java.rmi.Remote; 
import java.rmi.RemoteException; 

public interface Example extends Remote
{
	long getTime() throws RemoteException;
	void putString(String message) throws RemoteException; 
}
