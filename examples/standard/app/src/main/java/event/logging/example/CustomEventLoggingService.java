package event.logging.example;

import event.logging.Device;
import event.logging.Event;
import event.logging.EventAction;
import event.logging.EventDetail;
import event.logging.EventSource;
import event.logging.EventTime;
import event.logging.Purpose;
import event.logging.SystemDetail;
import event.logging.User;
import event.logging.impl.DefaultEventLoggingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

public class CustomEventLoggingService extends DefaultEventLoggingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomEventLoggingService.class);

    private static final String SYSTEM = "Example Application";
    private static final String ENVIRONMENT = "DEV";
    private static final String GENERATOR = "CustomEventLoggingService";
    private static final String VERSION = "1.2.3";

    private volatile boolean obtainedDevice = false;
    private volatile Device storedDevice;

    @Override
    public Event createEvent(final String typeId,
                             final String description,
                             final EventAction eventAction) {

        // Construct an event using the passed details and adding in
        // all common event details that are specific to this application.
        // This method can be used to add in details of things like:
        //  * The logged in user
        //  * Details of the client device, e.g. in a web app


        return Event.builder()
                .withEventTime(EventTime.builder()
                        .withTimeCreated(new Date())
                        .build())
                .withEventSource(EventSource.builder()
                        .withSystem(SystemDetail.builder()
                                .withName(SYSTEM)
                                .withEnvironment(ENVIRONMENT)
                                .withVersion(VERSION)
                                .build())
                        .withGenerator(GENERATOR)
                        .withDevice(getDevice())
                        .withUser(User.builder()
                                .withName(getOsUser())
                                .build())
                        .build())
                .withEventDetail(EventDetail.builder()
                        .withTypeId(typeId)
                        .withDescription(description)
                        .withPurpose(Purpose.builder()
                                .withJustification(getJustification())
                                .build())
                        .withEventAction(eventAction)
                        .build())
                .build();
    }

    private String getJustification() {
        // This would need to be obtained from some kind of Context object
    }

    private Device getDevice() {
        if (!obtainedDevice) {
            synchronized (this) {
                try {
                    final String hostname = InetAddress.getLocalHost().getHostName();
                    final String hostAddress = InetAddress.getLocalHost().getHostName();

                    storedDevice = Device.builder()
                            .withHostName(hostname)
                            .withIPAddress(hostAddress)
                            .build();
                } catch (UnknownHostException e) {
                    LOGGER.error("Unable to determine device address", e);
                    storedDevice = null;
                } finally {
                    obtainedDevice = true;
                }
            }
        }
        return storedDevice;
    }

    private String getOsUser() {
        return System.getProperty("user.name");
    }
}
