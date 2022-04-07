package ws;

import javax.jws.WebService;

@WebService(endpointInterface = "ws.Calculator")
public class CalculatorImpl implements Calculator {

	public int subtract(int minuend, int subtrahend) 
	{
		return minuend - subtrahend;
	}
}
