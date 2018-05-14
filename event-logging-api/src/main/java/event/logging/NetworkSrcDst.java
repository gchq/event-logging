

package event.logging;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * Desc
 * 
 * <p>Java class for NetworkSrcDst complex type.
 * 
 * <p>The following schema fragment specifies the expected         content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NetworkSrcDst"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Device" type="{event-logging:3}Device" minOccurs="0"/&gt;
 *         &lt;element name="Application" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="TransportProtocol" type="{event-logging:3}NetworkSrcDstTransportProtocol" minOccurs="0"/&gt;
 *         &lt;element name="ICMPType" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/&gt;
 *         &lt;element name="HTTPMethod" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ApplicationProtocol" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Port" type="{event-logging:3}Port" minOccurs="0"/&gt;
 *         &lt;element name="Data" type="{event-logging:3}Data" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NetworkSrcDst", propOrder = {
    "device",
    "application",
    "transportProtocol",
    "icmpType",
    "httpMethod",
    "applicationProtocol",
    "port",
    "data"
})
public class NetworkSrcDst {

    @XmlElement(name = "Device")
    protected Device device;
    @XmlElement(name = "Application")
    protected String application;
    @XmlElement(name = "TransportProtocol")
    @XmlSchemaType(name = "string")
    protected NetworkSrcDstTransportProtocol transportProtocol;
    @XmlElement(name = "ICMPType")
    protected BigInteger icmpType;
    @XmlElement(name = "HTTPMethod")
    protected String httpMethod;
    @XmlElement(name = "ApplicationProtocol")
    protected String applicationProtocol;
    @XmlElement(name = "Port")
    protected Integer port;
    @XmlElement(name = "Data")
    protected List<Data> data;

    /**
     * Gets the value of the device property.
     * 
     * @return
     *     possible object is
     *     {@link Device }
     *     
     */
    public Device getDevice() {
        return device;
    }

    /**
     * Sets the value of the device property.
     * 
     * @param value
     *     allowed object is
     *     {@link Device }
     *     
     */
    public void setDevice(Device value) {
        this.device = value;
    }

    /**
     * Gets the value of the application property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApplication() {
        return application;
    }

    /**
     * Sets the value of the application property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApplication(String value) {
        this.application = value;
    }

    /**
     * Gets the value of the transportProtocol property.
     * 
     * @return
     *     possible object is
     *     {@link NetworkSrcDstTransportProtocol }
     *     
     */
    public NetworkSrcDstTransportProtocol getTransportProtocol() {
        return transportProtocol;
    }

    /**
     * Sets the value of the transportProtocol property.
     * 
     * @param value
     *     allowed object is
     *     {@link NetworkSrcDstTransportProtocol }
     *     
     */
    public void setTransportProtocol(NetworkSrcDstTransportProtocol value) {
        this.transportProtocol = value;
    }

    /**
     * Gets the value of the icmpType property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getICMPType() {
        return icmpType;
    }

    /**
     * Sets the value of the icmpType property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setICMPType(BigInteger value) {
        this.icmpType = value;
    }

    /**
     * Gets the value of the httpMethod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHTTPMethod() {
        return httpMethod;
    }

    /**
     * Sets the value of the httpMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHTTPMethod(String value) {
        this.httpMethod = value;
    }

    /**
     * Gets the value of the applicationProtocol property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApplicationProtocol() {
        return applicationProtocol;
    }

    /**
     * Sets the value of the applicationProtocol property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApplicationProtocol(String value) {
        this.applicationProtocol = value;
    }

    /**
     * Gets the value of the port property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPort() {
        return port;
    }

    /**
     * Sets the value of the port property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPort(Integer value) {
        this.port = value;
    }

    /**
     * Gets the value of the data property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the data property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Data }
     * 
     * 
     */
    public List<Data> getData() {
        if (data == null) {
            data = new ArrayList<Data>();
        }
        return this.data;
    }

}
