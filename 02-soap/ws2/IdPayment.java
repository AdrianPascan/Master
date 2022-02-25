package ws2;

@javax.jws.WebService
public interface IdPayment {
	
	@javax.jws.WebMethod
    String payWithId(String id, int amount);
}
