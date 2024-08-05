package event.logging.base.impl.actions;

import event.logging.Device;
import event.logging.Event;
import event.logging.EventAction;
import event.logging.EventDetail;
import event.logging.EventSource;
import event.logging.EventTime;
import event.logging.SystemDetail;
import event.logging.User;
import event.logging.base.impl.XMLWriter;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public abstract class AbstractTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTest.class);

    static Event createEvent(final EventAction eventAction) {
        return Event.builder()
                .withEventTime(EventTime.builder()
                        .withTimeCreated(LocalDateTime.of(
                                        2024, 6, 18,
                                        16, 13, 59)
                                .toInstant(ZoneOffset.UTC))
                        .build())
                .withEventSource(EventSource.builder()
                        .withSystem(SystemDetail.builder()
                                .withName("Test System")
                                .withEnvironment("Test")
                                .build())
                        .withGenerator("JUnit")
                        .withDevice(Device.builder()
                                .withIPAddress("123.123.123.123")
                                .build())
                        .withUser(User.builder()
                                .withId("someuser")
                                .build())
                        .build())
                .withEventDetail(EventDetail.builder()
                        .withTypeId(eventAction.getClass().getSimpleName())
                        .withDescription("Doing a " + eventAction.getClass().getSimpleName())
                        .withEventAction(eventAction)
                        .build())
                .build();
    }

    static String getXML(final Object object) {
        try {
            final StringWriter writer = new StringWriter();
            final event.logging.base.impl.XMLWriter xmlWriter = new XMLWriter(writer);
            getMarshaller().marshal(object, xmlWriter);
            return writer.toString();

        } catch (final Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static Marshaller getMarshaller() {
        try {
            final Marshaller marshaller = getContext().createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

            return marshaller;
        } catch (final Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static JAXBContext getContext() {
        // Initialize-on-Demand, Holder Class Idiom
        return Holder.JAXB_CONTEXT;
    }

    private static JAXBContext createContext() {
        try {
            LOGGER.info("Creating JAXB context");
            return JAXBContext.newInstance(Event.class);
        } catch (final Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    // --------------------------------------------------------------------------------


    // Initialize-on-Demand, Holder Class Idiom
    private static class Holder {
        private static final JAXBContext JAXB_CONTEXT = createContext();
    }
}
