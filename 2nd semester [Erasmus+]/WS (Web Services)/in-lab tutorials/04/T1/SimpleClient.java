import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import ws.Calculator; 

public class SimpleClient {
  public static void main(String[] args) throws Exception {
   URL url = new URL("http://127.0.0.1:8000/Calculator?wsdl");
   QName qname = new QName("http://ws/", "CalculatorImplService");
   Service service = Service.create(url, qname);
   Calculator calculator = service.getPort(Calculator.class);
   System.out.println(calculator.subtract(1000, 1));
  }
}
