

package event.logging;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PrintAction.
 * 
 * <p>The following schema fragment specifies the expected         content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="PrintAction"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="CreateJob"/&gt;
 *     &lt;enumeration value="CancelJob"/&gt;
 *     &lt;enumeration value="PauseJob"/&gt;
 *     &lt;enumeration value="ResumeJob"/&gt;
 *     &lt;enumeration value="StartPrint"/&gt;
 *     &lt;enumeration value="FinishPrint"/&gt;
 *     &lt;enumeration value="CancelPrint"/&gt;
 *     &lt;enumeration value="FailedPrint"/&gt;
 *     &lt;enumeration value="Other"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "PrintAction")
@XmlEnum
public enum PrintAction {

    @XmlEnumValue("CreateJob")
    CREATE_JOB("CreateJob"),
    @XmlEnumValue("CancelJob")
    CANCEL_JOB("CancelJob"),
    @XmlEnumValue("PauseJob")
    PAUSE_JOB("PauseJob"),
    @XmlEnumValue("ResumeJob")
    RESUME_JOB("ResumeJob"),
    @XmlEnumValue("StartPrint")
    START_PRINT("StartPrint"),
    @XmlEnumValue("FinishPrint")
    FINISH_PRINT("FinishPrint"),
    @XmlEnumValue("CancelPrint")
    CANCEL_PRINT("CancelPrint"),
    @XmlEnumValue("FailedPrint")
    FAILED_PRINT("FailedPrint"),
    @XmlEnumValue("Other")
    OTHER("Other");
    private final String value;

    PrintAction(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static PrintAction fromValue(String v) {
        for (PrintAction c: PrintAction.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
