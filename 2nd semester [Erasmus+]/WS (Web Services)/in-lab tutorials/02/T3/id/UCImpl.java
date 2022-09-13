package id;
import javax.jws.WebService;

@WebService(endpointInterface = "id.UC")
public class UCImpl implements UC {
	
    public double c2f(double celsius) {
        return celsius * 9.0 / 5.0 + 32;
    }
}
