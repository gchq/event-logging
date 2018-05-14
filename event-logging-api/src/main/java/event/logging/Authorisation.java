

package event.logging;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Authorisation.
 * 
 * <p>The following schema fragment specifies the expected         content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Authorisation"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Request"/&gt;
 *     &lt;enumeration value="Modify"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "Authorisation")
@XmlEnum
public enum Authorisation {

    @XmlEnumValue("Request")
    REQUEST("Request"),
    @XmlEnumValue("Modify")
    MODIFY("Modify");
    private final String value;

    Authorisation(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static Authorisation fromValue(String v) {
        for (Authorisation c: Authorisation.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
