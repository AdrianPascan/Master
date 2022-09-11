import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.EntryProcessor;
import com.hazelcast.map.IMap;

// Example application which is a Hazelcast client
public class ExampleClient {
	public static void main(String[] args) {
		// No command-line arguments
	    if (args.length != 0) {
			System.err.println("Usage: bash run-client.sh");
			return;
		}

		// Use the default configuration
	    ClientConfig config = new ClientConfig();

	    // Connect to the Hazelcast cluster.
	    // As a client, we can query and modify the data,
	    // but we do not store any data.
	    HazelcastInstance hazelcast = HazelcastClient.newHazelcastClient(config);
		// Get the example distributed hash map-
        IMap<String, String> map = hazelcast.getMap("Example");

		// Print each entry on the member that stores it
        map.executeOnEntries((k) -> {
        	System.out.println("This member owns key " + k.getKey() + " with value " + k.getValue());
        	return null;
        	});

		// Disconnect
        hazelcast.shutdown();
    }

}
