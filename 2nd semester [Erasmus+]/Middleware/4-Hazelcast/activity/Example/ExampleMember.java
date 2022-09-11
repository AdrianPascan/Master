import com.hazelcast.config.Config;
import com.hazelcast.config.FileSystemXmlConfig;
import com.hazelcast.config.FileSystemYamlConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


// Example application which is a Hazelcast cluster member
public class ExampleMember {

	public static void main(String[] args) {
        // The command-line argument is a prefix for entries created by this member
		if (args.length != 1) {
			System.err.println("Usage: bash run-member.sh <prefix>");
			return;
		}
		String prefix = args[0];

		try {
            // Load the configuration from hazelcast.yaml
            // You can also use XML format
            // or creating empty configuration object by new Config()
            // and setting properties of this object
            Config config = new FileSystemYamlConfig("hazelcast.yaml");

            // Create a Hazelcast member.
            // This will either create a new cluster or join an existing one
            // according to the join section in the configuration.
            HazelcastInstance hazelcast = Hazelcast.newHazelcastInstance(config);

            // Get the example distributed map
            // If it does not exist, it will be created
            IMap<String, String> map = hazelcast.getMap("Example");

            // Get the name of this member.
            // It will be automatically generated.
            String memberName = hazelcast.getName();

            // Insert keys prefix0 ... prefix9 into the map
            for(int i = 0; i < 10; ++i){
                map.put(prefix + i, memberName);
            }

            // Keep the member running until enter is pressed
            try {
                System.out.println("Press enter to exit");
                System.in.read();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Leave the cluster
            hazelcast.shutdown();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


}
