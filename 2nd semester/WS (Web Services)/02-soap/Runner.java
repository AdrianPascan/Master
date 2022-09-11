import ws.CalculatorImpl;
import ws2.IdPaymentImpl;

import javax.xml.ws.Endpoint;

public class Runner {

    public static void main(String[] args) {
        Endpoint.publish("http://127.0.0.1:8000/Calculator", new CalculatorImpl());
        Endpoint.publish("http://127.0.0.1:8000/IdPayment", new IdPaymentImpl());
    }
}