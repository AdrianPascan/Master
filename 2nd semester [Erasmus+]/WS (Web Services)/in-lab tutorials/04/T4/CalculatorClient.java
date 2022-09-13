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

public class CalculatorClient {
	public static void main(String[] args) throws SOAPException {
		SOAPConnectionFactory soapcf = SOAPConnectionFactory.newInstance();
		SOAPConnection soapc = soapcf.createConnection();

		MessageFactory mf = MessageFactory.newInstance();
		SOAPMessage soapm = mf.createMessage();
		    
		SOAPPart soapp = soapm.getSOAPPart();
		SOAPEnvelope soape = soapp.getEnvelope();
		SOAPBody soapb = soape.getBody();

		soape.getHeader().detachNode();
		QName name = new QName("http://ws/", "subtract", "ws");
		SOAPElement soapel = soapb.addBodyElement(name);

		
//		QName typeName = new QName("http://www.w3.org/2001/XMLSchema", "type", "xs");
//		soapel.addChildElement(
//		    new QName("arg0")).addAttribute(typeName, "int").addTextNode("1000");
//		soapel.addChildElement(
//		    new QName("arg1")).addAttribute(typeName, "int").addTextNode("1");
		soapel.addChildElement(
		    new QName("arg0")).addTextNode("1000");
		soapel.addChildElement(
		    new QName("arg1")).addTextNode("1");
		String endpoint = "http://127.0.0.1:8000/Calculator";
		SOAPMessage response = soapc.call(soapm, endpoint);
		soapc.close();
		
		SOAPBody responseBody = response.getSOAPBody();
		if (responseBody.hasFault()) {
		    System.out.println(responseBody.getFault().getFaultString()); 
		} else {
			System.out.println(response);
			System.out.println(responseBody);
			
			QName subtractResponseName = new QName("http://ws/", "subtractResponse", "ns2");
			QName returnName = new QName("http://ws/", "return");
	
			SOAPBodyElement subtractResponseEl = (SOAPBodyElement)
				       responseBody.getChildElements(subtractResponseName).next();
			SOAPBodyElement returnEl = (SOAPBodyElement)
			       subtractResponseEl.getChildElements(returnName).next();
	
			System.out.println(returnEl.getValue());
		}
	}
}
