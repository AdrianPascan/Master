package ws;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService(name = "Calculator", targetNamespace = "http://ws/")
public interface Calculator {
	
    @WebMethod(operationName = "subtract", action = "urn:Subtract")
	int subtract(int minuend, int subtrahend);
}
