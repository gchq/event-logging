

package event.logging;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TermCondition.
 * 
 * <p>The following schema fragment specifies the expected         content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="TermCondition"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Exists"/&gt;
 *     &lt;enumeration value="NotExists"/&gt;
 *     &lt;enumeration value="Contains"/&gt;
 *     &lt;enumeration value="NotContains"/&gt;
 *     &lt;enumeration value="Empty"/&gt;
 *     &lt;enumeration value="NotEmpty"/&gt;
 *     &lt;enumeration value="Equals"/&gt;
 *     &lt;enumeration value="NotEquals"/&gt;
 *     &lt;enumeration value="GreaterThan"/&gt;
 *     &lt;enumeration value="GreaterThanEqualTo"/&gt;
 *     &lt;enumeration value="LessThan"/&gt;
 *     &lt;enumeration value="LessThanEqualTo"/&gt;
 *     &lt;enumeration value="StartsWith"/&gt;
 *     &lt;enumeration value="NotStartsWith"/&gt;
 *     &lt;enumeration value="Regex"/&gt;
 *     &lt;enumeration value="NotRegex"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "TermCondition")
@XmlEnum
public enum TermCondition {

    @XmlEnumValue("Exists")
    EXISTS("Exists"),
    @XmlEnumValue("NotExists")
    NOT_EXISTS("NotExists"),
    @XmlEnumValue("Contains")
    CONTAINS("Contains"),
    @XmlEnumValue("NotContains")
    NOT_CONTAINS("NotContains"),
    @XmlEnumValue("Empty")
    EMPTY("Empty"),
    @XmlEnumValue("NotEmpty")
    NOT_EMPTY("NotEmpty"),
    @XmlEnumValue("Equals")
    EQUALS("Equals"),
    @XmlEnumValue("NotEquals")
    NOT_EQUALS("NotEquals"),
    @XmlEnumValue("GreaterThan")
    GREATER_THAN("GreaterThan"),
    @XmlEnumValue("GreaterThanEqualTo")
    GREATER_THAN_EQUAL_TO("GreaterThanEqualTo"),
    @XmlEnumValue("LessThan")
    LESS_THAN("LessThan"),
    @XmlEnumValue("LessThanEqualTo")
    LESS_THAN_EQUAL_TO("LessThanEqualTo"),
    @XmlEnumValue("StartsWith")
    STARTS_WITH("StartsWith"),
    @XmlEnumValue("NotStartsWith")
    NOT_STARTS_WITH("NotStartsWith"),
    @XmlEnumValue("Regex")
    REGEX("Regex"),
    @XmlEnumValue("NotRegex")
    NOT_REGEX("NotRegex");
    private final String value;

    TermCondition(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TermCondition fromValue(String v) {
        for (TermCondition c: TermCondition.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
