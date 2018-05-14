

package event.logging;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ProcessType.
 * 
 * <p>The following schema fragment specifies the expected         content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ProcessType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="OS"/&gt;
 *     &lt;enumeration value="Service"/&gt;
 *     &lt;enumeration value="Application"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ProcessType")
@XmlEnum
public enum ProcessType {

    OS("OS"),
    @XmlEnumValue("Service")
    SERVICE("Service"),
    @XmlEnumValue("Application")
    APPLICATION("Application");
    private final String value;

    ProcessType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ProcessType fromValue(String v) {
        for (ProcessType c: ProcessType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
