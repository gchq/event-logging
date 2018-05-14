

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
 * Structure used to describe a criteria used for search, view, update or delete.
 * 
 * <p>Java class for Criteria complex type.
 * 
 * <p>The following schema fragment specifies the expected         content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Criteria"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{event-logging:3}BaseObject"&gt;
 *       &lt;sequence&gt;
 *         &lt;group ref="{event-logging:3}CriteriaExtensionGroup"/&gt;
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
@XmlType(name = "Criteria", propOrder = {
    "dataSources",
    "query",
    "resultPage",
    "totalResults",
    "results",
    "data"
})
public class Criteria
    extends BaseObject
{

    @XmlElement(name = "DataSources")
    protected event.logging.Search.DataSources dataSources;
    @XmlElement(name = "Query")
    protected Query query;
    @XmlElement(name = "ResultPage")
    protected event.logging.Search.ResultPage resultPage;
    @XmlElement(name = "TotalResults")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger totalResults;
    @XmlElement(name = "Results")
    protected MultiObject results;
    @XmlElement(name = "Data")
    protected List<Data> data;

    /**
     * Gets the value of the dataSources property.
     * 
     * @return
     *     possible object is
     *     {@link event.logging.Search.DataSources }
     *     
     */
    public event.logging.Search.DataSources getDataSources() {
        return dataSources;
    }

    /**
     * Sets the value of the dataSources property.
     * 
     * @param value
     *     allowed object is
     *     {@link event.logging.Search.DataSources }
     *     
     */
    public void setDataSources(event.logging.Search.DataSources value) {
        this.dataSources = value;
    }

    /**
     * Gets the value of the query property.
     * 
     * @return
     *     possible object is
     *     {@link Query }
     *     
     */
    public Query getQuery() {
        return query;
    }

    /**
     * Sets the value of the query property.
     * 
     * @param value
     *     allowed object is
     *     {@link Query }
     *     
     */
    public void setQuery(Query value) {
        this.query = value;
    }

    /**
     * Gets the value of the resultPage property.
     * 
     * @return
     *     possible object is
     *     {@link event.logging.Search.ResultPage }
     *     
     */
    public event.logging.Search.ResultPage getResultPage() {
        return resultPage;
    }

    /**
     * Sets the value of the resultPage property.
     * 
     * @param value
     *     allowed object is
     *     {@link event.logging.Search.ResultPage }
     *     
     */
    public void setResultPage(event.logging.Search.ResultPage value) {
        this.resultPage = value;
    }

    /**
     * Gets the value of the totalResults property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTotalResults() {
        return totalResults;
    }

    /**
     * Sets the value of the totalResults property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTotalResults(BigInteger value) {
        this.totalResults = value;
    }

    /**
     * Gets the value of the results property.
     * 
     * @return
     *     possible object is
     *     {@link MultiObject }
     *     
     */
    public MultiObject getResults() {
        return results;
    }

    /**
     * Sets the value of the results property.
     * 
     * @param value
     *     allowed object is
     *     {@link MultiObject }
     *     
     */
    public void setResults(MultiObject value) {
        this.results = value;
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
