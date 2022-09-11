/**
 * Generator which can generate the documents by their name.
 */
public class DocumentGenerator {
	static Document generateDocument(String documentName) {
		// This method simulates a long running computation generating some document
		try {
			System.out.println("Generating document " + documentName);
			Thread.sleep(3000);
		}
		catch (InterruptedException ie) {
			// exception ignored
		}
		return new Document(
		    "==================================\n" +
		    "This is the document named " + documentName + "\n" +
		    "=================================="
		    );
	}
}