

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

/**
 * Servlet implementation class CalculatorInterServlet
 */
@WebServlet("/CalculatorInterServlet")
public class CalculatorInterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CalculatorInterServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean commission = false;
		
		try {	
			MessageFactory messageFactory = MessageFactory.newInstance();
			SOAPMessage requestMessage = messageFactory.createMessage(null, request.getInputStream());
			    
			SOAPPart requestPart = requestMessage.getSOAPPart();
			SOAPEnvelope requestEnvelope = requestPart.getEnvelope();
	
			SOAPHeader requestHeader = requestEnvelope.getHeader();
			SOAPHeaderElement commissionEl = (SOAPHeaderElement) requestHeader.getChildElements(new QName("http://ws/", "commission", "ws")).next();
			if (commissionEl.getTextContent().equals("yes")) {
				commission = true;
			}
			requestHeader.detachNode();
			
			String endpoint = "http://127.0.0.1:8000/Calculator";
			SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
			SOAPConnection soapConnection = soapConnectionFactory.createConnection();
			SOAPMessage soapResponse = soapConnection.call(requestMessage, endpoint);
			soapConnection.close();
			
			SOAPBody responseBody = soapResponse.getSOAPBody();
			if (responseBody.hasFault()) {
			    System.out.println(responseBody.getFault().getFaultString()); 
			} else {
				SOAPHeader responseHeader = soapResponse.getSOAPHeader();
				if (responseHeader == null) {
					SOAPPart responsePart = soapResponse.getSOAPPart();
					SOAPEnvelope responseEnvelope = responsePart.getEnvelope();
					responseHeader = responseEnvelope.addHeader();
				}
				SOAPElement commissionedEl = responseHeader.addHeaderElement(new QName("http://ws/", "commissioned", "ws"));
				
				if (commission) {
					commissionedEl.setTextContent("yes");
					
					QName subtractResponseName = new QName("http://ws/", "subtractResponse", "ns2");
					QName returnName = new QName("return");
					SOAPBodyElement subtractResponseEl = (SOAPBodyElement)
							responseBody.getChildElements(subtractResponseName).next();			
					SOAPBodyElement returnEl = (SOAPBodyElement)
					       subtractResponseEl.getChildElements(returnName).next();
					
					int difference = Integer.valueOf(returnEl.getTextContent());
					difference = difference - 1;
					returnEl.setTextContent(Integer.toString(difference));	
				} else {
					commissionedEl.setTextContent("no");
				}
			}
			soapResponse.writeTo(response.getOutputStream());
		} catch (SOAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
