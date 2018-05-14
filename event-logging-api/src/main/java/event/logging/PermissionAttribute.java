

package event.logging;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PermissionAttribute.
 * 
 * <p>The following schema fragment specifies the expected         content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="PermissionAttribute"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Author"/&gt;
 *     &lt;enumeration value="Owner"/&gt;
 *     &lt;enumeration value="Read"/&gt;
 *     &lt;enumeration value="Write"/&gt;
 *     &lt;enumeration value="Execute"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "PermissionAttribute")
@XmlEnum
public enum PermissionAttribute {

    @XmlEnumValue("Author")
    AUTHOR("Author"),
    @XmlEnumValue("Owner")
    OWNER("Owner"),
    @XmlEnumValue("Read")
    READ("Read"),
    @XmlEnumValue("Write")
    WRITE("Write"),
    @XmlEnumValue("Execute")
    EXECUTE("Execute");
    private final String value;

    PermissionAttribute(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static PermissionAttribute fromValue(String v) {
        for (PermissionAttribute c: PermissionAttribute.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
