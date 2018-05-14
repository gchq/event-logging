

package event.logging;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AlertPriority.
 * 
 * <p>The following schema fragment specifies the expected         content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="AlertPriority"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="None"/&gt;
 *     &lt;enumeration value="Low"/&gt;
 *     &lt;enumeration value="Normal"/&gt;
 *     &lt;enumeration value="High"/&gt;
 *     &lt;enumeration value="Urgent"/&gt;
 *     &lt;enumeration value="Immediate"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "AlertPriority")
@XmlEnum
public enum AlertPriority {

    @XmlEnumValue("None")
    NONE("None"),
    @XmlEnumValue("Low")
    LOW("Low"),
    @XmlEnumValue("Normal")
    NORMAL("Normal"),
    @XmlEnumValue("High")
    HIGH("High"),
    @XmlEnumValue("Urgent")
    URGENT("Urgent"),
    @XmlEnumValue("Immediate")
    IMMEDIATE("Immediate");
    private final String value;

    AlertPriority(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AlertPriority fromValue(String v) {
        for (AlertPriority c: AlertPriority.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
