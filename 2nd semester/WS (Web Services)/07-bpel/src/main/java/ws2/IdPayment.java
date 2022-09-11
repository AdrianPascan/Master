package ws2;

@javax.jws.WebService
public interface IdPayment {
	
	@javax.jws.WebMethod
    int getAllowanceForId(String id);
	
	@javax.jws.WebMethod
    boolean setAllowanceForId(String id, int amount);
}
