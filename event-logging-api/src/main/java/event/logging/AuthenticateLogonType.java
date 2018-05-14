

package event.logging;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AuthenticateLogonType.
 * 
 * <p>The following schema fragment specifies the expected         content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="AuthenticateLogonType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Interactive"/&gt;
 *     &lt;enumeration value="Network"/&gt;
 *     &lt;enumeration value="Batch"/&gt;
 *     &lt;enumeration value="Service"/&gt;
 *     &lt;enumeration value="Unlock"/&gt;
 *     &lt;enumeration value="NetworkCleartext"/&gt;
 *     &lt;enumeration value="NewCredentials"/&gt;
 *     &lt;enumeration value="RemoteInteractive"/&gt;
 *     &lt;enumeration value="CachedInteractive"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "AuthenticateLogonType")
@XmlEnum
public enum AuthenticateLogonType {

    @XmlEnumValue("Interactive")
    INTERACTIVE("Interactive"),
    @XmlEnumValue("Network")
    NETWORK("Network"),
    @XmlEnumValue("Batch")
    BATCH("Batch"),
    @XmlEnumValue("Service")
    SERVICE("Service"),
    @XmlEnumValue("Unlock")
    UNLOCK("Unlock"),
    @XmlEnumValue("NetworkCleartext")
    NETWORK_CLEARTEXT("NetworkCleartext"),
    @XmlEnumValue("NewCredentials")
    NEW_CREDENTIALS("NewCredentials"),
    @XmlEnumValue("RemoteInteractive")
    REMOTE_INTERACTIVE("RemoteInteractive"),
    @XmlEnumValue("CachedInteractive")
    CACHED_INTERACTIVE("CachedInteractive");
    private final String value;

    AuthenticateLogonType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AuthenticateLogonType fromValue(String v) {
        for (AuthenticateLogonType c: AuthenticateLogonType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
