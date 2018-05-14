

package event.logging;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Captures data relevant to both copy and move data events.
 * 
 * <p>Java class for CopyMove complex type.
 * 
 * <p>The following schema fragment specifies the expected         content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CopyMove"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Source" type="{event-logging:3}MultiObject"/&gt;
 *         &lt;element name="Destination" type="{event-logging:3}MultiObject"/&gt;
 *         &lt;element name="Outcome" type="{event-logging:3}CopyMoveOutcome" minOccurs="0"/&gt;
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
@XmlType(name = "CopyMove", propOrder = {
    "source",
    "destination",
    "outcome",
    "data"
})
public class CopyMove {

    @XmlElement(name = "Source", required = true)
    protected MultiObject source;
    @XmlElement(name = "Destination", required = true)
    protected MultiObject destination;
    @XmlElement(name = "Outcome")
    protected CopyMoveOutcome outcome;
    @XmlElement(name = "Data")
    protected List<Data> data;

    /**
     * Gets the value of the source property.
     * 
     * @return
     *     possible object is
     *     {@link MultiObject }
     *     
     */
    public MultiObject getSource() {
        return source;
    }

    /**
     * Sets the value of the source property.
     * 
     * @param value
     *     allowed object is
     *     {@link MultiObject }
     *     
     */
    public void setSource(MultiObject value) {
        this.source = value;
    }

    /**
     * Gets the value of the destination property.
     * 
     * @return
     *     possible object is
     *     {@link MultiObject }
     *     
     */
    public MultiObject getDestination() {
        return destination;
    }

    /**
     * Sets the value of the destination property.
     * 
     * @param value
     *     allowed object is
     *     {@link MultiObject }
     *     
     */
    public void setDestination(MultiObject value) {
        this.destination = value;
    }

    /**
     * Gets the value of the outcome property.
     * 
     * @return
     *     possible object is
     *     {@link CopyMoveOutcome }
     *     
     */
    public CopyMoveOutcome getOutcome() {
        return outcome;
    }

    /**
     * Sets the value of the outcome property.
     * 
     * @param value
     *     allowed object is
     *     {@link CopyMoveOutcome }
     *     
     */
    public void setOutcome(CopyMoveOutcome value) {
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

}
