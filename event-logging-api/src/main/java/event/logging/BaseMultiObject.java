

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
 * <p>Java class for BaseMultiObject complex type.
 * 
 * <p>The following schema fragment specifies the expected         content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BaseMultiObject"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;choice maxOccurs="unbounded"&gt;
 *         &lt;element name="Banner" type="{event-logging:3}Banner"/&gt;
 *         &lt;element name="Chat" type="{event-logging:3}Chat"/&gt;
 *         &lt;element name="Configuration" type="{event-logging:3}Configuration"/&gt;
 *         &lt;element name="Criteria" type="{event-logging:3}Criteria"/&gt;
 *         &lt;element name="Document" type="{event-logging:3}Document"/&gt;
 *         &lt;element name="Email" type="{event-logging:3}Email"/&gt;
 *         &lt;element name="File" type="{event-logging:3}File"/&gt;
 *         &lt;element name="Folder" type="{event-logging:3}Folder"/&gt;
 *         &lt;element name="Group" type="{event-logging:3}Group"/&gt;
 *         &lt;element name="GroupChat" type="{event-logging:3}GroupChat"/&gt;
 *         &lt;element name="Object" type="{event-logging:3}Object"/&gt;
 *         &lt;element name="SearchResult" type="{event-logging:3}SearchResult"/&gt;
 *         &lt;element name="Shortcut" type="{event-logging:3}Shortcut"/&gt;
 *         &lt;element name="User" type="{event-logging:3}User"/&gt;
 *         &lt;element name="VirtualSession" type="{event-logging:3}VirtualSession"/&gt;
 *         &lt;element name="VOIP" type="{event-logging:3}VOIP"/&gt;
 *         &lt;element name="Resource" type="{event-logging:3}Resource"/&gt;
 *         &lt;element name="Association" type="{event-logging:3}Association"/&gt;
 *       &lt;/choice&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BaseMultiObject", propOrder = {
    "objects"
})
@XmlSeeAlso({
    event.logging.Event.EventDetail.Authorise.class,
    MultiObject.class,
    ObjectOutcome.class
})
public abstract class BaseMultiObject {

    @XmlElements({
        @XmlElement(name = "Association", type = Association.class),
        @XmlElement(name = "Banner", type = Banner.class),
        @XmlElement(name = "Chat", type = Chat.class),
        @XmlElement(name = "Configuration", type = Configuration.class),
        @XmlElement(name = "Criteria", type = Criteria.class),
        @XmlElement(name = "Document", type = Document.class),
        @XmlElement(name = "Email", type = Email.class),
        @XmlElement(name = "File", type = File.class),
        @XmlElement(name = "Folder", type = Folder.class),
        @XmlElement(name = "Group", type = Group.class),
        @XmlElement(name = "GroupChat", type = GroupChat.class),
        @XmlElement(name = "Object", type = Object.class),
        @XmlElement(name = "Resource", type = Resource.class),
        @XmlElement(name = "SearchResult", type = SearchResult.class),
        @XmlElement(name = "Shortcut", type = Shortcut.class),
        @XmlElement(name = "User", type = User.class),
        @XmlElement(name = "VOIP", type = VOIP.class),
        @XmlElement(name = "VirtualSession", type = VirtualSession.class)
    })
    protected List<BaseObject> objects;

    /**
     * Gets the value of the objects property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the objects property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getObjects().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Association }
     * {@link Banner }
     * {@link Chat }
     * {@link Configuration }
     * {@link Criteria }
     * {@link Document }
     * {@link Email }
     * {@link File }
     * {@link Folder }
     * {@link Group }
     * {@link GroupChat }
     * {@link Object }
     * {@link Resource }
     * {@link SearchResult }
     * {@link Shortcut }
     * {@link User }
     * {@link VOIP }
     * {@link VirtualSession }
     * 
     * 
     */
    public List<BaseObject> getObjects() {
        if (objects == null) {
            objects = new ArrayList<BaseObject>();
        }
        return this.objects;
    }

}
