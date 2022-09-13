package servlet;

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
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

/**
 * Servlet implementation class CalculatorServlet
 */
@WebServlet("/CalculatorServlet")
public class CalculatorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CalculatorServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
		
		try {
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
			SOAPMessage soapr = soapc.call(soapm, endpoint);
			soapc.close();
			
			SOAPBody responseBody = soapr.getSOAPBody();
			if (responseBody.hasFault()) {
			    System.out.println(responseBody.getFault().getFaultString()); 
			} else {
				QName AddResponseName = new QName("http://tempuri.org/", "AddResponse");
				QName AddResultName = new QName("http://tempuri.org/", "AddResult");
		
				SOAPBodyElement AddResponse = (SOAPBodyElement)
				       responseBody.getChildElements(AddResponseName).next();
				SOAPBodyElement AddResult = (SOAPBodyElement)
				       AddResponse.getChildElements(AddResultName).next();
		
				response.getWriter().append(AddResult.getValue());
			}
		} catch (SOAPException soape) {
				response.getWriter().append(soape.getMessage());
				System.out.println(soape);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
