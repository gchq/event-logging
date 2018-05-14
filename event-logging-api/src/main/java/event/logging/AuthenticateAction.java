

package event.logging;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AuthenticateAction.
 * 
 * <p>The following schema fragment specifies the expected         content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="AuthenticateAction"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Logon"/&gt;
 *     &lt;enumeration value="Logoff"/&gt;
 *     &lt;enumeration value="ChangePassword"/&gt;
 *     &lt;enumeration value="ResetPassword"/&gt;
 *     &lt;enumeration value="ScreenLock"/&gt;
 *     &lt;enumeration value="ScreenUnlock"/&gt;
 *     &lt;enumeration value="AccountLock"/&gt;
 *     &lt;enumeration value="AccountUnlock"/&gt;
 *     &lt;enumeration value="Reconnect"/&gt;
 *     &lt;enumeration value="Disconnect"/&gt;
 *     &lt;enumeration value="Connect"/&gt;
 *     &lt;enumeration value="ElevatePrivilege"/&gt;
 *     &lt;enumeration value="Other"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "AuthenticateAction")
@XmlEnum
public enum AuthenticateAction {

    @XmlEnumValue("Logon")
    LOGON("Logon"),
    @XmlEnumValue("Logoff")
    LOGOFF("Logoff"),
    @XmlEnumValue("ChangePassword")
    CHANGE_PASSWORD("ChangePassword"),
    @XmlEnumValue("ResetPassword")
    RESET_PASSWORD("ResetPassword"),
    @XmlEnumValue("ScreenLock")
    SCREEN_LOCK("ScreenLock"),
    @XmlEnumValue("ScreenUnlock")
    SCREEN_UNLOCK("ScreenUnlock"),
    @XmlEnumValue("AccountLock")
    ACCOUNT_LOCK("AccountLock"),
    @XmlEnumValue("AccountUnlock")
    ACCOUNT_UNLOCK("AccountUnlock"),
    @XmlEnumValue("Reconnect")
    RECONNECT("Reconnect"),
    @XmlEnumValue("Disconnect")
    DISCONNECT("Disconnect"),
    @XmlEnumValue("Connect")
    CONNECT("Connect"),
    @XmlEnumValue("ElevatePrivilege")
    ELEVATE_PRIVILEGE("ElevatePrivilege"),
    @XmlEnumValue("Other")
    OTHER("Other");
    private final String value;

    AuthenticateAction(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AuthenticateAction fromValue(String v) {
        for (AuthenticateAction c: AuthenticateAction.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
