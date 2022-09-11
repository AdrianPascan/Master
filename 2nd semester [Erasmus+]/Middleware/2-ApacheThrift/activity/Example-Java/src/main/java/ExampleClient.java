import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

public class ExampleClient {
    public static void main(String args[]) {
        // Connect to server by TCP socket
        try (TTransport transport = new TSocket("localhost", 5000)) {
            // The socket transport is already buffered
            // Use a binary protocol to serialize data
            TProtocol muxProtocol = new TBinaryProtocol(transport);
            // Use a multiplexed protocol to select a service by name
            TProtocol exampleProtocol = new TMultiplexedProtocol(muxProtocol, "Example");
            // Proxy object
            Example.Client client = new Example.Client(exampleProtocol);

            // Open the connection
            transport.open();
            
            // Send a string and print the response
            String text = "Hello from Java";
            System.out.printf("Calling Example with: %s%n", text);
            String response = client.ping(text);
            System.out.printf("Response: %s%n", response);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
