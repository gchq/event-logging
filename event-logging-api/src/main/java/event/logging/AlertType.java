

package event.logging;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AlertType.
 * 
 * <p>The following schema fragment specifies the expected         content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="AlertType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Vulnerability"/&gt;
 *     &lt;enumeration value="IDS"/&gt;
 *     &lt;enumeration value="Malware"/&gt;
 *     &lt;enumeration value="Network"/&gt;
 *     &lt;enumeration value="Change"/&gt;
 *     &lt;enumeration value="Error"/&gt;
 *     &lt;enumeration value="Other"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "AlertType")
@XmlEnum
public enum AlertType {

    @XmlEnumValue("Vulnerability")
    VULNERABILITY("Vulnerability"),
    IDS("IDS"),
    @XmlEnumValue("Malware")
    MALWARE("Malware"),
    @XmlEnumValue("Network")
    NETWORK("Network"),
    @XmlEnumValue("Change")
    CHANGE("Change"),
    @XmlEnumValue("Error")
    ERROR("Error"),
    @XmlEnumValue("Other")
    OTHER("Other");
    private final String value;

    AlertType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AlertType fromValue(String v) {
        for (AlertType c: AlertType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
