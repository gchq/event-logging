

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
 * <p>Java class for Resource complex type.
 * 
 * <p>The following schema fragment specifies the expected         content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Resource"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{event-logging:3}BaseObject"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Title" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="URL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Referrer" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="SessionId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="HTTPMethod" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="HTTPVersion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="UserAgent" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="InboundSize" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/&gt;
 *         &lt;element name="InboundContentSize" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/&gt;
 *         &lt;element name="InboundHeader" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="OutboundSize" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/&gt;
 *         &lt;element name="OutboundContentSize" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/&gt;
 *         &lt;element name="OutboundHeader" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="RequestTime" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/&gt;
 *         &lt;element name="ConnectionStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="InitialResponseCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ResponseCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="MimeType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Category" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Data" type="{event-logging:3}Data" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Resource", propOrder = {
    "title",
    "url",
    "referrer",
    "sessionId",
    "httpMethod",
    "httpVersion",
    "userAgent",
    "inboundSize",
    "inboundContentSize",
    "inboundHeader",
    "outboundSize",
    "outboundContentSize",
    "outboundHeader",
    "requestTime",
    "connectionStatus",
    "initialResponseCode",
    "responseCode",
    "mimeType",
    "category",
    "data"
})
public class Resource
    extends BaseObject
{

    @XmlElement(name = "Title")
    protected String title;
    @XmlElement(name = "URL")
    protected String url;
    @XmlElement(name = "Referrer")
    protected String referrer;
    @XmlElement(name = "SessionId")
    protected String sessionId;
    @XmlElement(name = "HTTPMethod")
    protected String httpMethod;
    @XmlElement(name = "HTTPVersion")
    protected String httpVersion;
    @XmlElement(name = "UserAgent")
    protected String userAgent;
    @XmlElement(name = "InboundSize")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger inboundSize;
    @XmlElement(name = "InboundContentSize")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger inboundContentSize;
    @XmlElement(name = "InboundHeader")
    protected String inboundHeader;
    @XmlElement(name = "OutboundSize")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger outboundSize;
    @XmlElement(name = "OutboundContentSize")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger outboundContentSize;
    @XmlElement(name = "OutboundHeader")
    protected String outboundHeader;
    @XmlElement(name = "RequestTime")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger requestTime;
    @XmlElement(name = "ConnectionStatus")
    protected String connectionStatus;
    @XmlElement(name = "InitialResponseCode")
    protected String initialResponseCode;
    @XmlElement(name = "ResponseCode")
    protected String responseCode;
    @XmlElement(name = "MimeType")
    protected String mimeType;
    @XmlElement(name = "Category")
    protected String category;
    @XmlElement(name = "Data")
    protected List<Data> data;

    /**
     * Gets the value of the title property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitle(String value) {
        this.title = value;
    }

    /**
     * Gets the value of the url property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getURL() {
        return url;
    }

    /**
     * Sets the value of the url property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setURL(String value) {
        this.url = value;
    }

    /**
     * Gets the value of the referrer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReferrer() {
        return referrer;
    }

    /**
     * Sets the value of the referrer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReferrer(String value) {
        this.referrer = value;
    }

    /**
     * Gets the value of the sessionId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * Sets the value of the sessionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSessionId(String value) {
        this.sessionId = value;
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
     * Gets the value of the httpVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHTTPVersion() {
        return httpVersion;
    }

    /**
     * Sets the value of the httpVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHTTPVersion(String value) {
        this.httpVersion = value;
    }

    /**
     * Gets the value of the userAgent property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserAgent() {
        return userAgent;
    }

    /**
     * Sets the value of the userAgent property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserAgent(String value) {
        this.userAgent = value;
    }

    /**
     * Gets the value of the inboundSize property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getInboundSize() {
        return inboundSize;
    }

    /**
     * Sets the value of the inboundSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setInboundSize(BigInteger value) {
        this.inboundSize = value;
    }

    /**
     * Gets the value of the inboundContentSize property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getInboundContentSize() {
        return inboundContentSize;
    }

    /**
     * Sets the value of the inboundContentSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setInboundContentSize(BigInteger value) {
        this.inboundContentSize = value;
    }

    /**
     * Gets the value of the inboundHeader property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInboundHeader() {
        return inboundHeader;
    }

    /**
     * Sets the value of the inboundHeader property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInboundHeader(String value) {
        this.inboundHeader = value;
    }

    /**
     * Gets the value of the outboundSize property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getOutboundSize() {
        return outboundSize;
    }

    /**
     * Sets the value of the outboundSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setOutboundSize(BigInteger value) {
        this.outboundSize = value;
    }

    /**
     * Gets the value of the outboundContentSize property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getOutboundContentSize() {
        return outboundContentSize;
    }

    /**
     * Sets the value of the outboundContentSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setOutboundContentSize(BigInteger value) {
        this.outboundContentSize = value;
    }

    /**
     * Gets the value of the outboundHeader property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOutboundHeader() {
        return outboundHeader;
    }

    /**
     * Sets the value of the outboundHeader property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOutboundHeader(String value) {
        this.outboundHeader = value;
    }

    /**
     * Gets the value of the requestTime property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getRequestTime() {
        return requestTime;
    }

    /**
     * Sets the value of the requestTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setRequestTime(BigInteger value) {
        this.requestTime = value;
    }

    /**
     * Gets the value of the connectionStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConnectionStatus() {
        return connectionStatus;
    }

    /**
     * Sets the value of the connectionStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConnectionStatus(String value) {
        this.connectionStatus = value;
    }

    /**
     * Gets the value of the initialResponseCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInitialResponseCode() {
        return initialResponseCode;
    }

    /**
     * Sets the value of the initialResponseCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInitialResponseCode(String value) {
        this.initialResponseCode = value;
    }

    /**
     * Gets the value of the responseCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResponseCode() {
        return responseCode;
    }

    /**
     * Sets the value of the responseCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResponseCode(String value) {
        this.responseCode = value;
    }

    /**
     * Gets the value of the mimeType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     * Sets the value of the mimeType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMimeType(String value) {
        this.mimeType = value;
    }

    /**
     * Gets the value of the category property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the value of the category property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCategory(String value) {
        this.category = value;
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
