

package event.logging;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for NetworkSrcDstTransportProtocol.
 * 
 * <p>The following schema fragment specifies the expected         content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="NetworkSrcDstTransportProtocol"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="TCP"/&gt;
 *     &lt;enumeration value="UDP"/&gt;
 *     &lt;enumeration value="ICMP"/&gt;
 *     &lt;enumeration value="IGMP"/&gt;
 *     &lt;enumeration value="Other"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "NetworkSrcDstTransportProtocol")
@XmlEnum
public enum NetworkSrcDstTransportProtocol {

    TCP("TCP"),
    UDP("UDP"),
    ICMP("ICMP"),
    IGMP("IGMP"),
    @XmlEnumValue("Other")
    OTHER("Other");
    private final String value;

    NetworkSrcDstTransportProtocol(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static NetworkSrcDstTransportProtocol fromValue(String v) {
        for (NetworkSrcDstTransportProtocol c: NetworkSrcDstTransportProtocol.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
