

package event.logging;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for VirtualSession complex type.
 * 
 * <p>The following schema fragment specifies the expected         content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="VirtualSession"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{event-logging:3}BaseObject"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="SessionState" type="{event-logging:3}VirtualSessionSessionState" minOccurs="0"/&gt;
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
@XmlType(name = "VirtualSession", propOrder = {
    "sessionState",
    "data"
})
public class VirtualSession
    extends BaseObject
{

    @XmlElement(name = "SessionState")
    @XmlSchemaType(name = "string")
    protected VirtualSessionSessionState sessionState;
    @XmlElement(name = "Data")
    protected List<Data> data;

    /**
     * Gets the value of the sessionState property.
     * 
     * @return
     *     possible object is
     *     {@link VirtualSessionSessionState }
     *     
     */
    public VirtualSessionSessionState getSessionState() {
        return sessionState;
    }

    /**
     * Sets the value of the sessionState property.
     * 
     * @param value
     *     allowed object is
     *     {@link VirtualSessionSessionState }
     *     
     */
    public void setSessionState(VirtualSessionSessionState value) {
        this.sessionState = value;
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
