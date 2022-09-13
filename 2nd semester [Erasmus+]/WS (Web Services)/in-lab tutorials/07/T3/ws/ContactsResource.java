package ws;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.ws.rs.Consumes;
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

@Path("/contacts")
public class ContactsResource {

	@Context
	UriInfo uriInfo;
	
	private static Map<Integer, Contact> contacts = new HashMap<Integer, Contact>();
    
	public ContactsResource() {
	    contacts.put(1, new Contact(1, "Jane Doe", "jane.doe@yahoo.com"));
	    contacts.put(2, new Contact(2, "John Doe", "john.doe@yahoo.com"));
	}
	
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Path("{contact}")
	public Contact getContact(@PathParam("contact") String contactID) {
		return contacts.get(Integer.parseInt(contactID));
	}

	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public List<Contact> getContacts() {
	    List<Contact> returnedContacts = new ArrayList<Contact>();
	    returnedContacts.addAll( contacts.values() );
	    return returnedContacts;
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_XML)
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("{contact}")
	public Response putContact(@PathParam("contact") String contactID, JAXBElement<Contact> contact) {
		Contact c = contact.getValue();
		Response res;
		
		if (contacts.get(c.getID()) == null) {
//			System.out.println("ERROR ---------------------------------------------");
			res = Response
					.created(uriInfo.getAbsolutePath())
					.status(Response.Status.CONFLICT)
					.entity("Numbers don't match")
					.build();
		}
		else {
			System.out.println("CHANGE --------------------------------------------");
			
			Contact c_old = contacts.replace(c.getID(), c);
			System.out.println(c_old.toString());
			System.out.println(c.toString());
			System.out.println(contacts.get(c.getID()).toString());
			
			res = Response
					.created(uriInfo.getAbsolutePath())
					.status(Response.Status.NO_CONTENT)
					.build();
		}
	
//		res = Response.created(uriInfo.getAbsolutePath()).build();
		
		return res;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response postContact(JAXBElement<Contact> contact) {
		Contact c = contact.getValue();
		Response res;
		
		c.setID(Collections.max(contacts.keySet()) + 1);

		res = Response
				.created(uriInfo.getAbsolutePath())
				.status(Response.Status.CREATED)
				.entity(uriInfo)
				.build();

		return res;
	}
}
