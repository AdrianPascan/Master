package ws;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.xml.bind.JAXBElement;

@Path("/insurances")
public class InsurancesResource {

	@Context
	UriInfo uriInfo;
	
	private static Map<String, Insurance> insurances = new HashMap<String, Insurance>();
    
	public InsurancesResource() {
	    insurances.put("WW012345", new Insurance("WW012345", false, 0));
	    insurances.put("XX012345", new Insurance("XX012345", false, 100));
	    insurances.put("YY012345", new Insurance("YY012345", true, 1000));
	    insurances.put("ZZ012345", new Insurance("ZZ012345", true, 9999));
	}

	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public List<Insurance> getInsurances() {
	    List<Insurance> returnedInsurances = new ArrayList<Insurance>();
	    returnedInsurances.addAll( insurances.values() );
	    return returnedInsurances;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response postInsurance(JAXBElement<Insurance> insuranceElem) {
		Insurance insurance = insuranceElem.getValue();
		Response res;
		
		if (insurances.get(insurance.getId()) != null) {
			res = Response
					.created(uriInfo.getAbsolutePath())
					.status(Response.Status.CONFLICT)
					.entity("Already existing insurance for id='" + insurance.getId() + "'.")
					.build();
		} else if (insurance.getAllowance() < 0) {
			res = Response
					.created(uriInfo.getAbsolutePath())
					.status(Response.Status.CONFLICT)
					.entity("Allowance must be at least 0.")
					.build();
		}
		else {
			insurances.put(insurance.getId(), insurance);
			res = Response
					.created(uriInfo.getAbsolutePath())
					.status(Response.Status.CREATED)
					.build();
		}

		return res;
	}
	
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Path("{insurance}")
	public Insurance getInsurance(@PathParam("insurance") String insuranceId) {
		return insurances.get(insuranceId);
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_XML)
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("{insurance}")
	public Response putInsurance(@PathParam("insurance") String insuranceId, JAXBElement<Insurance> insuranceElem) {
		Insurance insurance = insuranceElem.getValue();
		Response res;
		
		if (insurances.get(insuranceId) == null) {
			res = Response
					.created(uriInfo.getAbsolutePath())
					.status(Response.Status.CONFLICT)
					.entity("No insurance for id='" + insuranceId + "'.")
					.build();
		} else if (!insurance.getId().equals(insuranceId)) {
			res = Response
					.created(uriInfo.getAbsolutePath())
					.status(Response.Status.CONFLICT)
					.entity("Insurance ids do not match: '" + insuranceId + "'!='" + insurance.getId() + "'.")
					.build();
		} else if (insurance.getAllowance() < 0) {
			res = Response
					.created(uriInfo.getAbsolutePath())
					.status(Response.Status.CONFLICT)
					.entity("Allowance must be at least 0.")
					.build();
		}
		else {	
			insurances.replace(insuranceId, insurance);
			
			res = Response
					.created(uriInfo.getAbsolutePath())
					.status(Response.Status.NO_CONTENT)
					.build();
		}
		
		return res;
	}
	
	@DELETE
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("{insurance}")
	public Response deleteInsurance(@PathParam("insurance") String insuranceId) {
		Response res;
		
		Insurance insurance = insurances.remove(insuranceId);
		
		if (insurance == null) {
			res = Response
					.created(uriInfo.getAbsolutePath())
					.status(Response.Status.CONFLICT)
					.entity("No insurance for id='" + insuranceId + "'.")
					.build();
		}
		else {	
			for (Insurance i : insurances.values()) {
				System.out.println(i);
			}
			res = Response
					.created(uriInfo.getAbsolutePath())
					.status(Response.Status.OK)
					.build();
		}
		
		return res;
	}
}
