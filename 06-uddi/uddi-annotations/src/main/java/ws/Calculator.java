package ws;

import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface Calculator {
	
    int subtract(@WebParam(name="arg0") int minuend, @WebParam(name="arg1") int subtrahend);
}
