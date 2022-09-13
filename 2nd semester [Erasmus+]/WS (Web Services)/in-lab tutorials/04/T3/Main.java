import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

public class Main {
	public static void main(String[] args) throws SOAPException {
		SOAPConnectionFactory soapcf = SOAPConnectionFactory.newInstance();
		SOAPConnection soapc = soapcf.createConnection();

		MessageFactory mf = MessageFactory.newInstance();
		SOAPMessage soapm = mf.createMessage();
		    
		SOAPPart soapp = soapm.getSOAPPart();
		SOAPEnvelope soape = soapp.getEnvelope();
		SOAPBody soapb = soape.getBody();

		soape.getHeader().detachNode();
		QName name = new QName("http://tempuri.org/", "Add", "temp");
		SOAPElement soapel = soapb.addBodyElement(name);

		soapel.addChildElement(
		    new QName("http://tempuri.org/", "intA", "temp")).addTextNode("1");
		soapel.addChildElement(
		    new QName("http://tempuri.org/", "intB", "temp")).addTextNode("2");
		String endpoint = "http://www.dneonline.com/calculator.asmx";
		SOAPMessage response = soapc.call(soapm, endpoint);
		soapc.close();
		
		SOAPBody responseBody = response.getSOAPBody();
		if (responseBody.hasFault()) {
		    System.out.println(responseBody.getFault().getFaultString()); 
		} else {
			QName AddResponseName = new QName("http://tempuri.org/", "AddResponse");
			QName AddResultName = new QName("http://tempuri.org/", "AddResult");
	
			SOAPBodyElement AddResponse = (SOAPBodyElement)
			       responseBody.getChildElements(AddResponseName).next();
			SOAPBodyElement AddResult = (SOAPBodyElement)
			       AddResponse.getChildElements(AddResultName).next();
	
			System.out.println("AddResult = " + AddResult.getValue());
		}
	}
}
