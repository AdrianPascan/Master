import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;


public class ExampleConsumer implements MessageListener {
	
	public static void main(String[] args) {
		
		// Create connection to the broker.
		// Note that the factory is usually obtained from JNDI, this method is ActiveMQ-specific
		// used here for simplicity
		try(ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://lab.d3s.mff.cuni.cz:5000?jms.watchTopicAdvisories=false", "labUser", "sieb5w9");
				Connection connection = connectionFactory.createConnection()){
			
			// Create a non-transacted, auto-acknowledged session
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			
			// Create a queue, name must match the queue created by producer
			// Note that this is also provider-specific and should be obtained from JNDI

			Topic topic = session.createTopic("LabTopic");
			
			// Create a consumer
			MessageConsumer consumer = session.createConsumer(topic);
			
			// Create and set an asynchronous message listener
			// consumer.setMessageListener(new ExampleConsumer());
			
			// Start processing messages
			connection.start();
			
			// Create another session, queue and consumer
			// Session session2 = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue queue = session.createQueue("LabQueue");
			MessageProducer producer = session.createProducer(queue);
			
			// Receive a message synchronously
			Message message = session.createTextMessage("Adrian");
			producer.send(message);
			Message msg = consumer.receive();
			
			// Print the message
			if (msg instanceof TextMessage) {
				TextMessage txt = (TextMessage) msg;
				System.out.println("Synchronous: " + txt.getText());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// Asynchronously receive messages
	@Override
	public void onMessage(Message msg) {
		// Print the message
		try {
			if (msg instanceof TextMessage) {
				TextMessage txt = (TextMessage) msg;
				System.out.println("Asynchronous: " + txt.getText());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
