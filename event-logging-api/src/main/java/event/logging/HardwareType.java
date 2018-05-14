

package event.logging;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for HardwareType.
 * 
 * <p>The following schema fragment specifies the expected         content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="HardwareType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="OpticalDrive"/&gt;
 *     &lt;enumeration value="HardDisk"/&gt;
 *     &lt;enumeration value="USBMassStorage"/&gt;
 *     &lt;enumeration value="Printer"/&gt;
 *     &lt;enumeration value="Modem"/&gt;
 *     &lt;enumeration value="Other"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "HardwareType")
@XmlEnum
public enum HardwareType {

    @XmlEnumValue("OpticalDrive")
    OPTICAL_DRIVE("OpticalDrive"),
    @XmlEnumValue("HardDisk")
    HARD_DISK("HardDisk"),
    @XmlEnumValue("USBMassStorage")
    USB_MASS_STORAGE("USBMassStorage"),
    @XmlEnumValue("Printer")
    PRINTER("Printer"),
    @XmlEnumValue("Modem")
    MODEM("Modem"),
    @XmlEnumValue("Other")
    OTHER("Other");
    private final String value;

    HardwareType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static HardwareType fromValue(String v) {
        for (HardwareType c: HardwareType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
