

package event.logging;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AlertSeverity.
 * 
 * <p>The following schema fragment specifies the expected         content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="AlertSeverity"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Info"/&gt;
 *     &lt;enumeration value="Minor"/&gt;
 *     &lt;enumeration value="Major"/&gt;
 *     &lt;enumeration value="Critical"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "AlertSeverity")
@XmlEnum
public enum AlertSeverity {

    @XmlEnumValue("Info")
    INFO("Info"),
    @XmlEnumValue("Minor")
    MINOR("Minor"),
    @XmlEnumValue("Major")
    MAJOR("Major"),
    @XmlEnumValue("Critical")
    CRITICAL("Critical");
    private final String value;

    AlertSeverity(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AlertSeverity fromValue(String v) {
        for (AlertSeverity c: AlertSeverity.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
