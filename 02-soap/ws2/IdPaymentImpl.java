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
	
	public String payWithId(String id, int amount) {
		Insurance insurance = insurances.stream()
			.where(i -> i.id == id)
			.findFirst()
			.get();
		if (insurance == null) {
			return "No insurance for id " + id;
		}
		if (!insurance.isActive()) {
			return "Inssurance is not active for id " + id;
		}
		if (insurance.getAllowance() < amount) {
			return "Insufficient allowance for id " + id;
		}
		insurance.setAllowance(insurance.getAllowance() - amount);
		return String.format("Successful payment for id %s. Remaining allowance is %d", id, insurance.getAllowance());
	}
}
