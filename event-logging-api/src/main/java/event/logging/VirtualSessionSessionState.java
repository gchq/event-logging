

package event.logging;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for VirtualSessionSessionState.
 * 
 * <p>The following schema fragment specifies the expected         content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="VirtualSessionSessionState"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Parked"/&gt;
 *     &lt;enumeration value="Unparked"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "VirtualSessionSessionState")
@XmlEnum
public enum VirtualSessionSessionState {

    @XmlEnumValue("Parked")
    PARKED("Parked"),
    @XmlEnumValue("Unparked")
    UNPARKED("Unparked");
    private final String value;

    VirtualSessionSessionState(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static VirtualSessionSessionState fromValue(String v) {
        for (VirtualSessionSessionState c: VirtualSessionSessionState.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
