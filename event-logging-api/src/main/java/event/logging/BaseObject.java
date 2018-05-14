

package event.logging;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BaseObject complex type.
 * 
 * <p>The following schema fragment specifies the expected         content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BaseObject"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;group ref="{event-logging:3}BaseObjectGroup"/&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BaseObject", propOrder = {
    "type",
    "id",
    "name",
    "description",
    "classification",
    "state",
    "groups",
    "permissions"
})
@XmlSeeAlso({
    Association.class,
    Banner.class,
    Configuration.class,
    Criteria.class,
    Document.class,
    Email.class,
    BaseChat.class,
    Group.class,
    Object.class,
    Resource.class,
    SearchResult.class,
    BaseFile.class,
    User.class,
    VOIP.class,
    VirtualSession.class
})
public abstract class BaseObject {

    @XmlElement(name = "Type")
    protected String type;
    @XmlElement(name = "Id")
    protected String id;
    @XmlElement(name = "Name")
    protected String name;
    @XmlElement(name = "Description")
    protected String description;
    @XmlElement(name = "Classification")
    protected Classification classification;
    @XmlElement(name = "State")
    protected String state;
    @XmlElement(name = "Groups")
    protected Groups groups;
    @XmlElement(name = "Permissions")
    protected BaseObject.Permissions permissions;

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

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
     * Gets the value of the state property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getState() {
        return state;
    }

    /**
     * Sets the value of the state property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setState(String value) {
        this.state = value;
    }

    /**
     * Gets the value of the groups property.
     * 
     * @return
     *     possible object is
     *     {@link Groups }
     *     
     */
    public Groups getGroups() {
        return groups;
    }

    /**
     * Sets the value of the groups property.
     * 
     * @param value
     *     allowed object is
     *     {@link Groups }
     *     
     */
    public void setGroups(Groups value) {
        this.groups = value;
    }

    /**
     * Gets the value of the permissions property.
     * 
     * @return
     *     possible object is
     *     {@link BaseObject.Permissions }
     *     
     */
    public BaseObject.Permissions getPermissions() {
        return permissions;
    }

    /**
     * Sets the value of the permissions property.
     * 
     * @param value
     *     allowed object is
     *     {@link BaseObject.Permissions }
     *     
     */
    public void setPermissions(BaseObject.Permissions value) {
        this.permissions = value;
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
     *         &lt;element name="Permission" maxOccurs="unbounded"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;choice&gt;
     *                     &lt;element name="User" type="{event-logging:3}User"/&gt;
     *                     &lt;element name="Group" type="{event-logging:3}Group"/&gt;
     *                   &lt;/choice&gt;
     *                   &lt;element name="Allow" type="{event-logging:3}PermissionAttribute" maxOccurs="unbounded" minOccurs="0"/&gt;
     *                   &lt;element name="Deny" type="{event-logging:3}PermissionAttribute" maxOccurs="unbounded" minOccurs="0"/&gt;
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
        "permission"
    })
    public static class Permissions {

        @XmlElement(name = "Permission", required = true)
        protected List<BaseObject.Permissions.Permission> permission;

        /**
         * Gets the value of the permission property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the permission property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getPermission().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link BaseObject.Permissions.Permission }
         * 
         * 
         */
        public List<BaseObject.Permissions.Permission> getPermission() {
            if (permission == null) {
                permission = new ArrayList<BaseObject.Permissions.Permission>();
            }
            return this.permission;
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
         *           &lt;element name="User" type="{event-logging:3}User"/&gt;
         *           &lt;element name="Group" type="{event-logging:3}Group"/&gt;
         *         &lt;/choice&gt;
         *         &lt;element name="Allow" type="{event-logging:3}PermissionAttribute" maxOccurs="unbounded" minOccurs="0"/&gt;
         *         &lt;element name="Deny" type="{event-logging:3}PermissionAttribute" maxOccurs="unbounded" minOccurs="0"/&gt;
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
            "user",
            "group",
            "allow",
            "deny"
        })
        public static class Permission {

            @XmlElement(name = "User")
            protected User user;
            @XmlElement(name = "Group")
            protected Group group;
            @XmlElement(name = "Allow")
            @XmlSchemaType(name = "string")
            protected List<PermissionAttribute> allow;
            @XmlElement(name = "Deny")
            @XmlSchemaType(name = "string")
            protected List<PermissionAttribute> deny;

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
             * Gets the value of the allow property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the allow property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getAllow().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link PermissionAttribute }
             * 
             * 
             */
            public List<PermissionAttribute> getAllow() {
                if (allow == null) {
                    allow = new ArrayList<PermissionAttribute>();
                }
                return this.allow;
            }

            /**
             * Gets the value of the deny property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the deny property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getDeny().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link PermissionAttribute }
             * 
             * 
             */
            public List<PermissionAttribute> getDeny() {
                if (deny == null) {
                    deny = new ArrayList<PermissionAttribute>();
                }
                return this.deny;
            }

        }

    }

}
