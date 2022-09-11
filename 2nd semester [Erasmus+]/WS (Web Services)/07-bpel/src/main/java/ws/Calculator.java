package ws;

@javax.jws.WebService
public interface Calculator {
	
	@javax.jws.WebMethod
    int subtract(int minuend, int subtrahend);
}
