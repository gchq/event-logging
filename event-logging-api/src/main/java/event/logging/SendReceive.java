

package event.logging;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;


/**
 * Structure to describe the details of a send or receive event
 * 
 * <p>Java class for SendReceive complex type.
 * 
 * <p>The following schema fragment specifies the expected         content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SendReceive"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Source"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;choice maxOccurs="unbounded"&gt;
 *                   &lt;element name="User" type="{event-logging:3}User"/&gt;
 *                   &lt;element name="Device" type="{event-logging:3}Device"/&gt;
 *                 &lt;/choice&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="Destination"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;choice maxOccurs="unbounded"&gt;
 *                   &lt;element name="User" type="{event-logging:3}User"/&gt;
 *                   &lt;element name="Device" type="{event-logging:3}Device"/&gt;
 *                 &lt;/choice&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="Payload" type="{event-logging:3}MultiObject" minOccurs="0"/&gt;
 *         &lt;element name="Outcome" type="{event-logging:3}Outcome" minOccurs="0"/&gt;
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
@XmlType(name = "SendReceive", propOrder = {
    "source",
    "destination",
    "payload",
    "outcome",
    "data"
})
public class SendReceive {

    @XmlElement(name = "Source", required = true)
    protected SendReceive.Source source;
    @XmlElement(name = "Destination", required = true)
    protected SendReceive.Destination destination;
    @XmlElement(name = "Payload")
    protected MultiObject payload;
    @XmlElement(name = "Outcome")
    protected Outcome outcome;
    @XmlElement(name = "Data")
    protected List<Data> data;

    /**
     * Gets the value of the source property.
     * 
     * @return
     *     possible object is
     *     {@link SendReceive.Source }
     *     
     */
    public SendReceive.Source getSource() {
        return source;
    }

    /**
     * Sets the value of the source property.
     * 
     * @param value
     *     allowed object is
     *     {@link SendReceive.Source }
     *     
     */
    public void setSource(SendReceive.Source value) {
        this.source = value;
    }

    /**
     * Gets the value of the destination property.
     * 
     * @return
     *     possible object is
     *     {@link SendReceive.Destination }
     *     
     */
    public SendReceive.Destination getDestination() {
        return destination;
    }

    /**
     * Sets the value of the destination property.
     * 
     * @param value
     *     allowed object is
     *     {@link SendReceive.Destination }
     *     
     */
    public void setDestination(SendReceive.Destination value) {
        this.destination = value;
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
     * Gets the value of the outcome property.
     * 
     * @return
     *     possible object is
     *     {@link Outcome }
     *     
     */
    public Outcome getOutcome() {
        return outcome;
    }

    /**
     * Sets the value of the outcome property.
     * 
     * @param value
     *     allowed object is
     *     {@link Outcome }
     *     
     */
    public void setOutcome(Outcome value) {
        this.outcome = value;
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


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected         content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;choice maxOccurs="unbounded"&gt;
     *         &lt;element name="User" type="{event-logging:3}User"/&gt;
     *         &lt;element name="Device" type="{event-logging:3}Device"/&gt;
     *       &lt;/choice&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "userOrDevice"
    })
    public static class Destination {

        @XmlElements({
            @XmlElement(name = "Device", type = Device.class),
            @XmlElement(name = "User", type = User.class)
        })
        protected List<java.lang.Object> userOrDevice;

        /**
         * Gets the value of the userOrDevice property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the userOrDevice property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getUserOrDevice().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Device }
         * {@link User }
         * 
         * 
         */
        public List<java.lang.Object> getUserOrDevice() {
            if (userOrDevice == null) {
                userOrDevice = new ArrayList<java.lang.Object>();
            }
            return this.userOrDevice;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected         content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;choice maxOccurs="unbounded"&gt;
     *         &lt;element name="User" type="{event-logging:3}User"/&gt;
     *         &lt;element name="Device" type="{event-logging:3}Device"/&gt;
     *       &lt;/choice&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "userOrDevice"
    })
    public static class Source {

        @XmlElements({
            @XmlElement(name = "Device", type = Device.class),
            @XmlElement(name = "User", type = User.class)
        })
        protected List<java.lang.Object> userOrDevice;

        /**
         * Gets the value of the userOrDevice property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the userOrDevice property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getUserOrDevice().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Device }
         * {@link User }
         * 
         * 
         */
        public List<java.lang.Object> getUserOrDevice() {
            if (userOrDevice == null) {
                userOrDevice = new ArrayList<java.lang.Object>();
            }
            return this.userOrDevice;
        }

    }

}
