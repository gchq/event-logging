module uk.gov.gchq.eventlogging {

    exports event.logging;
    exports event.logging.impl;
    exports event.logging.jaxb;
    exports event.logging.jaxb.fluent;
    exports event.logging.util;

    requires transitive jakarta.xml.bind;
    requires java.xml;
    requires org.slf4j;

    opens event.logging to jakarta.xml.bind;
}
