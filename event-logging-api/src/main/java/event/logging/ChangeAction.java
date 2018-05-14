

package event.logging;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ChangeAction.
 * 
 * <p>The following schema fragment specifies the expected         content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ChangeAction"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="StartScan"/&gt;
 *     &lt;enumeration value="StopScan"/&gt;
 *     &lt;enumeration value="FileAdded"/&gt;
 *     &lt;enumeration value="FileRemoved"/&gt;
 *     &lt;enumeration value="FileModified"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ChangeAction")
@XmlEnum
public enum ChangeAction {

    @XmlEnumValue("StartScan")
    START_SCAN("StartScan"),
    @XmlEnumValue("StopScan")
    STOP_SCAN("StopScan"),
    @XmlEnumValue("FileAdded")
    FILE_ADDED("FileAdded"),
    @XmlEnumValue("FileRemoved")
    FILE_REMOVED("FileRemoved"),
    @XmlEnumValue("FileModified")
    FILE_MODIFIED("FileModified");
    private final String value;

    ChangeAction(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ChangeAction fromValue(String v) {
        for (ChangeAction c: ChangeAction.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
