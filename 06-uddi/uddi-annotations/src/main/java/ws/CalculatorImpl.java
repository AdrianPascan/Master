package ws;

import javax.jws.WebService;

import org.apache.juddi.v3.annotations.UDDIService;
import org.apache.juddi.v3.annotations.UDDIServiceBinding;

@UDDIService(
		businessKey = "uddi:www.mycompany.com:f5e21ceb-5c86-4d1c-bb31-c1fcc2a491c4", 
		serviceKey = "uddi:${keyDomain}:services-calculator${department}", 
		description = "Calculator test service")
@UDDIServiceBinding(
		bindingKey = "uddi:${keyDomain}:${serverName}-${serverPort}-hello${department}-wsdl", 
		description = "WSDL endpoint for the calculator${department} Service. This service is used for testing the jUDDI annotation functionality",
		accessPointType = "wsdlDeployment", 
		accessPoint = "http://${serverName}:${serverPort}/uddi-annotations/services/calculator?wsdl")
@WebService(
		endpointInterface = "ws.Calculator", 
		serviceName = "Calculator")
public class CalculatorImpl implements Calculator {

	public int subtract(int minuend, int subtrahend) {
		return minuend - subtrahend;
	}
}
