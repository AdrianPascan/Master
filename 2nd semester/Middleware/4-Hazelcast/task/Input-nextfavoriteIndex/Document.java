import java.io.Serializable;

/**
 * Represents some kind of document that can be displayed to the user.
 */
public class Document implements Serializable {
	private String content;
	public Document(String content) {
	    this.content = content;
	}
	public String getContent() {
	    return content;
	}
}