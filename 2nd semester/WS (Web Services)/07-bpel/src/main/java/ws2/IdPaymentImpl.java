package ws2;

import java.util.Arrays;
import java.util.List;

import javax.jws.WebService;

@WebService(endpointInterface = "ws2.IdPayment")
public class IdPaymentImpl implements IdPayment {
	
	private final List<Insurance> insurances = Arrays.asList(
			new Insurance("XX012345", false, 0),
			new Insurance("YY012345", true, 1000),
			new Insurance("ZZ012345", true, 9999)
	);
	
	
    public int getAllowanceForId(String id) {
    	Insurance insurance = getInsuranceById(id);
    	if (insurance == null || !insurance.isActive()) {
			return -1;
		}
		return insurance.getAllowance();
    }
	
    public boolean setAllowanceForId(String id, int amount) {
    	Insurance insurance = getInsuranceById(id);
    	if (amount < 0 || insurance == null || !insurance.isActive()) {
			return false;
		}
    	insurance.setAllowance(amount);
    	return true;
    }
    
    private Insurance getInsuranceById(String id) {
		return insurances.stream()
			.where(i -> i.id == id)
			.findFirst()
			.get();
    }
}
