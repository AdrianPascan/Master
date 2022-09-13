package ws;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/contacts")
public class ContactsResource {

	private static Map<Integer, Contact> contacts = new HashMap<Integer, Contact>();

	public ContactsResource() {
		contacts.put(1, new Contact(1, "Jane Doe", "jane.doe@yahoo.com"));
		contacts.put(2, new Contact(2, "John Doe", "john.doe@yahoo.com"));
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("{contact}")
	public Contact getContact(@PathParam("contact") String contactID) {
		return contacts.get(Integer.parseInt(contactID));
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<Contact> getContacts() {
		List<Contact> returnedContacts = new ArrayList<Contact>();
		returnedContacts.addAll(contacts.values());
		return returnedContacts;
	}
}
