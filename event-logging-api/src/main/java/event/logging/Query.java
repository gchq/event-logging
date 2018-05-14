

package event.logging;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Query complex type.
 * 
 * <p>The following schema fragment specifies the expected         content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Query"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Advanced" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;group ref="{event-logging:3}AdvancedQueryOperatorGroup" maxOccurs="unbounded" minOccurs="0"/&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="Simple" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="Include" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="Exclude" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="Raw" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Query", propOrder = {
    "advanced",
    "simple",
    "raw"
})
public class Query {

    @XmlElement(name = "Advanced")
    protected Query.Advanced advanced;
    @XmlElement(name = "Simple")
    protected Query.Simple simple;
    @XmlElement(name = "Raw")
    protected String raw;

    /**
     * Gets the value of the advanced property.
     * 
     * @return
     *     possible object is
     *     {@link Query.Advanced }
     *     
     */
    public Query.Advanced getAdvanced() {
        return advanced;
    }

    /**
     * Sets the value of the advanced property.
     * 
     * @param value
     *     allowed object is
     *     {@link Query.Advanced }
     *     
     */
    public void setAdvanced(Query.Advanced value) {
        this.advanced = value;
    }

    /**
     * Gets the value of the simple property.
     * 
     * @return
     *     possible object is
     *     {@link Query.Simple }
     *     
     */
    public Query.Simple getSimple() {
        return simple;
    }

    /**
     * Sets the value of the simple property.
     * 
     * @param value
     *     allowed object is
     *     {@link Query.Simple }
     *     
     */
    public void setSimple(Query.Simple value) {
        this.simple = value;
    }

    /**
     * Gets the value of the raw property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRaw() {
        return raw;
    }

    /**
     * Sets the value of the raw property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRaw(String value) {
        this.raw = value;
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
     *       &lt;group ref="{event-logging:3}AdvancedQueryOperatorGroup" maxOccurs="unbounded" minOccurs="0"/&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "advancedQueryItems"
    })
    public static class Advanced {

        @XmlElements({
            @XmlElement(name = "And", type = Query.Advanced.And.class),
            @XmlElement(name = "Not", type = Query.Advanced.Not.class),
            @XmlElement(name = "Or", type = Query.Advanced.Or.class),
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
         * {@link Query.Advanced.And }
         * {@link Query.Advanced.Not }
         * {@link Query.Advanced.Or }
         * {@link Term }
         * 
         * 
         */
        public List<BaseAdvancedQueryItem> getAdvancedQueryItems() {
            if (advancedQueryItems == null) {
                advancedQueryItems = new ArrayList<BaseAdvancedQueryItem>();
            }
            return this.advancedQueryItems;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected         content contained within this class.
         * 
         * <pre>
         * &lt;complexType&gt;
         *   &lt;complexContent&gt;
         *     &lt;extension base="{event-logging:3}BaseAdvancedQueryOperator"&gt;
         *     &lt;/extension&gt;
         *   &lt;/complexContent&gt;
         * &lt;/complexType&gt;
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class And
            extends BaseAdvancedQueryOperator
        {


        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected         content contained within this class.
         * 
         * <pre>
         * &lt;complexType&gt;
         *   &lt;complexContent&gt;
         *     &lt;extension base="{event-logging:3}BaseAdvancedQueryOperator"&gt;
         *     &lt;/extension&gt;
         *   &lt;/complexContent&gt;
         * &lt;/complexType&gt;
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Not
            extends BaseAdvancedQueryOperator
        {


        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected         content contained within this class.
         * 
         * <pre>
         * &lt;complexType&gt;
         *   &lt;complexContent&gt;
         *     &lt;extension base="{event-logging:3}BaseAdvancedQueryOperator"&gt;
         *     &lt;/extension&gt;
         *   &lt;/complexContent&gt;
         * &lt;/complexType&gt;
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Or
            extends BaseAdvancedQueryOperator
        {


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
     *       &lt;sequence&gt;
     *         &lt;element name="Include" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="Exclude" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "include",
        "exclude"
    })
    public static class Simple {

        @XmlElement(name = "Include")
        protected String include;
        @XmlElement(name = "Exclude")
        protected String exclude;

        /**
         * Gets the value of the include property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getInclude() {
            return include;
        }

        /**
         * Sets the value of the include property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setInclude(String value) {
            this.include = value;
        }

        /**
         * Gets the value of the exclude property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getExclude() {
            return exclude;
        }

        /**
         * Sets the value of the exclude property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setExclude(String value) {
            this.exclude = value;
        }

    }

}
