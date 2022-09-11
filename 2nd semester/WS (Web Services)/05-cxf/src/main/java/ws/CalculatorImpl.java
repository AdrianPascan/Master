package ws;

import javax.jws.WebService;

@WebService(targetNamespace = "http://ws/", endpointInterface = "ws.Calculator", portName = "CalculatorImplPort", serviceName = "CalculatorImplService")
public class CalculatorImpl implements Calculator {

	public int subtract(int minuend, int subtrahend) 
	{
		return minuend - subtrahend;
	}
}
