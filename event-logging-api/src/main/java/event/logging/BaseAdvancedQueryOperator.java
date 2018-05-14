

package event.logging;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BaseAdvancedQueryOperator complex type.
 * 
 * <p>The following schema fragment specifies the expected         content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BaseAdvancedQueryOperator"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{event-logging:3}BaseAdvancedQueryItem"&gt;
 *       &lt;group ref="{event-logging:3}AdvancedQueryOperatorGroup" maxOccurs="unbounded" minOccurs="0"/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BaseAdvancedQueryOperator", propOrder = {
    "advancedQueryItems"
})
@XmlSeeAlso({
    event.logging.Query.Advanced.And.class,
    event.logging.Query.Advanced.Or.class,
    event.logging.Query.Advanced.Not.class
})
public abstract class BaseAdvancedQueryOperator
    extends BaseAdvancedQueryItem
{

    @XmlElements({
        @XmlElement(name = "And", type = event.logging.Query.Advanced.And.class),
        @XmlElement(name = "Not", type = event.logging.Query.Advanced.Not.class),
        @XmlElement(name = "Or", type = event.logging.Query.Advanced.Or.class),
        @XmlElement(name = "Term", type = Term.class)
    })
    protected List<BaseAdvancedQueryItem> advancedQueryItems;

    /**
     * Gets the value of the advancedQueryItems property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the advancedQueryItems property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAdvancedQueryItems().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Term }
     * {@link event.logging.Query.Advanced.And }
     * {@link event.logging.Query.Advanced.Not }
     * {@link event.logging.Query.Advanced.Or }
     * 
     * 
     */
    public List<BaseAdvancedQueryItem> getAdvancedQueryItems() {
        if (advancedQueryItems == null) {
            advancedQueryItems = new ArrayList<BaseAdvancedQueryItem>();
        }
        return this.advancedQueryItems;
    }

}
