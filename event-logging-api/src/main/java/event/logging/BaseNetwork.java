

package event.logging;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BaseNetwork complex type.
 * 
 * <p>The following schema fragment specifies the expected         content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BaseNetwork"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Source" type="{event-logging:3}NetworkSrcDst"/&gt;
 *         &lt;element name="Destination" type="{event-logging:3}NetworkSrcDst" minOccurs="0"/&gt;
 *         &lt;element name="ProcessName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Payload" type="{event-logging:3}MultiObject" minOccurs="0"/&gt;
 *         &lt;element name="Rule" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BaseNetwork", propOrder = {
    "source",
    "destination",
    "processName",
    "payload",
    "rule"
})
@XmlSeeAlso({
    Network.class
})
public abstract class BaseNetwork {

    @XmlElement(name = "Source", required = true)
    protected NetworkSrcDst source;
    @XmlElement(name = "Destination")
    protected NetworkSrcDst destination;
    @XmlElement(name = "ProcessName")
    protected String processName;
    @XmlElement(name = "Payload")
    protected MultiObject payload;
    @XmlElement(name = "Rule")
    protected String rule;

    /**
     * Gets the value of the source property.
     * 
     * @return
     *     possible object is
     *     {@link NetworkSrcDst }
     *     
     */
    public NetworkSrcDst getSource() {
        return source;
    }

    /**
     * Sets the value of the source property.
     * 
     * @param value
     *     allowed object is
     *     {@link NetworkSrcDst }
     *     
     */
    public void setSource(NetworkSrcDst value) {
        this.source = value;
    }

    /**
     * Gets the value of the destination property.
     * 
     * @return
     *     possible object is
     *     {@link NetworkSrcDst }
     *     
     */
    public NetworkSrcDst getDestination() {
        return destination;
    }

    /**
     * Sets the value of the destination property.
     * 
     * @param value
     *     allowed object is
     *     {@link NetworkSrcDst }
     *     
     */
    public void setDestination(NetworkSrcDst value) {
        this.destination = value;
    }

    /**
     * Gets the value of the processName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProcessName() {
        return processName;
    }

    /**
     * Sets the value of the processName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProcessName(String value) {
        this.processName = value;
    }

    /**
     * Gets the value of the payload property.
     * 
     * @return
     *     possible object is
     *     {@link MultiObject }
     *     
     */
    public MultiObject getPayload() {
        return payload;
    }

    /**
     * Sets the value of the payload property.
     * 
     * @param value
     *     allowed object is
     *     {@link MultiObject }
     *     
     */
    public void setPayload(MultiObject value) {
        this.payload = value;
    }

    /**
     * Gets the value of the rule property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRule() {
        return rule;
    }

    /**
     * Sets the value of the rule property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRule(String value) {
        this.rule = value;
    }

}
