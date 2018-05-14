

package event.logging;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


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
 *         &lt;element name="Classification" type="{event-logging:3}Classification" minOccurs="0"/&gt;
 *         &lt;element name="EventTime"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="TimeCreated" type="{event-logging:3}DateTime"/&gt;
 *                   &lt;element name="TimeSource" type="{event-logging:3}Device" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="EventSource"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="System" type="{event-logging:3}System"/&gt;
 *                   &lt;element name="Generator" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;choice&gt;
 *                     &lt;sequence&gt;
 *                       &lt;element name="Device" type="{event-logging:3}Device"/&gt;
 *                       &lt;element name="Client" type="{event-logging:3}Device" minOccurs="0"/&gt;
 *                       &lt;element name="Server" type="{event-logging:3}Device" minOccurs="0"/&gt;
 *                     &lt;/sequence&gt;
 *                     &lt;sequence&gt;
 *                       &lt;element name="Door"&gt;
 *                         &lt;complexType&gt;
 *                           &lt;complexContent&gt;
 *                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                               &lt;sequence&gt;
 *                                 &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                                 &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                                 &lt;element name="Location" type="{event-logging:3}Location"/&gt;
 *                                 &lt;element name="SingleEntry" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *                                 &lt;element name="RemoveAll" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *                                 &lt;element name="AddAccess"&gt;
 *                                   &lt;complexType&gt;
 *                                     &lt;complexContent&gt;
 *                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                         &lt;sequence&gt;
 *                                           &lt;element name="AccessZone" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded"/&gt;
 *                                         &lt;/sequence&gt;
 *                                       &lt;/restriction&gt;
 *                                     &lt;/complexContent&gt;
 *                                   &lt;/complexType&gt;
 *                                 &lt;/element&gt;
 *                               &lt;/sequence&gt;
 *                             &lt;/restriction&gt;
 *                           &lt;/complexContent&gt;
 *                         &lt;/complexType&gt;
 *                       &lt;/element&gt;
 *                     &lt;/sequence&gt;
 *                   &lt;/choice&gt;
 *                   &lt;element name="User" type="{event-logging:3}User" minOccurs="0"/&gt;
 *                   &lt;element name="RunAs" type="{event-logging:3}User" minOccurs="0"/&gt;
 *                   &lt;element name="Interactive" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *                   &lt;element name="Data" type="{event-logging:3}Data" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="EventDetail"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="TypeId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="Classification" type="{event-logging:3}Classification" minOccurs="0"/&gt;
 *                   &lt;element name="Purpose" type="{event-logging:3}Purpose" minOccurs="0"/&gt;
 *                   &lt;choice&gt;
 *                     &lt;element name="Authenticate"&gt;
 *                       &lt;complexType&gt;
 *                         &lt;complexContent&gt;
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                             &lt;sequence&gt;
 *                               &lt;element name="Action" type="{event-logging:3}AuthenticateAction"/&gt;
 *                               &lt;element name="LogonType" type="{event-logging:3}AuthenticateLogonType" minOccurs="0"/&gt;
 *                               &lt;choice&gt;
 *                                 &lt;element name="User" type="{event-logging:3}User"/&gt;
 *                                 &lt;element name="Device" type="{event-logging:3}Device"/&gt;
 *                                 &lt;element name="Group" type="{event-logging:3}Group"/&gt;
 *                               &lt;/choice&gt;
 *                               &lt;element name="Outcome" type="{event-logging:3}AuthenticateOutcome" minOccurs="0"/&gt;
 *                               &lt;element name="Data" type="{event-logging:3}Data" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                             &lt;/sequence&gt;
 *                           &lt;/restriction&gt;
 *                         &lt;/complexContent&gt;
 *                       &lt;/complexType&gt;
 *                     &lt;/element&gt;
 *                     &lt;element name="Authorise"&gt;
 *                       &lt;complexType&gt;
 *                         &lt;complexContent&gt;
 *                           &lt;extension base="{event-logging:3}BaseMultiObject"&gt;
 *                             &lt;sequence&gt;
 *                               &lt;element name="Action" type="{event-logging:3}Authorisation" minOccurs="0"/&gt;
 *                               &lt;element name="AddGroups" minOccurs="0"&gt;
 *                                 &lt;complexType&gt;
 *                                   &lt;complexContent&gt;
 *                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                       &lt;sequence&gt;
 *                                         &lt;element name="Group" type="{event-logging:3}Group" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                                       &lt;/sequence&gt;
 *                                     &lt;/restriction&gt;
 *                                   &lt;/complexContent&gt;
 *                                 &lt;/complexType&gt;
 *                               &lt;/element&gt;
 *                               &lt;element name="RemoveGroups" minOccurs="0"&gt;
 *                                 &lt;complexType&gt;
 *                                   &lt;complexContent&gt;
 *                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                       &lt;sequence&gt;
 *                                         &lt;element name="Group" type="{event-logging:3}Group" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                                       &lt;/sequence&gt;
 *                                     &lt;/restriction&gt;
 *                                   &lt;/complexContent&gt;
 *                                 &lt;/complexType&gt;
 *                               &lt;/element&gt;
 *                               &lt;element name="Outcome" type="{event-logging:3}Outcome" minOccurs="0"/&gt;
 *                               &lt;element name="Data" type="{event-logging:3}Data" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                             &lt;/sequence&gt;
 *                           &lt;/extension&gt;
 *                         &lt;/complexContent&gt;
 *                       &lt;/complexType&gt;
 *                     &lt;/element&gt;
 *                     &lt;element name="Search" type="{event-logging:3}Search"/&gt;
 *                     &lt;element name="Copy" type="{event-logging:3}CopyMove"/&gt;
 *                     &lt;element name="Move" type="{event-logging:3}CopyMove"/&gt;
 *                     &lt;element name="Create" type="{event-logging:3}ObjectOutcome"/&gt;
 *                     &lt;element name="View" type="{event-logging:3}ObjectOutcome"/&gt;
 *                     &lt;element name="Import" type="{event-logging:3}Import"/&gt;
 *                     &lt;element name="Export" type="{event-logging:3}Export"/&gt;
 *                     &lt;element name="Update"&gt;
 *                       &lt;complexType&gt;
 *                         &lt;complexContent&gt;
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                             &lt;sequence&gt;
 *                               &lt;element name="Before" type="{event-logging:3}MultiObject" minOccurs="0"/&gt;
 *                               &lt;element name="After" type="{event-logging:3}MultiObject"/&gt;
 *                               &lt;element name="Outcome" type="{event-logging:3}Outcome" minOccurs="0"/&gt;
 *                               &lt;element name="Data" type="{event-logging:3}Data" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                             &lt;/sequence&gt;
 *                           &lt;/restriction&gt;
 *                         &lt;/complexContent&gt;
 *                       &lt;/complexType&gt;
 *                     &lt;/element&gt;
 *                     &lt;element name="Delete" type="{event-logging:3}ObjectOutcome"/&gt;
 *                     &lt;element name="Process"&gt;
 *                       &lt;complexType&gt;
 *                         &lt;complexContent&gt;
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                             &lt;sequence&gt;
 *                               &lt;element name="Action" type="{event-logging:3}ProcessAction"/&gt;
 *                               &lt;element name="Type" type="{event-logging:3}ProcessType"/&gt;
 *                               &lt;element name="Command" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                               &lt;element name="Arguments" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                               &lt;element name="ProcessId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                               &lt;element name="ThreadId" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" minOccurs="0"/&gt;
 *                               &lt;element name="Rule" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                               &lt;element name="Input" type="{event-logging:3}MultiObject" minOccurs="0"/&gt;
 *                               &lt;element name="Output" type="{event-logging:3}MultiObject" minOccurs="0"/&gt;
 *                               &lt;element name="Outcome" type="{event-logging:3}Outcome" minOccurs="0"/&gt;
 *                               &lt;element name="Data" type="{event-logging:3}Data" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                             &lt;/sequence&gt;
 *                           &lt;/restriction&gt;
 *                         &lt;/complexContent&gt;
 *                       &lt;/complexType&gt;
 *                     &lt;/element&gt;
 *                     &lt;element name="Print"&gt;
 *                       &lt;complexType&gt;
 *                         &lt;complexContent&gt;
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                             &lt;sequence&gt;
 *                               &lt;element name="Action" type="{event-logging:3}PrintAction"/&gt;
 *                               &lt;element name="PrintJob"&gt;
 *                                 &lt;complexType&gt;
 *                                   &lt;complexContent&gt;
 *                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                       &lt;sequence&gt;
 *                                         &lt;element name="Document" type="{event-logging:3}Document"/&gt;
 *                                         &lt;element name="Pages" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/&gt;
 *                                         &lt;element name="Size" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/&gt;
 *                                         &lt;element name="Submitted" type="{event-logging:3}DateTime" minOccurs="0"/&gt;
 *                                       &lt;/sequence&gt;
 *                                     &lt;/restriction&gt;
 *                                   &lt;/complexContent&gt;
 *                                 &lt;/complexType&gt;
 *                               &lt;/element&gt;
 *                               &lt;element name="PrintSettings" minOccurs="0"&gt;
 *                                 &lt;complexType&gt;
 *                                   &lt;complexContent&gt;
 *                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                       &lt;sequence&gt;
 *                                         &lt;element name="PaperSize" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                                         &lt;element name="Orientation" type="{event-logging:3}PrintSettingsOrientation" minOccurs="0"/&gt;
 *                                         &lt;element name="Colour" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *                                         &lt;element name="DeviceFonts" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *                                         &lt;element name="Data" type="{event-logging:3}Data" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                                       &lt;/sequence&gt;
 *                                     &lt;/restriction&gt;
 *                                   &lt;/complexContent&gt;
 *                                 &lt;/complexType&gt;
 *                               &lt;/element&gt;
 *                               &lt;element name="Printer" type="{event-logging:3}Device" minOccurs="0"/&gt;
 *                               &lt;element name="Outcome" type="{event-logging:3}Outcome" minOccurs="0"/&gt;
 *                               &lt;element name="Data" type="{event-logging:3}Data" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                             &lt;/sequence&gt;
 *                           &lt;/restriction&gt;
 *                         &lt;/complexContent&gt;
 *                       &lt;/complexType&gt;
 *                     &lt;/element&gt;
 *                     &lt;element name="Install" type="{event-logging:3}Install"/&gt;
 *                     &lt;element name="Uninstall" type="{event-logging:3}Install"/&gt;
 *                     &lt;element name="Network"&gt;
 *                       &lt;complexType&gt;
 *                         &lt;complexContent&gt;
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                             &lt;choice&gt;
 *                               &lt;element name="Bind" type="{event-logging:3}Network"/&gt;
 *                               &lt;element name="Connect" type="{event-logging:3}Network"/&gt;
 *                               &lt;element name="Open" type="{event-logging:3}Network"/&gt;
 *                               &lt;element name="Close" type="{event-logging:3}Network"/&gt;
 *                               &lt;element name="Send" type="{event-logging:3}Network"/&gt;
 *                               &lt;element name="Receive" type="{event-logging:3}Network"/&gt;
 *                               &lt;element name="Listen" type="{event-logging:3}Network"/&gt;
 *                               &lt;element name="Permit" type="{event-logging:3}Network"/&gt;
 *                               &lt;element name="Deny" type="{event-logging:3}Network"/&gt;
 *                             &lt;/choice&gt;
 *                           &lt;/restriction&gt;
 *                         &lt;/complexContent&gt;
 *                       &lt;/complexType&gt;
 *                     &lt;/element&gt;
 *                     &lt;element name="AntiMalware"&gt;
 *                       &lt;complexType&gt;
 *                         &lt;complexContent&gt;
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                             &lt;sequence&gt;
 *                               &lt;choice&gt;
 *                                 &lt;element name="StartScan" type="{event-logging:3}AntiMalware"/&gt;
 *                                 &lt;element name="StopScan" type="{event-logging:3}AntiMalware"/&gt;
 *                                 &lt;element name="Admin" type="{event-logging:3}AntiMalware"/&gt;
 *                                 &lt;element name="Quarantine" type="{event-logging:3}AntiMalwareThreat"/&gt;
 *                                 &lt;element name="Report" type="{event-logging:3}AntiMalware"/&gt;
 *                                 &lt;element name="Email" type="{event-logging:3}AntiMalware"/&gt;
 *                                 &lt;element name="Delete" type="{event-logging:3}AntiMalwareThreat"/&gt;
 *                                 &lt;element name="ScanEngineUpdated" type="{event-logging:3}AntiMalware"/&gt;
 *                                 &lt;element name="SignaturesUpdated" type="{event-logging:3}AntiMalware"/&gt;
 *                               &lt;/choice&gt;
 *                               &lt;element name="Outcome" type="{event-logging:3}Outcome" minOccurs="0"/&gt;
 *                               &lt;element name="Data" type="{event-logging:3}Data" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                             &lt;/sequence&gt;
 *                           &lt;/restriction&gt;
 *                         &lt;/complexContent&gt;
 *                       &lt;/complexType&gt;
 *                     &lt;/element&gt;
 *                     &lt;element name="Alert"&gt;
 *                       &lt;complexType&gt;
 *                         &lt;complexContent&gt;
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                             &lt;sequence&gt;
 *                               &lt;element name="Type" type="{event-logging:3}AlertType"/&gt;
 *                               &lt;element name="Severity" type="{event-logging:3}AlertSeverity" minOccurs="0"/&gt;
 *                               &lt;element name="Priority" type="{event-logging:3}AlertPriority" minOccurs="0"/&gt;
 *                               &lt;element name="Subject" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                               &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                               &lt;element name="IDS" minOccurs="0"&gt;
 *                                 &lt;complexType&gt;
 *                                   &lt;complexContent&gt;
 *                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                       &lt;sequence&gt;
 *                                         &lt;element name="Rule" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                                         &lt;element name="Source" type="{event-logging:3}NetworkSrcDst" minOccurs="0"/&gt;
 *                                         &lt;element name="Destination" type="{event-logging:3}NetworkSrcDst" minOccurs="0"/&gt;
 *                                         &lt;element name="Payload" type="{event-logging:3}MultiObject" minOccurs="0"/&gt;
 *                                       &lt;/sequence&gt;
 *                                     &lt;/restriction&gt;
 *                                   &lt;/complexContent&gt;
 *                                 &lt;/complexType&gt;
 *                               &lt;/element&gt;
 *                               &lt;element name="Malware" type="{event-logging:3}AntiMalwareThreat" minOccurs="0"/&gt;
 *                               &lt;element name="Network" type="{event-logging:3}BaseNetwork" minOccurs="0"/&gt;
 *                               &lt;element name="Change" minOccurs="0"&gt;
 *                                 &lt;complexType&gt;
 *                                   &lt;complexContent&gt;
 *                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                       &lt;sequence&gt;
 *                                         &lt;element name="Action" type="{event-logging:3}ChangeAction"/&gt;
 *                                         &lt;element name="Before" type="{event-logging:3}MultiObject" minOccurs="0"/&gt;
 *                                         &lt;element name="After" type="{event-logging:3}MultiObject" minOccurs="0"/&gt;
 *                                         &lt;element name="Rule" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                                       &lt;/sequence&gt;
 *                                     &lt;/restriction&gt;
 *                                   &lt;/complexContent&gt;
 *                                 &lt;/complexType&gt;
 *                               &lt;/element&gt;
 *                               &lt;element name="Data" type="{event-logging:3}Data" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                             &lt;/sequence&gt;
 *                           &lt;/restriction&gt;
 *                         &lt;/complexContent&gt;
 *                       &lt;/complexType&gt;
 *                     &lt;/element&gt;
 *                     &lt;element name="Send" type="{event-logging:3}SendReceive"/&gt;
 *                     &lt;element name="Receive" type="{event-logging:3}SendReceive"/&gt;
 *                     &lt;element name="Unknown"&gt;
 *                       &lt;complexType&gt;
 *                         &lt;complexContent&gt;
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                             &lt;sequence&gt;
 *                               &lt;element name="Data" type="{event-logging:3}Data" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                             &lt;/sequence&gt;
 *                           &lt;/restriction&gt;
 *                         &lt;/complexContent&gt;
 *                       &lt;/complexType&gt;
 *                     &lt;/element&gt;
 *                   &lt;/choice&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="EventChain" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="Activity" type="{event-logging:3}Activity"/&gt;
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
@XmlType(name = "", propOrder = {
    "classification",
    "eventTime",
    "eventSource",
    "eventDetail",
    "eventChain",
    "data"
})
@XmlRootElement(name = "Event")
public class Event {

    @XmlElement(name = "Classification")
    protected Classification classification;
    @XmlElement(name = "EventTime", required = true)
    protected Event.EventTime eventTime;
    @XmlElement(name = "EventSource", required = true)
    protected Event.EventSource eventSource;
    @XmlElement(name = "EventDetail", required = true)
    protected Event.EventDetail eventDetail;
    @XmlElement(name = "EventChain")
    protected Event.EventChain eventChain;
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
     * Gets the value of the eventTime property.
     * 
     * @return
     *     possible object is
     *     {@link Event.EventTime }
     *     
     */
    public Event.EventTime getEventTime() {
        return eventTime;
    }

    /**
     * Sets the value of the eventTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link Event.EventTime }
     *     
     */
    public void setEventTime(Event.EventTime value) {
        this.eventTime = value;
    }

    /**
     * Gets the value of the eventSource property.
     * 
     * @return
     *     possible object is
     *     {@link Event.EventSource }
     *     
     */
    public Event.EventSource getEventSource() {
        return eventSource;
    }

    /**
     * Sets the value of the eventSource property.
     * 
     * @param value
     *     allowed object is
     *     {@link Event.EventSource }
     *     
     */
    public void setEventSource(Event.EventSource value) {
        this.eventSource = value;
    }

    /**
     * Gets the value of the eventDetail property.
     * 
     * @return
     *     possible object is
     *     {@link Event.EventDetail }
     *     
     */
    public Event.EventDetail getEventDetail() {
        return eventDetail;
    }

    /**
     * Sets the value of the eventDetail property.
     * 
     * @param value
     *     allowed object is
     *     {@link Event.EventDetail }
     *     
     */
    public void setEventDetail(Event.EventDetail value) {
        this.eventDetail = value;
    }

    /**
     * Gets the value of the eventChain property.
     * 
     * @return
     *     possible object is
     *     {@link Event.EventChain }
     *     
     */
    public Event.EventChain getEventChain() {
        return eventChain;
    }

    /**
     * Sets the value of the eventChain property.
     * 
     * @param value
     *     allowed object is
     *     {@link Event.EventChain }
     *     
     */
    public void setEventChain(Event.EventChain value) {
        this.eventChain = value;
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
     *         &lt;element name="Activity" type="{event-logging:3}Activity"/&gt;
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
        "activity"
    })
    public static class EventChain {

        @XmlElement(name = "Activity", required = true)
        protected Activity activity;

        /**
         * Gets the value of the activity property.
         * 
         * @return
         *     possible object is
         *     {@link Activity }
         *     
         */
        public Activity getActivity() {
            return activity;
        }

        /**
         * Sets the value of the activity property.
         * 
         * @param value
         *     allowed object is
         *     {@link Activity }
         *     
         */
        public void setActivity(Activity value) {
            this.activity = value;
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
     *         &lt;element name="TypeId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="Classification" type="{event-logging:3}Classification" minOccurs="0"/&gt;
     *         &lt;element name="Purpose" type="{event-logging:3}Purpose" minOccurs="0"/&gt;
     *         &lt;choice&gt;
     *           &lt;element name="Authenticate"&gt;
     *             &lt;complexType&gt;
     *               &lt;complexContent&gt;
     *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                   &lt;sequence&gt;
     *                     &lt;element name="Action" type="{event-logging:3}AuthenticateAction"/&gt;
     *                     &lt;element name="LogonType" type="{event-logging:3}AuthenticateLogonType" minOccurs="0"/&gt;
     *                     &lt;choice&gt;
     *                       &lt;element name="User" type="{event-logging:3}User"/&gt;
     *                       &lt;element name="Device" type="{event-logging:3}Device"/&gt;
     *                       &lt;element name="Group" type="{event-logging:3}Group"/&gt;
     *                     &lt;/choice&gt;
     *                     &lt;element name="Outcome" type="{event-logging:3}AuthenticateOutcome" minOccurs="0"/&gt;
     *                     &lt;element name="Data" type="{event-logging:3}Data" maxOccurs="unbounded" minOccurs="0"/&gt;
     *                   &lt;/sequence&gt;
     *                 &lt;/restriction&gt;
     *               &lt;/complexContent&gt;
     *             &lt;/complexType&gt;
     *           &lt;/element&gt;
     *           &lt;element name="Authorise"&gt;
     *             &lt;complexType&gt;
     *               &lt;complexContent&gt;
     *                 &lt;extension base="{event-logging:3}BaseMultiObject"&gt;
     *                   &lt;sequence&gt;
     *                     &lt;element name="Action" type="{event-logging:3}Authorisation" minOccurs="0"/&gt;
     *                     &lt;element name="AddGroups" minOccurs="0"&gt;
     *                       &lt;complexType&gt;
     *                         &lt;complexContent&gt;
     *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                             &lt;sequence&gt;
     *                               &lt;element name="Group" type="{event-logging:3}Group" maxOccurs="unbounded" minOccurs="0"/&gt;
     *                             &lt;/sequence&gt;
     *                           &lt;/restriction&gt;
     *                         &lt;/complexContent&gt;
     *                       &lt;/complexType&gt;
     *                     &lt;/element&gt;
     *                     &lt;element name="RemoveGroups" minOccurs="0"&gt;
     *                       &lt;complexType&gt;
     *                         &lt;complexContent&gt;
     *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                             &lt;sequence&gt;
     *                               &lt;element name="Group" type="{event-logging:3}Group" maxOccurs="unbounded" minOccurs="0"/&gt;
     *                             &lt;/sequence&gt;
     *                           &lt;/restriction&gt;
     *                         &lt;/complexContent&gt;
     *                       &lt;/complexType&gt;
     *                     &lt;/element&gt;
     *                     &lt;element name="Outcome" type="{event-logging:3}Outcome" minOccurs="0"/&gt;
     *                     &lt;element name="Data" type="{event-logging:3}Data" maxOccurs="unbounded" minOccurs="0"/&gt;
     *                   &lt;/sequence&gt;
     *                 &lt;/extension&gt;
     *               &lt;/complexContent&gt;
     *             &lt;/complexType&gt;
     *           &lt;/element&gt;
     *           &lt;element name="Search" type="{event-logging:3}Search"/&gt;
     *           &lt;element name="Copy" type="{event-logging:3}CopyMove"/&gt;
     *           &lt;element name="Move" type="{event-logging:3}CopyMove"/&gt;
     *           &lt;element name="Create" type="{event-logging:3}ObjectOutcome"/&gt;
     *           &lt;element name="View" type="{event-logging:3}ObjectOutcome"/&gt;
     *           &lt;element name="Import" type="{event-logging:3}Import"/&gt;
     *           &lt;element name="Export" type="{event-logging:3}Export"/&gt;
     *           &lt;element name="Update"&gt;
     *             &lt;complexType&gt;
     *               &lt;complexContent&gt;
     *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                   &lt;sequence&gt;
     *                     &lt;element name="Before" type="{event-logging:3}MultiObject" minOccurs="0"/&gt;
     *                     &lt;element name="After" type="{event-logging:3}MultiObject"/&gt;
     *                     &lt;element name="Outcome" type="{event-logging:3}Outcome" minOccurs="0"/&gt;
     *                     &lt;element name="Data" type="{event-logging:3}Data" maxOccurs="unbounded" minOccurs="0"/&gt;
     *                   &lt;/sequence&gt;
     *                 &lt;/restriction&gt;
     *               &lt;/complexContent&gt;
     *             &lt;/complexType&gt;
     *           &lt;/element&gt;
     *           &lt;element name="Delete" type="{event-logging:3}ObjectOutcome"/&gt;
     *           &lt;element name="Process"&gt;
     *             &lt;complexType&gt;
     *               &lt;complexContent&gt;
     *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                   &lt;sequence&gt;
     *                     &lt;element name="Action" type="{event-logging:3}ProcessAction"/&gt;
     *                     &lt;element name="Type" type="{event-logging:3}ProcessType"/&gt;
     *                     &lt;element name="Command" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                     &lt;element name="Arguments" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                     &lt;element name="ProcessId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                     &lt;element name="ThreadId" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" minOccurs="0"/&gt;
     *                     &lt;element name="Rule" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                     &lt;element name="Input" type="{event-logging:3}MultiObject" minOccurs="0"/&gt;
     *                     &lt;element name="Output" type="{event-logging:3}MultiObject" minOccurs="0"/&gt;
     *                     &lt;element name="Outcome" type="{event-logging:3}Outcome" minOccurs="0"/&gt;
     *                     &lt;element name="Data" type="{event-logging:3}Data" maxOccurs="unbounded" minOccurs="0"/&gt;
     *                   &lt;/sequence&gt;
     *                 &lt;/restriction&gt;
     *               &lt;/complexContent&gt;
     *             &lt;/complexType&gt;
     *           &lt;/element&gt;
     *           &lt;element name="Print"&gt;
     *             &lt;complexType&gt;
     *               &lt;complexContent&gt;
     *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                   &lt;sequence&gt;
     *                     &lt;element name="Action" type="{event-logging:3}PrintAction"/&gt;
     *                     &lt;element name="PrintJob"&gt;
     *                       &lt;complexType&gt;
     *                         &lt;complexContent&gt;
     *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                             &lt;sequence&gt;
     *                               &lt;element name="Document" type="{event-logging:3}Document"/&gt;
     *                               &lt;element name="Pages" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/&gt;
     *                               &lt;element name="Size" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/&gt;
     *                               &lt;element name="Submitted" type="{event-logging:3}DateTime" minOccurs="0"/&gt;
     *                             &lt;/sequence&gt;
     *                           &lt;/restriction&gt;
     *                         &lt;/complexContent&gt;
     *                       &lt;/complexType&gt;
     *                     &lt;/element&gt;
     *                     &lt;element name="PrintSettings" minOccurs="0"&gt;
     *                       &lt;complexType&gt;
     *                         &lt;complexContent&gt;
     *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                             &lt;sequence&gt;
     *                               &lt;element name="PaperSize" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                               &lt;element name="Orientation" type="{event-logging:3}PrintSettingsOrientation" minOccurs="0"/&gt;
     *                               &lt;element name="Colour" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
     *                               &lt;element name="DeviceFonts" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
     *                               &lt;element name="Data" type="{event-logging:3}Data" maxOccurs="unbounded" minOccurs="0"/&gt;
     *                             &lt;/sequence&gt;
     *                           &lt;/restriction&gt;
     *                         &lt;/complexContent&gt;
     *                       &lt;/complexType&gt;
     *                     &lt;/element&gt;
     *                     &lt;element name="Printer" type="{event-logging:3}Device" minOccurs="0"/&gt;
     *                     &lt;element name="Outcome" type="{event-logging:3}Outcome" minOccurs="0"/&gt;
     *                     &lt;element name="Data" type="{event-logging:3}Data" maxOccurs="unbounded" minOccurs="0"/&gt;
     *                   &lt;/sequence&gt;
     *                 &lt;/restriction&gt;
     *               &lt;/complexContent&gt;
     *             &lt;/complexType&gt;
     *           &lt;/element&gt;
     *           &lt;element name="Install" type="{event-logging:3}Install"/&gt;
     *           &lt;element name="Uninstall" type="{event-logging:3}Install"/&gt;
     *           &lt;element name="Network"&gt;
     *             &lt;complexType&gt;
     *               &lt;complexContent&gt;
     *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                   &lt;choice&gt;
     *                     &lt;element name="Bind" type="{event-logging:3}Network"/&gt;
     *                     &lt;element name="Connect" type="{event-logging:3}Network"/&gt;
     *                     &lt;element name="Open" type="{event-logging:3}Network"/&gt;
     *                     &lt;element name="Close" type="{event-logging:3}Network"/&gt;
     *                     &lt;element name="Send" type="{event-logging:3}Network"/&gt;
     *                     &lt;element name="Receive" type="{event-logging:3}Network"/&gt;
     *                     &lt;element name="Listen" type="{event-logging:3}Network"/&gt;
     *                     &lt;element name="Permit" type="{event-logging:3}Network"/&gt;
     *                     &lt;element name="Deny" type="{event-logging:3}Network"/&gt;
     *                   &lt;/choice&gt;
     *                 &lt;/restriction&gt;
     *               &lt;/complexContent&gt;
     *             &lt;/complexType&gt;
     *           &lt;/element&gt;
     *           &lt;element name="AntiMalware"&gt;
     *             &lt;complexType&gt;
     *               &lt;complexContent&gt;
     *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                   &lt;sequence&gt;
     *                     &lt;choice&gt;
     *                       &lt;element name="StartScan" type="{event-logging:3}AntiMalware"/&gt;
     *                       &lt;element name="StopScan" type="{event-logging:3}AntiMalware"/&gt;
     *                       &lt;element name="Admin" type="{event-logging:3}AntiMalware"/&gt;
     *                       &lt;element name="Quarantine" type="{event-logging:3}AntiMalwareThreat"/&gt;
     *                       &lt;element name="Report" type="{event-logging:3}AntiMalware"/&gt;
     *                       &lt;element name="Email" type="{event-logging:3}AntiMalware"/&gt;
     *                       &lt;element name="Delete" type="{event-logging:3}AntiMalwareThreat"/&gt;
     *                       &lt;element name="ScanEngineUpdated" type="{event-logging:3}AntiMalware"/&gt;
     *                       &lt;element name="SignaturesUpdated" type="{event-logging:3}AntiMalware"/&gt;
     *                     &lt;/choice&gt;
     *                     &lt;element name="Outcome" type="{event-logging:3}Outcome" minOccurs="0"/&gt;
     *                     &lt;element name="Data" type="{event-logging:3}Data" maxOccurs="unbounded" minOccurs="0"/&gt;
     *                   &lt;/sequence&gt;
     *                 &lt;/restriction&gt;
     *               &lt;/complexContent&gt;
     *             &lt;/complexType&gt;
     *           &lt;/element&gt;
     *           &lt;element name="Alert"&gt;
     *             &lt;complexType&gt;
     *               &lt;complexContent&gt;
     *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                   &lt;sequence&gt;
     *                     &lt;element name="Type" type="{event-logging:3}AlertType"/&gt;
     *                     &lt;element name="Severity" type="{event-logging:3}AlertSeverity" minOccurs="0"/&gt;
     *                     &lt;element name="Priority" type="{event-logging:3}AlertPriority" minOccurs="0"/&gt;
     *                     &lt;element name="Subject" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                     &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                     &lt;element name="IDS" minOccurs="0"&gt;
     *                       &lt;complexType&gt;
     *                         &lt;complexContent&gt;
     *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                             &lt;sequence&gt;
     *                               &lt;element name="Rule" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                               &lt;element name="Source" type="{event-logging:3}NetworkSrcDst" minOccurs="0"/&gt;
     *                               &lt;element name="Destination" type="{event-logging:3}NetworkSrcDst" minOccurs="0"/&gt;
     *                               &lt;element name="Payload" type="{event-logging:3}MultiObject" minOccurs="0"/&gt;
     *                             &lt;/sequence&gt;
     *                           &lt;/restriction&gt;
     *                         &lt;/complexContent&gt;
     *                       &lt;/complexType&gt;
     *                     &lt;/element&gt;
     *                     &lt;element name="Malware" type="{event-logging:3}AntiMalwareThreat" minOccurs="0"/&gt;
     *                     &lt;element name="Network" type="{event-logging:3}BaseNetwork" minOccurs="0"/&gt;
     *                     &lt;element name="Change" minOccurs="0"&gt;
     *                       &lt;complexType&gt;
     *                         &lt;complexContent&gt;
     *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                             &lt;sequence&gt;
     *                               &lt;element name="Action" type="{event-logging:3}ChangeAction"/&gt;
     *                               &lt;element name="Before" type="{event-logging:3}MultiObject" minOccurs="0"/&gt;
     *                               &lt;element name="After" type="{event-logging:3}MultiObject" minOccurs="0"/&gt;
     *                               &lt;element name="Rule" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                             &lt;/sequence&gt;
     *                           &lt;/restriction&gt;
     *                         &lt;/complexContent&gt;
     *                       &lt;/complexType&gt;
     *                     &lt;/element&gt;
     *                     &lt;element name="Data" type="{event-logging:3}Data" maxOccurs="unbounded" minOccurs="0"/&gt;
     *                   &lt;/sequence&gt;
     *                 &lt;/restriction&gt;
     *               &lt;/complexContent&gt;
     *             &lt;/complexType&gt;
     *           &lt;/element&gt;
     *           &lt;element name="Send" type="{event-logging:3}SendReceive"/&gt;
     *           &lt;element name="Receive" type="{event-logging:3}SendReceive"/&gt;
     *           &lt;element name="Unknown"&gt;
     *             &lt;complexType&gt;
     *               &lt;complexContent&gt;
     *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                   &lt;sequence&gt;
     *                     &lt;element name="Data" type="{event-logging:3}Data" maxOccurs="unbounded" minOccurs="0"/&gt;
     *                   &lt;/sequence&gt;
     *                 &lt;/restriction&gt;
     *               &lt;/complexContent&gt;
     *             &lt;/complexType&gt;
     *           &lt;/element&gt;
     *         &lt;/choice&gt;
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
        "typeId",
        "description",
        "classification",
        "purpose",
        "authenticate",
        "authorise",
        "search",
        "copy",
        "move",
        "create",
        "view",
        "_import",
        "export",
        "update",
        "delete",
        "process",
        "print",
        "install",
        "uninstall",
        "network",
        "antiMalware",
        "alert",
        "send",
        "receive",
        "unknown"
    })
    public static class EventDetail {

        @XmlElement(name = "TypeId", required = true)
        protected String typeId;
        @XmlElement(name = "Description")
        protected String description;
        @XmlElement(name = "Classification")
        protected Classification classification;
        @XmlElement(name = "Purpose")
        protected Purpose purpose;
        @XmlElement(name = "Authenticate")
        protected Event.EventDetail.Authenticate authenticate;
        @XmlElement(name = "Authorise")
        protected Event.EventDetail.Authorise authorise;
        @XmlElement(name = "Search")
        protected Search search;
        @XmlElement(name = "Copy")
        protected CopyMove copy;
        @XmlElement(name = "Move")
        protected CopyMove move;
        @XmlElement(name = "Create")
        protected ObjectOutcome create;
        @XmlElement(name = "View")
        protected ObjectOutcome view;
        @XmlElement(name = "Import")
        protected Import _import;
        @XmlElement(name = "Export")
        protected Export export;
        @XmlElement(name = "Update")
        protected Event.EventDetail.Update update;
        @XmlElement(name = "Delete")
        protected ObjectOutcome delete;
        @XmlElement(name = "Process")
        protected Event.EventDetail.Process process;
        @XmlElement(name = "Print")
        protected Event.EventDetail.Print print;
        @XmlElement(name = "Install")
        protected Install install;
        @XmlElement(name = "Uninstall")
        protected Install uninstall;
        @XmlElement(name = "Network")
        protected Event.EventDetail.Network network;
        @XmlElement(name = "AntiMalware")
        protected Event.EventDetail.AntiMalware antiMalware;
        @XmlElement(name = "Alert")
        protected Event.EventDetail.Alert alert;
        @XmlElement(name = "Send")
        protected SendReceive send;
        @XmlElement(name = "Receive")
        protected SendReceive receive;
        @XmlElement(name = "Unknown")
        protected Event.EventDetail.Unknown unknown;

        /**
         * Gets the value of the typeId property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTypeId() {
            return typeId;
        }

        /**
         * Sets the value of the typeId property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTypeId(String value) {
            this.typeId = value;
        }

        /**
         * Gets the value of the description property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescription() {
            return description;
        }

        /**
         * Sets the value of the description property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescription(String value) {
            this.description = value;
        }

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
         * Gets the value of the purpose property.
         * 
         * @return
         *     possible object is
         *     {@link Purpose }
         *     
         */
        public Purpose getPurpose() {
            return purpose;
        }

        /**
         * Sets the value of the purpose property.
         * 
         * @param value
         *     allowed object is
         *     {@link Purpose }
         *     
         */
        public void setPurpose(Purpose value) {
            this.purpose = value;
        }

        /**
         * Gets the value of the authenticate property.
         * 
         * @return
         *     possible object is
         *     {@link Event.EventDetail.Authenticate }
         *     
         */
        public Event.EventDetail.Authenticate getAuthenticate() {
            return authenticate;
        }

        /**
         * Sets the value of the authenticate property.
         * 
         * @param value
         *     allowed object is
         *     {@link Event.EventDetail.Authenticate }
         *     
         */
        public void setAuthenticate(Event.EventDetail.Authenticate value) {
            this.authenticate = value;
        }

        /**
         * Gets the value of the authorise property.
         * 
         * @return
         *     possible object is
         *     {@link Event.EventDetail.Authorise }
         *     
         */
        public Event.EventDetail.Authorise getAuthorise() {
            return authorise;
        }

        /**
         * Sets the value of the authorise property.
         * 
         * @param value
         *     allowed object is
         *     {@link Event.EventDetail.Authorise }
         *     
         */
        public void setAuthorise(Event.EventDetail.Authorise value) {
            this.authorise = value;
        }

        /**
         * Gets the value of the search property.
         * 
         * @return
         *     possible object is
         *     {@link Search }
         *     
         */
        public Search getSearch() {
            return search;
        }

        /**
         * Sets the value of the search property.
         * 
         * @param value
         *     allowed object is
         *     {@link Search }
         *     
         */
        public void setSearch(Search value) {
            this.search = value;
        }

        /**
         * Gets the value of the copy property.
         * 
         * @return
         *     possible object is
         *     {@link CopyMove }
         *     
         */
        public CopyMove getCopy() {
            return copy;
        }

        /**
         * Sets the value of the copy property.
         * 
         * @param value
         *     allowed object is
         *     {@link CopyMove }
         *     
         */
        public void setCopy(CopyMove value) {
            this.copy = value;
        }

        /**
         * Gets the value of the move property.
         * 
         * @return
         *     possible object is
         *     {@link CopyMove }
         *     
         */
        public CopyMove getMove() {
            return move;
        }

        /**
         * Sets the value of the move property.
         * 
         * @param value
         *     allowed object is
         *     {@link CopyMove }
         *     
         */
        public void setMove(CopyMove value) {
            this.move = value;
        }

        /**
         * Gets the value of the create property.
         * 
         * @return
         *     possible object is
         *     {@link ObjectOutcome }
         *     
         */
        public ObjectOutcome getCreate() {
            return create;
        }

        /**
         * Sets the value of the create property.
         * 
         * @param value
         *     allowed object is
         *     {@link ObjectOutcome }
         *     
         */
        public void setCreate(ObjectOutcome value) {
            this.create = value;
        }

        /**
         * Gets the value of the view property.
         * 
         * @return
         *     possible object is
         *     {@link ObjectOutcome }
         *     
         */
        public ObjectOutcome getView() {
            return view;
        }

        /**
         * Sets the value of the view property.
         * 
         * @param value
         *     allowed object is
         *     {@link ObjectOutcome }
         *     
         */
        public void setView(ObjectOutcome value) {
            this.view = value;
        }

        /**
         * Gets the value of the import property.
         * 
         * @return
         *     possible object is
         *     {@link Import }
         *     
         */
        public Import getImport() {
            return _import;
        }

        /**
         * Sets the value of the import property.
         * 
         * @param value
         *     allowed object is
         *     {@link Import }
         *     
         */
        public void setImport(Import value) {
            this._import = value;
        }

        /**
         * Gets the value of the export property.
         * 
         * @return
         *     possible object is
         *     {@link Export }
         *     
         */
        public Export getExport() {
            return export;
        }

        /**
         * Sets the value of the export property.
         * 
         * @param value
         *     allowed object is
         *     {@link Export }
         *     
         */
        public void setExport(Export value) {
            this.export = value;
        }

        /**
         * Gets the value of the update property.
         * 
         * @return
         *     possible object is
         *     {@link Event.EventDetail.Update }
         *     
         */
        public Event.EventDetail.Update getUpdate() {
            return update;
        }

        /**
         * Sets the value of the update property.
         * 
         * @param value
         *     allowed object is
         *     {@link Event.EventDetail.Update }
         *     
         */
        public void setUpdate(Event.EventDetail.Update value) {
            this.update = value;
        }

        /**
         * Gets the value of the delete property.
         * 
         * @return
         *     possible object is
         *     {@link ObjectOutcome }
         *     
         */
        public ObjectOutcome getDelete() {
            return delete;
        }

        /**
         * Sets the value of the delete property.
         * 
         * @param value
         *     allowed object is
         *     {@link ObjectOutcome }
         *     
         */
        public void setDelete(ObjectOutcome value) {
            this.delete = value;
        }

        /**
         * Gets the value of the process property.
         * 
         * @return
         *     possible object is
         *     {@link Event.EventDetail.Process }
         *     
         */
        public Event.EventDetail.Process getProcess() {
            return process;
        }

        /**
         * Sets the value of the process property.
         * 
         * @param value
         *     allowed object is
         *     {@link Event.EventDetail.Process }
         *     
         */
        public void setProcess(Event.EventDetail.Process value) {
            this.process = value;
        }

        /**
         * Gets the value of the print property.
         * 
         * @return
         *     possible object is
         *     {@link Event.EventDetail.Print }
         *     
         */
        public Event.EventDetail.Print getPrint() {
            return print;
        }

        /**
         * Sets the value of the print property.
         * 
         * @param value
         *     allowed object is
         *     {@link Event.EventDetail.Print }
         *     
         */
        public void setPrint(Event.EventDetail.Print value) {
            this.print = value;
        }

        /**
         * Gets the value of the install property.
         * 
         * @return
         *     possible object is
         *     {@link Install }
         *     
         */
        public Install getInstall() {
            return install;
        }

        /**
         * Sets the value of the install property.
         * 
         * @param value
         *     allowed object is
         *     {@link Install }
         *     
         */
        public void setInstall(Install value) {
            this.install = value;
        }

        /**
         * Gets the value of the uninstall property.
         * 
         * @return
         *     possible object is
         *     {@link Install }
         *     
         */
        public Install getUninstall() {
            return uninstall;
        }

        /**
         * Sets the value of the uninstall property.
         * 
         * @param value
         *     allowed object is
         *     {@link Install }
         *     
         */
        public void setUninstall(Install value) {
            this.uninstall = value;
        }

        /**
         * Gets the value of the network property.
         * 
         * @return
         *     possible object is
         *     {@link Event.EventDetail.Network }
         *     
         */
        public Event.EventDetail.Network getNetwork() {
            return network;
        }

        /**
         * Sets the value of the network property.
         * 
         * @param value
         *     allowed object is
         *     {@link Event.EventDetail.Network }
         *     
         */
        public void setNetwork(Event.EventDetail.Network value) {
            this.network = value;
        }

        /**
         * Gets the value of the antiMalware property.
         * 
         * @return
         *     possible object is
         *     {@link Event.EventDetail.AntiMalware }
         *     
         */
        public Event.EventDetail.AntiMalware getAntiMalware() {
            return antiMalware;
        }

        /**
         * Sets the value of the antiMalware property.
         * 
         * @param value
         *     allowed object is
         *     {@link Event.EventDetail.AntiMalware }
         *     
         */
        public void setAntiMalware(Event.EventDetail.AntiMalware value) {
            this.antiMalware = value;
        }

        /**
         * Gets the value of the alert property.
         * 
         * @return
         *     possible object is
         *     {@link Event.EventDetail.Alert }
         *     
         */
        public Event.EventDetail.Alert getAlert() {
            return alert;
        }

        /**
         * Sets the value of the alert property.
         * 
         * @param value
         *     allowed object is
         *     {@link Event.EventDetail.Alert }
         *     
         */
        public void setAlert(Event.EventDetail.Alert value) {
            this.alert = value;
        }

        /**
         * Gets the value of the send property.
         * 
         * @return
         *     possible object is
         *     {@link SendReceive }
         *     
         */
        public SendReceive getSend() {
            return send;
        }

        /**
         * Sets the value of the send property.
         * 
         * @param value
         *     allowed object is
         *     {@link SendReceive }
         *     
         */
        public void setSend(SendReceive value) {
            this.send = value;
        }

        /**
         * Gets the value of the receive property.
         * 
         * @return
         *     possible object is
         *     {@link SendReceive }
         *     
         */
        public SendReceive getReceive() {
            return receive;
        }

        /**
         * Sets the value of the receive property.
         * 
         * @param value
         *     allowed object is
         *     {@link SendReceive }
         *     
         */
        public void setReceive(SendReceive value) {
            this.receive = value;
        }

        /**
         * Gets the value of the unknown property.
         * 
         * @return
         *     possible object is
         *     {@link Event.EventDetail.Unknown }
         *     
         */
        public Event.EventDetail.Unknown getUnknown() {
            return unknown;
        }

        /**
         * Sets the value of the unknown property.
         * 
         * @param value
         *     allowed object is
         *     {@link Event.EventDetail.Unknown }
         *     
         */
        public void setUnknown(Event.EventDetail.Unknown value) {
            this.unknown = value;
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
         *         &lt;element name="Type" type="{event-logging:3}AlertType"/&gt;
         *         &lt;element name="Severity" type="{event-logging:3}AlertSeverity" minOccurs="0"/&gt;
         *         &lt;element name="Priority" type="{event-logging:3}AlertPriority" minOccurs="0"/&gt;
         *         &lt;element name="Subject" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="IDS" minOccurs="0"&gt;
         *           &lt;complexType&gt;
         *             &lt;complexContent&gt;
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                 &lt;sequence&gt;
         *                   &lt;element name="Rule" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *                   &lt;element name="Source" type="{event-logging:3}NetworkSrcDst" minOccurs="0"/&gt;
         *                   &lt;element name="Destination" type="{event-logging:3}NetworkSrcDst" minOccurs="0"/&gt;
         *                   &lt;element name="Payload" type="{event-logging:3}MultiObject" minOccurs="0"/&gt;
         *                 &lt;/sequence&gt;
         *               &lt;/restriction&gt;
         *             &lt;/complexContent&gt;
         *           &lt;/complexType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="Malware" type="{event-logging:3}AntiMalwareThreat" minOccurs="0"/&gt;
         *         &lt;element name="Network" type="{event-logging:3}BaseNetwork" minOccurs="0"/&gt;
         *         &lt;element name="Change" minOccurs="0"&gt;
         *           &lt;complexType&gt;
         *             &lt;complexContent&gt;
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                 &lt;sequence&gt;
         *                   &lt;element name="Action" type="{event-logging:3}ChangeAction"/&gt;
         *                   &lt;element name="Before" type="{event-logging:3}MultiObject" minOccurs="0"/&gt;
         *                   &lt;element name="After" type="{event-logging:3}MultiObject" minOccurs="0"/&gt;
         *                   &lt;element name="Rule" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
        @XmlType(name = "", propOrder = {
            "type",
            "severity",
            "priority",
            "subject",
            "description",
            "ids",
            "malware",
            "network",
            "change",
            "data"
        })
        public static class Alert {

            @XmlElement(name = "Type", required = true)
            @XmlSchemaType(name = "string")
            protected AlertType type;
            @XmlElement(name = "Severity")
            @XmlSchemaType(name = "string")
            protected AlertSeverity severity;
            @XmlElement(name = "Priority")
            @XmlSchemaType(name = "string")
            protected AlertPriority priority;
            @XmlElement(name = "Subject")
            protected String subject;
            @XmlElement(name = "Description")
            protected String description;
            @XmlElement(name = "IDS")
            protected Event.EventDetail.Alert.IDS ids;
            @XmlElement(name = "Malware")
            protected AntiMalwareThreat malware;
            @XmlElement(name = "Network")
            protected BaseNetwork network;
            @XmlElement(name = "Change")
            protected Event.EventDetail.Alert.Change change;
            @XmlElement(name = "Data")
            protected List<Data> data;

            /**
             * Gets the value of the type property.
             * 
             * @return
             *     possible object is
             *     {@link AlertType }
             *     
             */
            public AlertType getType() {
                return type;
            }

            /**
             * Sets the value of the type property.
             * 
             * @param value
             *     allowed object is
             *     {@link AlertType }
             *     
             */
            public void setType(AlertType value) {
                this.type = value;
            }

            /**
             * Gets the value of the severity property.
             * 
             * @return
             *     possible object is
             *     {@link AlertSeverity }
             *     
             */
            public AlertSeverity getSeverity() {
                return severity;
            }

            /**
             * Sets the value of the severity property.
             * 
             * @param value
             *     allowed object is
             *     {@link AlertSeverity }
             *     
             */
            public void setSeverity(AlertSeverity value) {
                this.severity = value;
            }

            /**
             * Gets the value of the priority property.
             * 
             * @return
             *     possible object is
             *     {@link AlertPriority }
             *     
             */
            public AlertPriority getPriority() {
                return priority;
            }

            /**
             * Sets the value of the priority property.
             * 
             * @param value
             *     allowed object is
             *     {@link AlertPriority }
             *     
             */
            public void setPriority(AlertPriority value) {
                this.priority = value;
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
             * Gets the value of the description property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getDescription() {
                return description;
            }

            /**
             * Sets the value of the description property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setDescription(String value) {
                this.description = value;
            }

            /**
             * Gets the value of the ids property.
             * 
             * @return
             *     possible object is
             *     {@link Event.EventDetail.Alert.IDS }
             *     
             */
            public Event.EventDetail.Alert.IDS getIDS() {
                return ids;
            }

            /**
             * Sets the value of the ids property.
             * 
             * @param value
             *     allowed object is
             *     {@link Event.EventDetail.Alert.IDS }
             *     
             */
            public void setIDS(Event.EventDetail.Alert.IDS value) {
                this.ids = value;
            }

            /**
             * Gets the value of the malware property.
             * 
             * @return
             *     possible object is
             *     {@link AntiMalwareThreat }
             *     
             */
            public AntiMalwareThreat getMalware() {
                return malware;
            }

            /**
             * Sets the value of the malware property.
             * 
             * @param value
             *     allowed object is
             *     {@link AntiMalwareThreat }
             *     
             */
            public void setMalware(AntiMalwareThreat value) {
                this.malware = value;
            }

            /**
             * Gets the value of the network property.
             * 
             * @return
             *     possible object is
             *     {@link BaseNetwork }
             *     
             */
            public BaseNetwork getNetwork() {
                return network;
            }

            /**
             * Sets the value of the network property.
             * 
             * @param value
             *     allowed object is
             *     {@link BaseNetwork }
             *     
             */
            public void setNetwork(BaseNetwork value) {
                this.network = value;
            }

            /**
             * Gets the value of the change property.
             * 
             * @return
             *     possible object is
             *     {@link Event.EventDetail.Alert.Change }
             *     
             */
            public Event.EventDetail.Alert.Change getChange() {
                return change;
            }

            /**
             * Sets the value of the change property.
             * 
             * @param value
             *     allowed object is
             *     {@link Event.EventDetail.Alert.Change }
             *     
             */
            public void setChange(Event.EventDetail.Alert.Change value) {
                this.change = value;
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
             *         &lt;element name="Action" type="{event-logging:3}ChangeAction"/&gt;
             *         &lt;element name="Before" type="{event-logging:3}MultiObject" minOccurs="0"/&gt;
             *         &lt;element name="After" type="{event-logging:3}MultiObject" minOccurs="0"/&gt;
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
            @XmlType(name = "", propOrder = {
                "action",
                "before",
                "after",
                "rule"
            })
            public static class Change {

                @XmlElement(name = "Action", required = true)
                @XmlSchemaType(name = "string")
                protected ChangeAction action;
                @XmlElement(name = "Before")
                protected MultiObject before;
                @XmlElement(name = "After")
                protected MultiObject after;
                @XmlElement(name = "Rule")
                protected String rule;

                /**
                 * Gets the value of the action property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link ChangeAction }
                 *     
                 */
                public ChangeAction getAction() {
                    return action;
                }

                /**
                 * Sets the value of the action property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link ChangeAction }
                 *     
                 */
                public void setAction(ChangeAction value) {
                    this.action = value;
                }

                /**
                 * Gets the value of the before property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link MultiObject }
                 *     
                 */
                public MultiObject getBefore() {
                    return before;
                }

                /**
                 * Sets the value of the before property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link MultiObject }
                 *     
                 */
                public void setBefore(MultiObject value) {
                    this.before = value;
                }

                /**
                 * Gets the value of the after property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link MultiObject }
                 *     
                 */
                public MultiObject getAfter() {
                    return after;
                }

                /**
                 * Sets the value of the after property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link MultiObject }
                 *     
                 */
                public void setAfter(MultiObject value) {
                    this.after = value;
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
             *         &lt;element name="Rule" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
             *         &lt;element name="Source" type="{event-logging:3}NetworkSrcDst" minOccurs="0"/&gt;
             *         &lt;element name="Destination" type="{event-logging:3}NetworkSrcDst" minOccurs="0"/&gt;
             *         &lt;element name="Payload" type="{event-logging:3}MultiObject" minOccurs="0"/&gt;
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
                "rule",
                "source",
                "destination",
                "payload"
            })
            public static class IDS {

                @XmlElement(name = "Rule")
                protected String rule;
                @XmlElement(name = "Source")
                protected NetworkSrcDst source;
                @XmlElement(name = "Destination")
                protected NetworkSrcDst destination;
                @XmlElement(name = "Payload")
                protected MultiObject payload;

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
         *         &lt;choice&gt;
         *           &lt;element name="StartScan" type="{event-logging:3}AntiMalware"/&gt;
         *           &lt;element name="StopScan" type="{event-logging:3}AntiMalware"/&gt;
         *           &lt;element name="Admin" type="{event-logging:3}AntiMalware"/&gt;
         *           &lt;element name="Quarantine" type="{event-logging:3}AntiMalwareThreat"/&gt;
         *           &lt;element name="Report" type="{event-logging:3}AntiMalware"/&gt;
         *           &lt;element name="Email" type="{event-logging:3}AntiMalware"/&gt;
         *           &lt;element name="Delete" type="{event-logging:3}AntiMalwareThreat"/&gt;
         *           &lt;element name="ScanEngineUpdated" type="{event-logging:3}AntiMalware"/&gt;
         *           &lt;element name="SignaturesUpdated" type="{event-logging:3}AntiMalware"/&gt;
         *         &lt;/choice&gt;
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
        @XmlType(name = "", propOrder = {
            "startScan",
            "stopScan",
            "admin",
            "quarantine",
            "report",
            "email",
            "delete",
            "scanEngineUpdated",
            "signaturesUpdated",
            "outcome",
            "data"
        })
        public static class AntiMalware {

            @XmlElement(name = "StartScan")
            protected event.logging.AntiMalware startScan;
            @XmlElement(name = "StopScan")
            protected event.logging.AntiMalware stopScan;
            @XmlElement(name = "Admin")
            protected event.logging.AntiMalware admin;
            @XmlElement(name = "Quarantine")
            protected AntiMalwareThreat quarantine;
            @XmlElement(name = "Report")
            protected event.logging.AntiMalware report;
            @XmlElement(name = "Email")
            protected event.logging.AntiMalware email;
            @XmlElement(name = "Delete")
            protected AntiMalwareThreat delete;
            @XmlElement(name = "ScanEngineUpdated")
            protected event.logging.AntiMalware scanEngineUpdated;
            @XmlElement(name = "SignaturesUpdated")
            protected event.logging.AntiMalware signaturesUpdated;
            @XmlElement(name = "Outcome")
            protected Outcome outcome;
            @XmlElement(name = "Data")
            protected List<Data> data;

            /**
             * Gets the value of the startScan property.
             * 
             * @return
             *     possible object is
             *     {@link event.logging.AntiMalware }
             *     
             */
            public event.logging.AntiMalware getStartScan() {
                return startScan;
            }

            /**
             * Sets the value of the startScan property.
             * 
             * @param value
             *     allowed object is
             *     {@link event.logging.AntiMalware }
             *     
             */
            public void setStartScan(event.logging.AntiMalware value) {
                this.startScan = value;
            }

            /**
             * Gets the value of the stopScan property.
             * 
             * @return
             *     possible object is
             *     {@link event.logging.AntiMalware }
             *     
             */
            public event.logging.AntiMalware getStopScan() {
                return stopScan;
            }

            /**
             * Sets the value of the stopScan property.
             * 
             * @param value
             *     allowed object is
             *     {@link event.logging.AntiMalware }
             *     
             */
            public void setStopScan(event.logging.AntiMalware value) {
                this.stopScan = value;
            }

            /**
             * Gets the value of the admin property.
             * 
             * @return
             *     possible object is
             *     {@link event.logging.AntiMalware }
             *     
             */
            public event.logging.AntiMalware getAdmin() {
                return admin;
            }

            /**
             * Sets the value of the admin property.
             * 
             * @param value
             *     allowed object is
             *     {@link event.logging.AntiMalware }
             *     
             */
            public void setAdmin(event.logging.AntiMalware value) {
                this.admin = value;
            }

            /**
             * Gets the value of the quarantine property.
             * 
             * @return
             *     possible object is
             *     {@link AntiMalwareThreat }
             *     
             */
            public AntiMalwareThreat getQuarantine() {
                return quarantine;
            }

            /**
             * Sets the value of the quarantine property.
             * 
             * @param value
             *     allowed object is
             *     {@link AntiMalwareThreat }
             *     
             */
            public void setQuarantine(AntiMalwareThreat value) {
                this.quarantine = value;
            }

            /**
             * Gets the value of the report property.
             * 
             * @return
             *     possible object is
             *     {@link event.logging.AntiMalware }
             *     
             */
            public event.logging.AntiMalware getReport() {
                return report;
            }

            /**
             * Sets the value of the report property.
             * 
             * @param value
             *     allowed object is
             *     {@link event.logging.AntiMalware }
             *     
             */
            public void setReport(event.logging.AntiMalware value) {
                this.report = value;
            }

            /**
             * Gets the value of the email property.
             * 
             * @return
             *     possible object is
             *     {@link event.logging.AntiMalware }
             *     
             */
            public event.logging.AntiMalware getEmail() {
                return email;
            }

            /**
             * Sets the value of the email property.
             * 
             * @param value
             *     allowed object is
             *     {@link event.logging.AntiMalware }
             *     
             */
            public void setEmail(event.logging.AntiMalware value) {
                this.email = value;
            }

            /**
             * Gets the value of the delete property.
             * 
             * @return
             *     possible object is
             *     {@link AntiMalwareThreat }
             *     
             */
            public AntiMalwareThreat getDelete() {
                return delete;
            }

            /**
             * Sets the value of the delete property.
             * 
             * @param value
             *     allowed object is
             *     {@link AntiMalwareThreat }
             *     
             */
            public void setDelete(AntiMalwareThreat value) {
                this.delete = value;
            }

            /**
             * Gets the value of the scanEngineUpdated property.
             * 
             * @return
             *     possible object is
             *     {@link event.logging.AntiMalware }
             *     
             */
            public event.logging.AntiMalware getScanEngineUpdated() {
                return scanEngineUpdated;
            }

            /**
             * Sets the value of the scanEngineUpdated property.
             * 
             * @param value
             *     allowed object is
             *     {@link event.logging.AntiMalware }
             *     
             */
            public void setScanEngineUpdated(event.logging.AntiMalware value) {
                this.scanEngineUpdated = value;
            }

            /**
             * Gets the value of the signaturesUpdated property.
             * 
             * @return
             *     possible object is
             *     {@link event.logging.AntiMalware }
             *     
             */
            public event.logging.AntiMalware getSignaturesUpdated() {
                return signaturesUpdated;
            }

            /**
             * Sets the value of the signaturesUpdated property.
             * 
             * @param value
             *     allowed object is
             *     {@link event.logging.AntiMalware }
             *     
             */
            public void setSignaturesUpdated(event.logging.AntiMalware value) {
                this.signaturesUpdated = value;
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
         *         &lt;element name="Action" type="{event-logging:3}AuthenticateAction"/&gt;
         *         &lt;element name="LogonType" type="{event-logging:3}AuthenticateLogonType" minOccurs="0"/&gt;
         *         &lt;choice&gt;
         *           &lt;element name="User" type="{event-logging:3}User"/&gt;
         *           &lt;element name="Device" type="{event-logging:3}Device"/&gt;
         *           &lt;element name="Group" type="{event-logging:3}Group"/&gt;
         *         &lt;/choice&gt;
         *         &lt;element name="Outcome" type="{event-logging:3}AuthenticateOutcome" minOccurs="0"/&gt;
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
        @XmlType(name = "", propOrder = {
            "action",
            "logonType",
            "user",
            "device",
            "group",
            "outcome",
            "data"
        })
        public static class Authenticate {

            @XmlElement(name = "Action", required = true)
            @XmlSchemaType(name = "string")
            protected AuthenticateAction action;
            @XmlElement(name = "LogonType")
            @XmlSchemaType(name = "string")
            protected AuthenticateLogonType logonType;
            @XmlElement(name = "User")
            protected User user;
            @XmlElement(name = "Device")
            protected Device device;
            @XmlElement(name = "Group")
            protected Group group;
            @XmlElement(name = "Outcome")
            protected AuthenticateOutcome outcome;
            @XmlElement(name = "Data")
            protected List<Data> data;

            /**
             * Gets the value of the action property.
             * 
             * @return
             *     possible object is
             *     {@link AuthenticateAction }
             *     
             */
            public AuthenticateAction getAction() {
                return action;
            }

            /**
             * Sets the value of the action property.
             * 
             * @param value
             *     allowed object is
             *     {@link AuthenticateAction }
             *     
             */
            public void setAction(AuthenticateAction value) {
                this.action = value;
            }

            /**
             * Gets the value of the logonType property.
             * 
             * @return
             *     possible object is
             *     {@link AuthenticateLogonType }
             *     
             */
            public AuthenticateLogonType getLogonType() {
                return logonType;
            }

            /**
             * Sets the value of the logonType property.
             * 
             * @param value
             *     allowed object is
             *     {@link AuthenticateLogonType }
             *     
             */
            public void setLogonType(AuthenticateLogonType value) {
                this.logonType = value;
            }

            /**
             * Gets the value of the user property.
             * 
             * @return
             *     possible object is
             *     {@link User }
             *     
             */
            public User getUser() {
                return user;
            }

            /**
             * Sets the value of the user property.
             * 
             * @param value
             *     allowed object is
             *     {@link User }
             *     
             */
            public void setUser(User value) {
                this.user = value;
            }

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
             * Gets the value of the group property.
             * 
             * @return
             *     possible object is
             *     {@link Group }
             *     
             */
            public Group getGroup() {
                return group;
            }

            /**
             * Sets the value of the group property.
             * 
             * @param value
             *     allowed object is
             *     {@link Group }
             *     
             */
            public void setGroup(Group value) {
                this.group = value;
            }

            /**
             * Gets the value of the outcome property.
             * 
             * @return
             *     possible object is
             *     {@link AuthenticateOutcome }
             *     
             */
            public AuthenticateOutcome getOutcome() {
                return outcome;
            }

            /**
             * Sets the value of the outcome property.
             * 
             * @param value
             *     allowed object is
             *     {@link AuthenticateOutcome }
             *     
             */
            public void setOutcome(AuthenticateOutcome value) {
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


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected         content contained within this class.
         * 
         * <pre>
         * &lt;complexType&gt;
         *   &lt;complexContent&gt;
         *     &lt;extension base="{event-logging:3}BaseMultiObject"&gt;
         *       &lt;sequence&gt;
         *         &lt;element name="Action" type="{event-logging:3}Authorisation" minOccurs="0"/&gt;
         *         &lt;element name="AddGroups" minOccurs="0"&gt;
         *           &lt;complexType&gt;
         *             &lt;complexContent&gt;
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                 &lt;sequence&gt;
         *                   &lt;element name="Group" type="{event-logging:3}Group" maxOccurs="unbounded" minOccurs="0"/&gt;
         *                 &lt;/sequence&gt;
         *               &lt;/restriction&gt;
         *             &lt;/complexContent&gt;
         *           &lt;/complexType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="RemoveGroups" minOccurs="0"&gt;
         *           &lt;complexType&gt;
         *             &lt;complexContent&gt;
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                 &lt;sequence&gt;
         *                   &lt;element name="Group" type="{event-logging:3}Group" maxOccurs="unbounded" minOccurs="0"/&gt;
         *                 &lt;/sequence&gt;
         *               &lt;/restriction&gt;
         *             &lt;/complexContent&gt;
         *           &lt;/complexType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="Outcome" type="{event-logging:3}Outcome" minOccurs="0"/&gt;
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
        @XmlType(name = "", propOrder = {
            "action",
            "addGroups",
            "removeGroups",
            "outcome",
            "data"
        })
        public static class Authorise
            extends BaseMultiObject
        {

            @XmlElement(name = "Action")
            @XmlSchemaType(name = "string")
            protected Authorisation action;
            @XmlElement(name = "AddGroups")
            protected Event.EventDetail.Authorise.AddGroups addGroups;
            @XmlElement(name = "RemoveGroups")
            protected Event.EventDetail.Authorise.RemoveGroups removeGroups;
            @XmlElement(name = "Outcome")
            protected Outcome outcome;
            @XmlElement(name = "Data")
            protected List<Data> data;

            /**
             * Gets the value of the action property.
             * 
             * @return
             *     possible object is
             *     {@link Authorisation }
             *     
             */
            public Authorisation getAction() {
                return action;
            }

            /**
             * Sets the value of the action property.
             * 
             * @param value
             *     allowed object is
             *     {@link Authorisation }
             *     
             */
            public void setAction(Authorisation value) {
                this.action = value;
            }

            /**
             * Gets the value of the addGroups property.
             * 
             * @return
             *     possible object is
             *     {@link Event.EventDetail.Authorise.AddGroups }
             *     
             */
            public Event.EventDetail.Authorise.AddGroups getAddGroups() {
                return addGroups;
            }

            /**
             * Sets the value of the addGroups property.
             * 
             * @param value
             *     allowed object is
             *     {@link Event.EventDetail.Authorise.AddGroups }
             *     
             */
            public void setAddGroups(Event.EventDetail.Authorise.AddGroups value) {
                this.addGroups = value;
            }

            /**
             * Gets the value of the removeGroups property.
             * 
             * @return
             *     possible object is
             *     {@link Event.EventDetail.Authorise.RemoveGroups }
             *     
             */
            public Event.EventDetail.Authorise.RemoveGroups getRemoveGroups() {
                return removeGroups;
            }

            /**
             * Sets the value of the removeGroups property.
             * 
             * @param value
             *     allowed object is
             *     {@link Event.EventDetail.Authorise.RemoveGroups }
             *     
             */
            public void setRemoveGroups(Event.EventDetail.Authorise.RemoveGroups value) {
                this.removeGroups = value;
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
             *       &lt;sequence&gt;
             *         &lt;element name="Group" type="{event-logging:3}Group" maxOccurs="unbounded" minOccurs="0"/&gt;
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
                "group"
            })
            public static class AddGroups {

                @XmlElement(name = "Group")
                protected List<Group> group;

                /**
                 * Gets the value of the group property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the group property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getGroup().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link Group }
                 * 
                 * 
                 */
                public List<Group> getGroup() {
                    if (group == null) {
                        group = new ArrayList<Group>();
                    }
                    return this.group;
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
             *         &lt;element name="Group" type="{event-logging:3}Group" maxOccurs="unbounded" minOccurs="0"/&gt;
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
                "group"
            })
            public static class RemoveGroups {

                @XmlElement(name = "Group")
                protected List<Group> group;

                /**
                 * Gets the value of the group property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the group property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getGroup().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link Group }
                 * 
                 * 
                 */
                public List<Group> getGroup() {
                    if (group == null) {
                        group = new ArrayList<Group>();
                    }
                    return this.group;
                }

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
         *       &lt;choice&gt;
         *         &lt;element name="Bind" type="{event-logging:3}Network"/&gt;
         *         &lt;element name="Connect" type="{event-logging:3}Network"/&gt;
         *         &lt;element name="Open" type="{event-logging:3}Network"/&gt;
         *         &lt;element name="Close" type="{event-logging:3}Network"/&gt;
         *         &lt;element name="Send" type="{event-logging:3}Network"/&gt;
         *         &lt;element name="Receive" type="{event-logging:3}Network"/&gt;
         *         &lt;element name="Listen" type="{event-logging:3}Network"/&gt;
         *         &lt;element name="Permit" type="{event-logging:3}Network"/&gt;
         *         &lt;element name="Deny" type="{event-logging:3}Network"/&gt;
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
            "bind",
            "connect",
            "open",
            "close",
            "send",
            "receive",
            "listen",
            "permit",
            "deny"
        })
        public static class Network {

            @XmlElement(name = "Bind")
            protected event.logging.Network bind;
            @XmlElement(name = "Connect")
            protected event.logging.Network connect;
            @XmlElement(name = "Open")
            protected event.logging.Network open;
            @XmlElement(name = "Close")
            protected event.logging.Network close;
            @XmlElement(name = "Send")
            protected event.logging.Network send;
            @XmlElement(name = "Receive")
            protected event.logging.Network receive;
            @XmlElement(name = "Listen")
            protected event.logging.Network listen;
            @XmlElement(name = "Permit")
            protected event.logging.Network permit;
            @XmlElement(name = "Deny")
            protected event.logging.Network deny;

            /**
             * Gets the value of the bind property.
             * 
             * @return
             *     possible object is
             *     {@link event.logging.Network }
             *     
             */
            public event.logging.Network getBind() {
                return bind;
            }

            /**
             * Sets the value of the bind property.
             * 
             * @param value
             *     allowed object is
             *     {@link event.logging.Network }
             *     
             */
            public void setBind(event.logging.Network value) {
                this.bind = value;
            }

            /**
             * Gets the value of the connect property.
             * 
             * @return
             *     possible object is
             *     {@link event.logging.Network }
             *     
             */
            public event.logging.Network getConnect() {
                return connect;
            }

            /**
             * Sets the value of the connect property.
             * 
             * @param value
             *     allowed object is
             *     {@link event.logging.Network }
             *     
             */
            public void setConnect(event.logging.Network value) {
                this.connect = value;
            }

            /**
             * Gets the value of the open property.
             * 
             * @return
             *     possible object is
             *     {@link event.logging.Network }
             *     
             */
            public event.logging.Network getOpen() {
                return open;
            }

            /**
             * Sets the value of the open property.
             * 
             * @param value
             *     allowed object is
             *     {@link event.logging.Network }
             *     
             */
            public void setOpen(event.logging.Network value) {
                this.open = value;
            }

            /**
             * Gets the value of the close property.
             * 
             * @return
             *     possible object is
             *     {@link event.logging.Network }
             *     
             */
            public event.logging.Network getClose() {
                return close;
            }

            /**
             * Sets the value of the close property.
             * 
             * @param value
             *     allowed object is
             *     {@link event.logging.Network }
             *     
             */
            public void setClose(event.logging.Network value) {
                this.close = value;
            }

            /**
             * Gets the value of the send property.
             * 
             * @return
             *     possible object is
             *     {@link event.logging.Network }
             *     
             */
            public event.logging.Network getSend() {
                return send;
            }

            /**
             * Sets the value of the send property.
             * 
             * @param value
             *     allowed object is
             *     {@link event.logging.Network }
             *     
             */
            public void setSend(event.logging.Network value) {
                this.send = value;
            }

            /**
             * Gets the value of the receive property.
             * 
             * @return
             *     possible object is
             *     {@link event.logging.Network }
             *     
             */
            public event.logging.Network getReceive() {
                return receive;
            }

            /**
             * Sets the value of the receive property.
             * 
             * @param value
             *     allowed object is
             *     {@link event.logging.Network }
             *     
             */
            public void setReceive(event.logging.Network value) {
                this.receive = value;
            }

            /**
             * Gets the value of the listen property.
             * 
             * @return
             *     possible object is
             *     {@link event.logging.Network }
             *     
             */
            public event.logging.Network getListen() {
                return listen;
            }

            /**
             * Sets the value of the listen property.
             * 
             * @param value
             *     allowed object is
             *     {@link event.logging.Network }
             *     
             */
            public void setListen(event.logging.Network value) {
                this.listen = value;
            }

            /**
             * Gets the value of the permit property.
             * 
             * @return
             *     possible object is
             *     {@link event.logging.Network }
             *     
             */
            public event.logging.Network getPermit() {
                return permit;
            }

            /**
             * Sets the value of the permit property.
             * 
             * @param value
             *     allowed object is
             *     {@link event.logging.Network }
             *     
             */
            public void setPermit(event.logging.Network value) {
                this.permit = value;
            }

            /**
             * Gets the value of the deny property.
             * 
             * @return
             *     possible object is
             *     {@link event.logging.Network }
             *     
             */
            public event.logging.Network getDeny() {
                return deny;
            }

            /**
             * Sets the value of the deny property.
             * 
             * @param value
             *     allowed object is
             *     {@link event.logging.Network }
             *     
             */
            public void setDeny(event.logging.Network value) {
                this.deny = value;
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
         *         &lt;element name="Action" type="{event-logging:3}PrintAction"/&gt;
         *         &lt;element name="PrintJob"&gt;
         *           &lt;complexType&gt;
         *             &lt;complexContent&gt;
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                 &lt;sequence&gt;
         *                   &lt;element name="Document" type="{event-logging:3}Document"/&gt;
         *                   &lt;element name="Pages" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/&gt;
         *                   &lt;element name="Size" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/&gt;
         *                   &lt;element name="Submitted" type="{event-logging:3}DateTime" minOccurs="0"/&gt;
         *                 &lt;/sequence&gt;
         *               &lt;/restriction&gt;
         *             &lt;/complexContent&gt;
         *           &lt;/complexType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="PrintSettings" minOccurs="0"&gt;
         *           &lt;complexType&gt;
         *             &lt;complexContent&gt;
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                 &lt;sequence&gt;
         *                   &lt;element name="PaperSize" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *                   &lt;element name="Orientation" type="{event-logging:3}PrintSettingsOrientation" minOccurs="0"/&gt;
         *                   &lt;element name="Colour" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
         *                   &lt;element name="DeviceFonts" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
         *                   &lt;element name="Data" type="{event-logging:3}Data" maxOccurs="unbounded" minOccurs="0"/&gt;
         *                 &lt;/sequence&gt;
         *               &lt;/restriction&gt;
         *             &lt;/complexContent&gt;
         *           &lt;/complexType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="Printer" type="{event-logging:3}Device" minOccurs="0"/&gt;
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
        @XmlType(name = "", propOrder = {
            "action",
            "printJob",
            "printSettings",
            "printer",
            "outcome",
            "data"
        })
        public static class Print {

            @XmlElement(name = "Action", required = true)
            @XmlSchemaType(name = "string")
            protected PrintAction action;
            @XmlElement(name = "PrintJob", required = true)
            protected Event.EventDetail.Print.PrintJob printJob;
            @XmlElement(name = "PrintSettings")
            protected Event.EventDetail.Print.PrintSettings printSettings;
            @XmlElement(name = "Printer")
            protected Device printer;
            @XmlElement(name = "Outcome")
            protected Outcome outcome;
            @XmlElement(name = "Data")
            protected List<Data> data;

            /**
             * Gets the value of the action property.
             * 
             * @return
             *     possible object is
             *     {@link PrintAction }
             *     
             */
            public PrintAction getAction() {
                return action;
            }

            /**
             * Sets the value of the action property.
             * 
             * @param value
             *     allowed object is
             *     {@link PrintAction }
             *     
             */
            public void setAction(PrintAction value) {
                this.action = value;
            }

            /**
             * Gets the value of the printJob property.
             * 
             * @return
             *     possible object is
             *     {@link Event.EventDetail.Print.PrintJob }
             *     
             */
            public Event.EventDetail.Print.PrintJob getPrintJob() {
                return printJob;
            }

            /**
             * Sets the value of the printJob property.
             * 
             * @param value
             *     allowed object is
             *     {@link Event.EventDetail.Print.PrintJob }
             *     
             */
            public void setPrintJob(Event.EventDetail.Print.PrintJob value) {
                this.printJob = value;
            }

            /**
             * Gets the value of the printSettings property.
             * 
             * @return
             *     possible object is
             *     {@link Event.EventDetail.Print.PrintSettings }
             *     
             */
            public Event.EventDetail.Print.PrintSettings getPrintSettings() {
                return printSettings;
            }

            /**
             * Sets the value of the printSettings property.
             * 
             * @param value
             *     allowed object is
             *     {@link Event.EventDetail.Print.PrintSettings }
             *     
             */
            public void setPrintSettings(Event.EventDetail.Print.PrintSettings value) {
                this.printSettings = value;
            }

            /**
             * Gets the value of the printer property.
             * 
             * @return
             *     possible object is
             *     {@link Device }
             *     
             */
            public Device getPrinter() {
                return printer;
            }

            /**
             * Sets the value of the printer property.
             * 
             * @param value
             *     allowed object is
             *     {@link Device }
             *     
             */
            public void setPrinter(Device value) {
                this.printer = value;
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
             *       &lt;sequence&gt;
             *         &lt;element name="Document" type="{event-logging:3}Document"/&gt;
             *         &lt;element name="Pages" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/&gt;
             *         &lt;element name="Size" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/&gt;
             *         &lt;element name="Submitted" type="{event-logging:3}DateTime" minOccurs="0"/&gt;
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
                "document",
                "pages",
                "size",
                "submitted"
            })
            public static class PrintJob {

                @XmlElement(name = "Document", required = true)
                protected Document document;
                @XmlElement(name = "Pages")
                @XmlSchemaType(name = "nonNegativeInteger")
                protected BigInteger pages;
                @XmlElement(name = "Size")
                @XmlSchemaType(name = "nonNegativeInteger")
                protected BigInteger size;
                @XmlElement(name = "Submitted", type = String.class)
                @XmlJavaTypeAdapter(Adapter2 .class)
                @XmlSchemaType(name = "dateTime")
                protected Date submitted;

                /**
                 * Gets the value of the document property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Document }
                 *     
                 */
                public Document getDocument() {
                    return document;
                }

                /**
                 * Sets the value of the document property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Document }
                 *     
                 */
                public void setDocument(Document value) {
                    this.document = value;
                }

                /**
                 * Gets the value of the pages property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link BigInteger }
                 *     
                 */
                public BigInteger getPages() {
                    return pages;
                }

                /**
                 * Sets the value of the pages property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link BigInteger }
                 *     
                 */
                public void setPages(BigInteger value) {
                    this.pages = value;
                }

                /**
                 * Gets the value of the size property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link BigInteger }
                 *     
                 */
                public BigInteger getSize() {
                    return size;
                }

                /**
                 * Sets the value of the size property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link BigInteger }
                 *     
                 */
                public void setSize(BigInteger value) {
                    this.size = value;
                }

                /**
                 * Gets the value of the submitted property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public Date getSubmitted() {
                    return submitted;
                }

                /**
                 * Sets the value of the submitted property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setSubmitted(Date value) {
                    this.submitted = value;
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
             *         &lt;element name="PaperSize" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
             *         &lt;element name="Orientation" type="{event-logging:3}PrintSettingsOrientation" minOccurs="0"/&gt;
             *         &lt;element name="Colour" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
             *         &lt;element name="DeviceFonts" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
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
            @XmlType(name = "", propOrder = {
                "paperSize",
                "orientation",
                "colour",
                "deviceFonts",
                "data"
            })
            public static class PrintSettings {

                @XmlElement(name = "PaperSize")
                protected String paperSize;
                @XmlElement(name = "Orientation")
                @XmlSchemaType(name = "string")
                protected PrintSettingsOrientation orientation;
                @XmlElement(name = "Colour")
                protected Boolean colour;
                @XmlElement(name = "DeviceFonts")
                protected Boolean deviceFonts;
                @XmlElement(name = "Data")
                protected List<Data> data;

                /**
                 * Gets the value of the paperSize property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getPaperSize() {
                    return paperSize;
                }

                /**
                 * Sets the value of the paperSize property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setPaperSize(String value) {
                    this.paperSize = value;
                }

                /**
                 * Gets the value of the orientation property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link PrintSettingsOrientation }
                 *     
                 */
                public PrintSettingsOrientation getOrientation() {
                    return orientation;
                }

                /**
                 * Sets the value of the orientation property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link PrintSettingsOrientation }
                 *     
                 */
                public void setOrientation(PrintSettingsOrientation value) {
                    this.orientation = value;
                }

                /**
                 * Gets the value of the colour property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Boolean }
                 *     
                 */
                public Boolean isColour() {
                    return colour;
                }

                /**
                 * Sets the value of the colour property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Boolean }
                 *     
                 */
                public void setColour(Boolean value) {
                    this.colour = value;
                }

                /**
                 * Gets the value of the deviceFonts property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Boolean }
                 *     
                 */
                public Boolean isDeviceFonts() {
                    return deviceFonts;
                }

                /**
                 * Sets the value of the deviceFonts property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Boolean }
                 *     
                 */
                public void setDeviceFonts(Boolean value) {
                    this.deviceFonts = value;
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
         *         &lt;element name="Action" type="{event-logging:3}ProcessAction"/&gt;
         *         &lt;element name="Type" type="{event-logging:3}ProcessType"/&gt;
         *         &lt;element name="Command" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="Arguments" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="ProcessId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="ThreadId" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" minOccurs="0"/&gt;
         *         &lt;element name="Rule" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="Input" type="{event-logging:3}MultiObject" minOccurs="0"/&gt;
         *         &lt;element name="Output" type="{event-logging:3}MultiObject" minOccurs="0"/&gt;
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
        @XmlType(name = "", propOrder = {
            "action",
            "type",
            "command",
            "arguments",
            "processId",
            "threadId",
            "rule",
            "input",
            "output",
            "outcome",
            "data"
        })
        public static class Process {

            @XmlElement(name = "Action", required = true)
            @XmlSchemaType(name = "string")
            protected ProcessAction action;
            @XmlElement(name = "Type", required = true)
            @XmlSchemaType(name = "string")
            protected ProcessType type;
            @XmlElement(name = "Command", required = true)
            protected String command;
            @XmlElement(name = "Arguments")
            protected String arguments;
            @XmlElement(name = "ProcessId")
            protected String processId;
            @XmlElement(name = "ThreadId")
            @XmlSchemaType(name = "positiveInteger")
            protected BigInteger threadId;
            @XmlElement(name = "Rule")
            protected String rule;
            @XmlElement(name = "Input")
            protected MultiObject input;
            @XmlElement(name = "Output")
            protected MultiObject output;
            @XmlElement(name = "Outcome")
            protected Outcome outcome;
            @XmlElement(name = "Data")
            protected List<Data> data;

            /**
             * Gets the value of the action property.
             * 
             * @return
             *     possible object is
             *     {@link ProcessAction }
             *     
             */
            public ProcessAction getAction() {
                return action;
            }

            /**
             * Sets the value of the action property.
             * 
             * @param value
             *     allowed object is
             *     {@link ProcessAction }
             *     
             */
            public void setAction(ProcessAction value) {
                this.action = value;
            }

            /**
             * Gets the value of the type property.
             * 
             * @return
             *     possible object is
             *     {@link ProcessType }
             *     
             */
            public ProcessType getType() {
                return type;
            }

            /**
             * Sets the value of the type property.
             * 
             * @param value
             *     allowed object is
             *     {@link ProcessType }
             *     
             */
            public void setType(ProcessType value) {
                this.type = value;
            }

            /**
             * Gets the value of the command property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getCommand() {
                return command;
            }

            /**
             * Sets the value of the command property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setCommand(String value) {
                this.command = value;
            }

            /**
             * Gets the value of the arguments property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getArguments() {
                return arguments;
            }

            /**
             * Sets the value of the arguments property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setArguments(String value) {
                this.arguments = value;
            }

            /**
             * Gets the value of the processId property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getProcessId() {
                return processId;
            }

            /**
             * Sets the value of the processId property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setProcessId(String value) {
                this.processId = value;
            }

            /**
             * Gets the value of the threadId property.
             * 
             * @return
             *     possible object is
             *     {@link BigInteger }
             *     
             */
            public BigInteger getThreadId() {
                return threadId;
            }

            /**
             * Sets the value of the threadId property.
             * 
             * @param value
             *     allowed object is
             *     {@link BigInteger }
             *     
             */
            public void setThreadId(BigInteger value) {
                this.threadId = value;
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

            /**
             * Gets the value of the input property.
             * 
             * @return
             *     possible object is
             *     {@link MultiObject }
             *     
             */
            public MultiObject getInput() {
                return input;
            }

            /**
             * Sets the value of the input property.
             * 
             * @param value
             *     allowed object is
             *     {@link MultiObject }
             *     
             */
            public void setInput(MultiObject value) {
                this.input = value;
            }

            /**
             * Gets the value of the output property.
             * 
             * @return
             *     possible object is
             *     {@link MultiObject }
             *     
             */
            public MultiObject getOutput() {
                return output;
            }

            /**
             * Sets the value of the output property.
             * 
             * @param value
             *     allowed object is
             *     {@link MultiObject }
             *     
             */
            public void setOutput(MultiObject value) {
                this.output = value;
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
        @XmlType(name = "", propOrder = {
            "data"
        })
        public static class Unknown {

            @XmlElement(name = "Data")
            protected List<Data> data;

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
         *         &lt;element name="Before" type="{event-logging:3}MultiObject" minOccurs="0"/&gt;
         *         &lt;element name="After" type="{event-logging:3}MultiObject"/&gt;
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
        @XmlType(name = "", propOrder = {
            "before",
            "after",
            "outcome",
            "data"
        })
        public static class Update {

            @XmlElement(name = "Before")
            protected MultiObject before;
            @XmlElement(name = "After", required = true)
            protected MultiObject after;
            @XmlElement(name = "Outcome")
            protected Outcome outcome;
            @XmlElement(name = "Data")
            protected List<Data> data;

            /**
             * Gets the value of the before property.
             * 
             * @return
             *     possible object is
             *     {@link MultiObject }
             *     
             */
            public MultiObject getBefore() {
                return before;
            }

            /**
             * Sets the value of the before property.
             * 
             * @param value
             *     allowed object is
             *     {@link MultiObject }
             *     
             */
            public void setBefore(MultiObject value) {
                this.before = value;
            }

            /**
             * Gets the value of the after property.
             * 
             * @return
             *     possible object is
             *     {@link MultiObject }
             *     
             */
            public MultiObject getAfter() {
                return after;
            }

            /**
             * Sets the value of the after property.
             * 
             * @param value
             *     allowed object is
             *     {@link MultiObject }
             *     
             */
            public void setAfter(MultiObject value) {
                this.after = value;
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
     *         &lt;element name="System" type="{event-logging:3}System"/&gt;
     *         &lt;element name="Generator" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;choice&gt;
     *           &lt;sequence&gt;
     *             &lt;element name="Device" type="{event-logging:3}Device"/&gt;
     *             &lt;element name="Client" type="{event-logging:3}Device" minOccurs="0"/&gt;
     *             &lt;element name="Server" type="{event-logging:3}Device" minOccurs="0"/&gt;
     *           &lt;/sequence&gt;
     *           &lt;sequence&gt;
     *             &lt;element name="Door"&gt;
     *               &lt;complexType&gt;
     *                 &lt;complexContent&gt;
     *                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                     &lt;sequence&gt;
     *                       &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                       &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                       &lt;element name="Location" type="{event-logging:3}Location"/&gt;
     *                       &lt;element name="SingleEntry" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
     *                       &lt;element name="RemoveAll" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
     *                       &lt;element name="AddAccess"&gt;
     *                         &lt;complexType&gt;
     *                           &lt;complexContent&gt;
     *                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                               &lt;sequence&gt;
     *                                 &lt;element name="AccessZone" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded"/&gt;
     *                               &lt;/sequence&gt;
     *                             &lt;/restriction&gt;
     *                           &lt;/complexContent&gt;
     *                         &lt;/complexType&gt;
     *                       &lt;/element&gt;
     *                     &lt;/sequence&gt;
     *                   &lt;/restriction&gt;
     *                 &lt;/complexContent&gt;
     *               &lt;/complexType&gt;
     *             &lt;/element&gt;
     *           &lt;/sequence&gt;
     *         &lt;/choice&gt;
     *         &lt;element name="User" type="{event-logging:3}User" minOccurs="0"/&gt;
     *         &lt;element name="RunAs" type="{event-logging:3}User" minOccurs="0"/&gt;
     *         &lt;element name="Interactive" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
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
    @XmlType(name = "", propOrder = {
        "system",
        "generator",
        "device",
        "client",
        "server",
        "door",
        "user",
        "runAs",
        "interactive",
        "data"
    })
    public static class EventSource {

        @XmlElement(name = "System", required = true)
        protected System system;
        @XmlElement(name = "Generator", required = true)
        protected String generator;
        @XmlElement(name = "Device")
        protected Device device;
        @XmlElement(name = "Client")
        protected Device client;
        @XmlElement(name = "Server")
        protected Device server;
        @XmlElement(name = "Door")
        protected Event.EventSource.Door door;
        @XmlElement(name = "User")
        protected User user;
        @XmlElement(name = "RunAs")
        protected User runAs;
        @XmlElement(name = "Interactive")
        protected Boolean interactive;
        @XmlElement(name = "Data")
        protected List<Data> data;

        /**
         * Gets the value of the system property.
         * 
         * @return
         *     possible object is
         *     {@link System }
         *     
         */
        public System getSystem() {
            return system;
        }

        /**
         * Sets the value of the system property.
         * 
         * @param value
         *     allowed object is
         *     {@link System }
         *     
         */
        public void setSystem(System value) {
            this.system = value;
        }

        /**
         * Gets the value of the generator property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getGenerator() {
            return generator;
        }

        /**
         * Sets the value of the generator property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setGenerator(String value) {
            this.generator = value;
        }

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
         * Gets the value of the client property.
         * 
         * @return
         *     possible object is
         *     {@link Device }
         *     
         */
        public Device getClient() {
            return client;
        }

        /**
         * Sets the value of the client property.
         * 
         * @param value
         *     allowed object is
         *     {@link Device }
         *     
         */
        public void setClient(Device value) {
            this.client = value;
        }

        /**
         * Gets the value of the server property.
         * 
         * @return
         *     possible object is
         *     {@link Device }
         *     
         */
        public Device getServer() {
            return server;
        }

        /**
         * Sets the value of the server property.
         * 
         * @param value
         *     allowed object is
         *     {@link Device }
         *     
         */
        public void setServer(Device value) {
            this.server = value;
        }

        /**
         * Gets the value of the door property.
         * 
         * @return
         *     possible object is
         *     {@link Event.EventSource.Door }
         *     
         */
        public Event.EventSource.Door getDoor() {
            return door;
        }

        /**
         * Sets the value of the door property.
         * 
         * @param value
         *     allowed object is
         *     {@link Event.EventSource.Door }
         *     
         */
        public void setDoor(Event.EventSource.Door value) {
            this.door = value;
        }

        /**
         * Gets the value of the user property.
         * 
         * @return
         *     possible object is
         *     {@link User }
         *     
         */
        public User getUser() {
            return user;
        }

        /**
         * Sets the value of the user property.
         * 
         * @param value
         *     allowed object is
         *     {@link User }
         *     
         */
        public void setUser(User value) {
            this.user = value;
        }

        /**
         * Gets the value of the runAs property.
         * 
         * @return
         *     possible object is
         *     {@link User }
         *     
         */
        public User getRunAs() {
            return runAs;
        }

        /**
         * Sets the value of the runAs property.
         * 
         * @param value
         *     allowed object is
         *     {@link User }
         *     
         */
        public void setRunAs(User value) {
            this.runAs = value;
        }

        /**
         * Gets the value of the interactive property.
         * 
         * @return
         *     possible object is
         *     {@link Boolean }
         *     
         */
        public Boolean isInteractive() {
            return interactive;
        }

        /**
         * Sets the value of the interactive property.
         * 
         * @param value
         *     allowed object is
         *     {@link Boolean }
         *     
         */
        public void setInteractive(Boolean value) {
            this.interactive = value;
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
         *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="Location" type="{event-logging:3}Location"/&gt;
         *         &lt;element name="SingleEntry" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
         *         &lt;element name="RemoveAll" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
         *         &lt;element name="AddAccess"&gt;
         *           &lt;complexType&gt;
         *             &lt;complexContent&gt;
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                 &lt;sequence&gt;
         *                   &lt;element name="AccessZone" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded"/&gt;
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
            "name",
            "description",
            "location",
            "singleEntry",
            "removeAll",
            "addAccess"
        })
        public static class Door {

            @XmlElement(name = "Name", required = true)
            protected String name;
            @XmlElement(name = "Description")
            protected String description;
            @XmlElement(name = "Location", required = true)
            protected Location location;
            @XmlElement(name = "SingleEntry")
            protected boolean singleEntry;
            @XmlElement(name = "RemoveAll")
            protected boolean removeAll;
            @XmlElement(name = "AddAccess", required = true)
            protected Event.EventSource.Door.AddAccess addAccess;

            /**
             * Gets the value of the name property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getName() {
                return name;
            }

            /**
             * Sets the value of the name property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setName(String value) {
                this.name = value;
            }

            /**
             * Gets the value of the description property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getDescription() {
                return description;
            }

            /**
             * Sets the value of the description property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setDescription(String value) {
                this.description = value;
            }

            /**
             * Gets the value of the location property.
             * 
             * @return
             *     possible object is
             *     {@link Location }
             *     
             */
            public Location getLocation() {
                return location;
            }

            /**
             * Sets the value of the location property.
             * 
             * @param value
             *     allowed object is
             *     {@link Location }
             *     
             */
            public void setLocation(Location value) {
                this.location = value;
            }

            /**
             * Gets the value of the singleEntry property.
             * 
             */
            public boolean isSingleEntry() {
                return singleEntry;
            }

            /**
             * Sets the value of the singleEntry property.
             * 
             */
            public void setSingleEntry(boolean value) {
                this.singleEntry = value;
            }

            /**
             * Gets the value of the removeAll property.
             * 
             */
            public boolean isRemoveAll() {
                return removeAll;
            }

            /**
             * Sets the value of the removeAll property.
             * 
             */
            public void setRemoveAll(boolean value) {
                this.removeAll = value;
            }

            /**
             * Gets the value of the addAccess property.
             * 
             * @return
             *     possible object is
             *     {@link Event.EventSource.Door.AddAccess }
             *     
             */
            public Event.EventSource.Door.AddAccess getAddAccess() {
                return addAccess;
            }

            /**
             * Sets the value of the addAccess property.
             * 
             * @param value
             *     allowed object is
             *     {@link Event.EventSource.Door.AddAccess }
             *     
             */
            public void setAddAccess(Event.EventSource.Door.AddAccess value) {
                this.addAccess = value;
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
             *         &lt;element name="AccessZone" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded"/&gt;
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
                "accessZone"
            })
            public static class AddAccess {

                @XmlElement(name = "AccessZone", required = true)
                protected List<String> accessZone;

                /**
                 * Gets the value of the accessZone property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the accessZone property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getAccessZone().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link String }
                 * 
                 * 
                 */
                public List<String> getAccessZone() {
                    if (accessZone == null) {
                        accessZone = new ArrayList<String>();
                    }
                    return this.accessZone;
                }

            }

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
     *         &lt;element name="TimeCreated" type="{event-logging:3}DateTime"/&gt;
     *         &lt;element name="TimeSource" type="{event-logging:3}Device" minOccurs="0"/&gt;
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
        "timeCreated",
        "timeSource"
    })
    public static class EventTime {

        @XmlElement(name = "TimeCreated", required = true, type = String.class)
        @XmlJavaTypeAdapter(Adapter2 .class)
        @XmlSchemaType(name = "dateTime")
        protected Date timeCreated;
        @XmlElement(name = "TimeSource")
        protected Device timeSource;

        /**
         * Gets the value of the timeCreated property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public Date getTimeCreated() {
            return timeCreated;
        }

        /**
         * Sets the value of the timeCreated property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTimeCreated(Date value) {
            this.timeCreated = value;
        }

        /**
         * Gets the value of the timeSource property.
         * 
         * @return
         *     possible object is
         *     {@link Device }
         *     
         */
        public Device getTimeSource() {
            return timeSource;
        }

        /**
         * Sets the value of the timeSource property.
         * 
         * @param value
         *     allowed object is
         *     {@link Device }
         *     
         */
        public void setTimeSource(Device value) {
            this.timeSource = value;
        }

    }

}
