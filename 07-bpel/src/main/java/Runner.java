import ws.CalculatorImpl;
import ws2.IdPaymentImpl;

import javax.jws.WebService;
import javax.xml.ws.Endpoint;

@WebService(targetNamespace = "http://default_package/", portName = "RunnerPort", serviceName = "RunnerService")
public class Runner {

    public static void main(String[] args) {
        Endpoint.publish("http://127.0.0.1:8000/Calculator", new CalculatorImpl());
        Endpoint.publish("http://127.0.0.1:8000/IdPayment", new IdPaymentImpl());
    }
}