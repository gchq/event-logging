

package event.logging;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ProcessAction.
 * 
 * <p>The following schema fragment specifies the expected         content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ProcessAction"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Startup"/&gt;
 *     &lt;enumeration value="Shutdown"/&gt;
 *     &lt;enumeration value="Execute"/&gt;
 *     &lt;enumeration value="Terminate"/&gt;
 *     &lt;enumeration value="ChangeDir"/&gt;
 *     &lt;enumeration value="Call"/&gt;
 *     &lt;enumeration value="Access"/&gt;
 *     &lt;enumeration value="RequestObjectHandle"/&gt;
 *     &lt;enumeration value="Register"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ProcessAction")
@XmlEnum
public enum ProcessAction {

    @XmlEnumValue("Startup")
    STARTUP("Startup"),
    @XmlEnumValue("Shutdown")
    SHUTDOWN("Shutdown"),
    @XmlEnumValue("Execute")
    EXECUTE("Execute"),
    @XmlEnumValue("Terminate")
    TERMINATE("Terminate"),
    @XmlEnumValue("ChangeDir")
    CHANGE_DIR("ChangeDir"),
    @XmlEnumValue("Call")
    CALL("Call"),
    @XmlEnumValue("Access")
    ACCESS("Access"),
    @XmlEnumValue("RequestObjectHandle")
    REQUEST_OBJECT_HANDLE("RequestObjectHandle"),
    @XmlEnumValue("Register")
    REGISTER("Register");
    private final String value;

    ProcessAction(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ProcessAction fromValue(String v) {
        for (ProcessAction c: ProcessAction.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
