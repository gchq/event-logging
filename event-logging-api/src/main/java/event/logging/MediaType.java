

package event.logging;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MediaType.
 * 
 * <p>The following schema fragment specifies the expected         content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="MediaType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="HardDisk"/&gt;
 *     &lt;enumeration value="USBMassStorage"/&gt;
 *     &lt;enumeration value="BluRay"/&gt;
 *     &lt;enumeration value="DVD"/&gt;
 *     &lt;enumeration value="CD"/&gt;
 *     &lt;enumeration value="FloppyDisk"/&gt;
 *     &lt;enumeration value="Tape"/&gt;
 *     &lt;enumeration value="Other"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "MediaType")
@XmlEnum
public enum MediaType {

    @XmlEnumValue("HardDisk")
    HARD_DISK("HardDisk"),
    @XmlEnumValue("USBMassStorage")
    USB_MASS_STORAGE("USBMassStorage"),
    @XmlEnumValue("BluRay")
    BLU_RAY("BluRay"),
    DVD("DVD"),
    CD("CD"),
    @XmlEnumValue("FloppyDisk")
    FLOPPY_DISK("FloppyDisk"),
    @XmlEnumValue("Tape")
    TAPE("Tape"),
    @XmlEnumValue("Other")
    OTHER("Other");
    private final String value;

    MediaType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static MediaType fromValue(String v) {
        for (MediaType c: MediaType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
