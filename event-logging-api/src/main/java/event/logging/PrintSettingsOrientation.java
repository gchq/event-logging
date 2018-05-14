

package event.logging;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PrintSettingsOrientation.
 * 
 * <p>The following schema fragment specifies the expected         content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="PrintSettingsOrientation"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Portrait"/&gt;
 *     &lt;enumeration value="Landscape"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "PrintSettingsOrientation")
@XmlEnum
public enum PrintSettingsOrientation {

    @XmlEnumValue("Portrait")
    PORTRAIT("Portrait"),
    @XmlEnumValue("Landscape")
    LANDSCAPE("Landscape");
    private final String value;

    PrintSettingsOrientation(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static PrintSettingsOrientation fromValue(String v) {
        for (PrintSettingsOrientation c: PrintSettingsOrientation.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
