import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService(name = "UC", targetNamespace = "http://default_package/")
public interface UC {
    
    @WebMethod(operationName = "c2f", action = "urn:C2f")
	public double c2f(double celsius);
}

