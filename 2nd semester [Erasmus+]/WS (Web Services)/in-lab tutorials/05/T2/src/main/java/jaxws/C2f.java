
package defaultnamespace.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This class was generated by Apache CXF 3.5.1
 * Thu Mar 17 13:14:49 CET 2022
 * Generated source version: 3.5.1
 */

@XmlRootElement(name = "c2f", namespace = "http://default_package/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "c2f", namespace = "http://default_package/")

public class C2f {

    @XmlElement(name = "arg0")
    private double arg0;

    public double getArg0() {
        return this.arg0;
    }

    public void setArg0(double newArg0)  {
        this.arg0 = newArg0;
    }

}

