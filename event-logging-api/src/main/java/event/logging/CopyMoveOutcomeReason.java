

package event.logging;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CopyMoveOutcomeReason.
 * 
 * <p>The following schema fragment specifies the expected         content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CopyMoveOutcomeReason"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="DeviceFull"/&gt;
 *     &lt;enumeration value="IOError"/&gt;
 *     &lt;enumeration value="InvalidPath"/&gt;
 *     &lt;enumeration value="PermissionDenied"/&gt;
 *     &lt;enumeration value="ReadOnly"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "CopyMoveOutcomeReason")
@XmlEnum
public enum CopyMoveOutcomeReason {

    @XmlEnumValue("DeviceFull")
    DEVICE_FULL("DeviceFull"),
    @XmlEnumValue("IOError")
    IO_ERROR("IOError"),
    @XmlEnumValue("InvalidPath")
    INVALID_PATH("InvalidPath"),
    @XmlEnumValue("PermissionDenied")
    PERMISSION_DENIED("PermissionDenied"),
    @XmlEnumValue("ReadOnly")
    READ_ONLY("ReadOnly");
    private final String value;

    CopyMoveOutcomeReason(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CopyMoveOutcomeReason fromValue(String v) {
        for (CopyMoveOutcomeReason c: CopyMoveOutcomeReason.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
