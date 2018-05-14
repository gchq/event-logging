

package event.logging;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ThreatCategory.
 * 
 * <p>The following schema fragment specifies the expected         content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ThreatCategory"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Virus"/&gt;
 *     &lt;enumeration value="Worm"/&gt;
 *     &lt;enumeration value="HackingTool"/&gt;
 *     &lt;enumeration value="Spyware"/&gt;
 *     &lt;enumeration value="TrojanHorse"/&gt;
 *     &lt;enumeration value="Adware"/&gt;
 *     &lt;enumeration value="Other"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ThreatCategory")
@XmlEnum
public enum ThreatCategory {

    @XmlEnumValue("Virus")
    VIRUS("Virus"),
    @XmlEnumValue("Worm")
    WORM("Worm"),
    @XmlEnumValue("HackingTool")
    HACKING_TOOL("HackingTool"),
    @XmlEnumValue("Spyware")
    SPYWARE("Spyware"),
    @XmlEnumValue("TrojanHorse")
    TROJAN_HORSE("TrojanHorse"),
    @XmlEnumValue("Adware")
    ADWARE("Adware"),
    @XmlEnumValue("Other")
    OTHER("Other");
    private final String value;

    ThreatCategory(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ThreatCategory fromValue(String v) {
        for (ThreatCategory c: ThreatCategory.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
