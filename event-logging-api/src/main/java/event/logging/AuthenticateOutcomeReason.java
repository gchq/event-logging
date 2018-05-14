

package event.logging;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AuthenticateOutcomeReason.
 * 
 * <p>The following schema fragment specifies the expected         content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="AuthenticateOutcomeReason"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="IncorrectUsernameOrPassword"/&gt;
 *     &lt;enumeration value="IncorrectUsername"/&gt;
 *     &lt;enumeration value="IncorrectPassword"/&gt;
 *     &lt;enumeration value="ExpiredCertificate"/&gt;
 *     &lt;enumeration value="RevokedCertificate"/&gt;
 *     &lt;enumeration value="IncorrectCA"/&gt;
 *     &lt;enumeration value="ExpiredCA"/&gt;
 *     &lt;enumeration value="AccountLocked"/&gt;
 *     &lt;enumeration value="AccountNotValidForLoginType"/&gt;
 *     &lt;enumeration value="Other"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "AuthenticateOutcomeReason")
@XmlEnum
public enum AuthenticateOutcomeReason {

    @XmlEnumValue("IncorrectUsernameOrPassword")
    INCORRECT_USERNAME_OR_PASSWORD("IncorrectUsernameOrPassword"),
    @XmlEnumValue("IncorrectUsername")
    INCORRECT_USERNAME("IncorrectUsername"),
    @XmlEnumValue("IncorrectPassword")
    INCORRECT_PASSWORD("IncorrectPassword"),
    @XmlEnumValue("ExpiredCertificate")
    EXPIRED_CERTIFICATE("ExpiredCertificate"),
    @XmlEnumValue("RevokedCertificate")
    REVOKED_CERTIFICATE("RevokedCertificate"),
    @XmlEnumValue("IncorrectCA")
    INCORRECT_CA("IncorrectCA"),
    @XmlEnumValue("ExpiredCA")
    EXPIRED_CA("ExpiredCA"),
    @XmlEnumValue("AccountLocked")
    ACCOUNT_LOCKED("AccountLocked"),
    @XmlEnumValue("AccountNotValidForLoginType")
    ACCOUNT_NOT_VALID_FOR_LOGIN_TYPE("AccountNotValidForLoginType"),
    @XmlEnumValue("Other")
    OTHER("Other");
    private final String value;

    AuthenticateOutcomeReason(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AuthenticateOutcomeReason fromValue(String v) {
        for (AuthenticateOutcomeReason c: AuthenticateOutcomeReason.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
