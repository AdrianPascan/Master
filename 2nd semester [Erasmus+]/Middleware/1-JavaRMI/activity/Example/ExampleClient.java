import java.rmi.Naming;

public class ExampleClient 
{ 
	static Example server = null;

	public static void main(String[] args){ 
		try {
			// Use the registry on this host to find the server.
			// The host name must be changed if the server uses
			// another computer than the client!
			// server = (Example) Naming.lookup("//localhost/HelloServer");
			// !!! LAB. ACTIVITY !!!
			server = (Example) Naming.lookup("//lab.d3s.mff.cuni.cz:5000/HelloServer");

			// Query local and remote time
			long localTime = System.nanoTime();
			long remoteTime = server.getTime();
      
			// Display both values locally and remotely
			System.out.println("Local time:  " + localTime);
			System.out.println("Remote time: " + remoteTime);
			server.putString("Local time:  " + localTime);
			server.putString("Remote time: " + remoteTime);
		}
		catch (Exception e) { 
			System.out.println("Client Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
