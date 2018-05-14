

package event.logging;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Purpose complex type.
 * 
 * <p>The following schema fragment specifies the expected         content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Purpose"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Classification" type="{event-logging:3}Classification" minOccurs="0"/&gt;
 *         &lt;element name="Subject" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Stakeholders" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Justification" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ExpectedOutcome" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Authorisations" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="Auth" maxOccurs="unbounded"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="RefNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="Detail" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
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
@XmlType(name = "Purpose", propOrder = {
    "classification",
    "subject",
    "stakeholders",
    "justification",
    "expectedOutcome",
    "authorisations",
    "data"
})
public class Purpose {

    @XmlElement(name = "Classification")
    protected Classification classification;
    @XmlElement(name = "Subject")
    protected String subject;
    @XmlElement(name = "Stakeholders")
    protected String stakeholders;
    @XmlElement(name = "Justification")
    protected String justification;
    @XmlElement(name = "ExpectedOutcome")
    protected String expectedOutcome;
    @XmlElement(name = "Authorisations")
    protected Purpose.Authorisations authorisations;
    @XmlElement(name = "Data")
    protected List<Data> data;

    /**
     * Gets the value of the classification property.
     * 
     * @return
     *     possible object is
     *     {@link Classification }
     *     
     */
    public Classification getClassification() {
        return classification;
    }

    /**
     * Sets the value of the classification property.
     * 
     * @param value
     *     allowed object is
     *     {@link Classification }
     *     
     */
    public void setClassification(Classification value) {
        this.classification = value;
    }

    /**
     * Gets the value of the subject property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Sets the value of the subject property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubject(String value) {
        this.subject = value;
    }

    /**
     * Gets the value of the stakeholders property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStakeholders() {
        return stakeholders;
    }

    /**
     * Sets the value of the stakeholders property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStakeholders(String value) {
        this.stakeholders = value;
    }

    /**
     * Gets the value of the justification property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJustification() {
        return justification;
    }

    /**
     * Sets the value of the justification property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJustification(String value) {
        this.justification = value;
    }

    /**
     * Gets the value of the expectedOutcome property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExpectedOutcome() {
        return expectedOutcome;
    }

    /**
     * Sets the value of the expectedOutcome property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExpectedOutcome(String value) {
        this.expectedOutcome = value;
    }

    /**
     * Gets the value of the authorisations property.
     * 
     * @return
     *     possible object is
     *     {@link Purpose.Authorisations }
     *     
     */
    public Purpose.Authorisations getAuthorisations() {
        return authorisations;
    }

    /**
     * Sets the value of the authorisations property.
     * 
     * @param value
     *     allowed object is
     *     {@link Purpose.Authorisations }
     *     
     */
    public void setAuthorisations(Purpose.Authorisations value) {
        this.authorisations = value;
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
     *       &lt;sequence&gt;
     *         &lt;element name="Auth" maxOccurs="unbounded"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="RefNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="Detail" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
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
        "auth"
    })
    public static class Authorisations {

        @XmlElement(name = "Auth", required = true)
        protected List<Purpose.Authorisations.Auth> auth;

        /**
         * Gets the value of the auth property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the auth property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getAuth().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Purpose.Authorisations.Auth }
         * 
         * 
         */
        public List<Purpose.Authorisations.Auth> getAuth() {
            if (auth == null) {
                auth = new ArrayList<Purpose.Authorisations.Auth>();
            }
            return this.auth;
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
         *         &lt;element name="RefNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="Detail" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
            "refNo",
            "detail"
        })
        public static class Auth {

            @XmlElement(name = "RefNo")
            protected String refNo;
            @XmlElement(name = "Detail")
            protected String detail;

            /**
             * Gets the value of the refNo property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getRefNo() {
                return refNo;
            }

            /**
             * Sets the value of the refNo property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setRefNo(String value) {
                this.refNo = value;
            }

            /**
             * Gets the value of the detail property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getDetail() {
                return detail;
            }

            /**
             * Sets the value of the detail property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setDetail(String value) {
                this.detail = value;
            }

        }

    }

}
