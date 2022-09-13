import javax.jws.WebService;

@WebService(targetNamespace = "http://default_package/", endpointInterface = "UC", portName = "UCImplPort", serviceName = "UCImplService")
public class UCImpl implements UC {
	
    public double c2f(double celsius) {
        return celsius * 9.0 / 5.0 + 32;
    }
}
