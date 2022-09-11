import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Client {
	// Reader for user input
	private LineNumberReader in = new LineNumberReader(new InputStreamReader(System.in));
	// Connection to the cluster
	private HazelcastInstance hazelcast;
	// The name of the user
	private String userName;
	// Do not keep any other state here - all data should be in the cluster
	IMap<String, Document> documents;
	IMap<String, Boolean> cachedDocuments;
	IMap<String, Integer> documentViewCount;
	IMap<String, List<String>> documentComments;
	IMap<String, List<String>> userFavorites;
	IMap<String, Integer> userFavoriteIndex;
	IMap<String, String> userSelectedDocument;

	/**
	 * Create a client for the specified user.
	 * @param userName user name used to identify the user
	 */
	public Client(String userName) {
		this.userName = userName;
		// Connect to the Hazelcast cluster
		ClientConfig config = new ClientConfig();
		hazelcast = HazelcastClient.newHazelcastClient(config);
		// Get the distributed maps
		documents = hazelcast.getMap("Documents");
		documentViewCount = hazelcast.getMap("DocumentViewCount");
		documentComments = hazelcast.getMap("DocumentComments");
		userFavorites = hazelcast.getMap("UserFavorites");
		userFavoriteIndex = hazelcast.getMap("UserFavoriteIndex");
		userSelectedDocument = hazelcast.getMap("UserSelectedDocument");
	}

	/**
	 * Disconnect from the Hazelcast cluster.
	 */
	public void disconnect() {
		// Disconnect from the Hazelcast cluster
		hazelcast.shutdown();
	}

	/**
	 * Read a name of a document,
	 * select it as the current document of the user
	 * and show the document content.
	 */
	private void showCommand() throws IOException {
		System.out.println("Enter document name:");
		String documentName = in.readLine();
		if (documentName.isEmpty()) {
			System.out.println("Document name is empty");
			return; 
		}

		// Currently, the document is generated directly on the client
		// TODO: change it, so that the document is generated in the cluster and cached
		Document document = documents.compute(documentName, (docName, doc) -> {
			if (doc == null) {
				return DocumentGenerator.generateDocument(docName);
			}
			return doc;
		});

		// TODO: Set the currently selected document for the user
		userSelectedDocument.put(userName, documentName);
		// TODO: Get the document (from the cache, or generated)
		if (document == null) {
			// document was not cached, generate it again
			document = documents.compute(documentName, (docName, doc) -> {
				return DocumentGenerator.generateDocument(docName);
			});
		}
		// TODO: Increment the view count
		documentViewCount.compute(documentName, (docName, viewCount) -> {
			return viewCount == null ? 1 : viewCount + 1; 
		});

		// Show the document content
		System.out.println("The document is:");
		System.out.println(document.getContent());
	}

	/**
	 * Show the next document in the list of favorites of the user.
	 * Select the next document, so that running this command repeatedly
	 * will cyclically show all favorite documents of the user.
	 */
	private void nextFavoriteCommand() {
		// TODO: Select the next document form the list of favorites
		List<String> favoriteList = userFavorites.get(userName);
		if (favoriteList == null || favoriteList.isEmpty()) {
			System.out.println("You have no favorites");
			return;
		}

		int favoriteIndex = userFavoriteIndex.compute(userName, (user, favoriteIndx) -> {
			if (favoriteIndx == null || favoriteIndx + 1 == favoriteList.size()) {
				return 0;
			}
			return favoriteIndx + 1;
		});

		String documentName = favoriteList.get(favoriteIndex);
		userSelectedDocument.put(userName, documentName);
		// TODO: Increment the view count, get the document (from the cache, or generated) and show the document content
		documentViewCount.compute(documentName, (docName, viewCount) -> {
			return viewCount == null ? 1 : viewCount + 1; 
		});
		// if document is not cached, generate it again
		Document document = documents.compute(documentName, (docName, doc) -> {
			if (doc == null) {
				return DocumentGenerator.generateDocument(docName);
			}
			return doc;
		});
		// Show the document content
		System.out.println("The document is:");
		System.out.println(document.getContent());
	}

	/**
	 * Add the currently selected document name to the list of favorite documents of the user.
	 * If the list already contains the document name, do nothing.
	 */
	private void addFavoriteCommand() {
		// TODO: Add the name of the selected document to the list of favorites
		String selectedDocumentName = userSelectedDocument.get(userName);
		if (selectedDocumentName == null || selectedDocumentName.isEmpty()) {
			System.out.println("No document selected");
			return;
		}
		userFavorites.compute(userName, (user, favoriteList) ->  {
			if (favoriteList == null) {
				favoriteList = new ArrayList<>();
			}
			else if(favoriteList.contains(selectedDocumentName)) {
				System.out.printf("Already added %s to favorites%n", selectedDocumentName);
			} else {
				favoriteList.add(selectedDocumentName);
				System.out.printf("Added %s to favorites%n", selectedDocumentName);
			}
			return favoriteList;
		});
	}
	/**
	 * Remove the currently selected document name from the list of favorite documents of the user.
	 * If the list does not contain the document name, do nothing.
	 */
	private void removeFavoriteCommand(){
		// TODO: Remove the name of the selected document from the list of favorites
		String selectedDocumentName = userSelectedDocument.get(userName);
		if (selectedDocumentName == null || selectedDocumentName.isEmpty()) {
			System.out.println("No document selected");
			return;
		}

		userFavorites.compute(userName, (user, favoriteList) -> {
			if (favoriteList == null || !favoriteList.remove(selectedDocumentName)) {
				System.out.println("Selected document is not in favorites");
			}
			else {
				System.out.printf("Removed %s from favorites%n", selectedDocumentName);
				// restart cycle on next favorite
				userFavoriteIndex.remove(userName);
				userSelectedDocument.remove(userName);
			}
			return favoriteList;
		});
	}
	/**
	 * Add the current selected document name to the list of favorite documents of the user.
	 * If the list already contains the document name, do nothing.
	 */
	private void listFavoritesCommand() {
		// TODO: Get the list of favorite documents of the user
		List<String> favoriteList = userFavorites.get(userName);
		// Print the list of favorite documents
		if (favoriteList == null || favoriteList.isEmpty()) {
			System.out.println("You have no favorite documents");
			return;
		}
		System.out.println("Your list of favorite documents:");
		for(String favoriteDocumentName: favoriteList)
			System.out.println(favoriteDocumentName);
	}

	/**
	 * Show the view count and comments of the current selected document.
	 */
	private void infoCommand(){
		// TODO: Get the view count and list of comments of the selected document
		String selectedDocumentName = userSelectedDocument.get(userName);
		if (selectedDocumentName == null || selectedDocumentName.isEmpty()) {
			System.out.println("No document selected");
			return;
		}
		int viewCount = documentViewCount.get(selectedDocumentName);
		List<String> comments = documentComments.get(selectedDocumentName);

		// Print the information
		System.out.printf("Info about %s:%n", selectedDocumentName);
		System.out.printf("Viewed %d times.%n", viewCount);
		if (comments == null || comments.isEmpty()) {
			System.out.println("No comments");
		}
		else {
			System.out.printf("Comments (%d):%n", comments.size());
			for(String comment: comments)
				System.out.println(comment);
		}
	}
	/**
	 * Add a comment about the current selected document.
	 */
	private void commentCommand() throws IOException{
		System.out.println("Enter comment text:");
		String commentText = in.readLine();
		if (commentText.isEmpty()) {
			System.out.println("Comment is empty");
			return;
		}

		// TODO: Add the comment to the list of comments of the selected document
		String selectedDocumentName = userSelectedDocument.get(userName);
		if (selectedDocumentName == null || selectedDocumentName.isEmpty()) {
			System.out.println("No document selected");
			return;
		}
		documentComments.compute(selectedDocumentName, (docName, commentList) -> {
			if (commentList == null) {
				commentList = new ArrayList<>();
			}
			commentList.add(commentText);
			return commentList;
		});
		System.out.printf("Added a comment about %s.%n", selectedDocumentName);
	}

	/*
	 * Main interactive user loop
	 */
	public void run() throws IOException {
		loop:
		while (true) {
			System.out.println("\nAvailable commands (type and press enter):");
			System.out.println(" s - select and show document");
			System.out.println(" i - show document view count and comments");
			System.out.println(" c - add comment");
			System.out.println(" a - add to favorites");
			System.out.println(" r - remove from favorites");
			System.out.println(" n - show next favorite");
			System.out.println(" l - list all favorites");
			System.out.println(" q - quit");
			// read first character
			int c = in.read();
			// throw away rest of the buffered line
			while (in.ready())
				in.read();
			switch (c) {
				case 'q': // Quit the application
					break loop;
				case 's': // Select and show a document
					showCommand();
					break;
				case 'i': // Show view count and comments of the selected document
					infoCommand();
					break;
				case 'c': // Add a comment to the selected document
					commentCommand();
					break;
				case 'a': // Add the selected document to favorites
					addFavoriteCommand();
					break;
				case 'r': // Remove the selected document from favorites
					removeFavoriteCommand();
					break;
				case 'n': // Select and show the next document in the list of favorites
					nextFavoriteCommand();
					break;
				case 'l': // Show the list of favorite documents
					listFavoritesCommand();
					break;
				case '\n':
				default:
					break;
			}
		}
	}

	/*
	 * Main method, creates a client instance and runs its loop
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Usage: ./client <userName>");
			return;
		}

		try {
			Client client = new Client(args[0]);
			try {
				client.run();
			}
			finally {
				client.disconnect();
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

}
