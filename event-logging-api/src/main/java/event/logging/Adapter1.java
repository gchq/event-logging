

package event.logging;

import java.util.Date;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class Adapter1
    extends XmlAdapter<String, Date>
{


    public Date unmarshal(String value) {
        return (event.logging.jaxb.DateAdaptor.parseDate(value));
    }

    public String marshal(Date value) {
        return (event.logging.jaxb.DateAdaptor.printDate(value));
    }

}
