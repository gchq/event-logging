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
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.time.Instant;

/**
 * Sub class the {@link DefaultEventLoggingService} to set up common values in log events.
 */
public class CustomEventLoggingService extends DefaultEventLoggingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomEventLoggingService.class);

    private static final String SYSTEM = "Example Application";
    private static final String GENERATOR = "CustomEventLoggingService";
    // These are only hard coded for this example application
    private static final String ENVIRONMENT = "DEV";
    private static final String VERSION = "1.2.3";

    private final UserContext userContext;

    private volatile boolean obtainedDevice = false;
    private volatile Device storedDevice;

    public CustomEventLoggingService(final UserContext userContext) {
        this.userContext = userContext;
    }

    // Override createEvent to provide an event populated with common values and the passed values.
    // This method is used by log(...) and loggedXXX(...) methods
    @Override
    public Event createEvent(final String typeId,
                             final String description,
                             final Purpose purpose,
                             final EventAction eventAction) {

        // The purpose/justification may be known only at the time of the event
        // or may be held in some kind of context object for the logged in user's session
        // or a mixture.
        final Purpose effectivePurpose = purpose != null
                ? purpose
                : getSessionPurpose();

        // Construct an event using the passed details and adding in
        // all common event details that are specific to this application.
        // This method can be used to add in details of things like:
        //  * The logged in user
        //  * Details of the client device, e.g. in a web app
        return Event.builder()
                .withEventTime(EventTime.builder()
                        .withTimeCreated(Instant.now())
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
                                .withName(userContext.getUserId() != null
                                        ? userContext.getUserId()
                                        : getOsUser())
                                .build())
                        .build())
                .withEventDetail(EventDetail.builder()
                        .withTypeId(typeId)
                        .withDescription(description)
                        .withPurpose(effectivePurpose)
                        .withEventAction(eventAction)
                        .build())
                .build();
    }

    private Purpose getSessionPurpose() {
        // This would need to be obtained from some kind of ThreadLocal based user context object
        return Purpose.builder()
                .withJustification(userContext.getJustification())
                .build();
    }

    private Device getDevice() {
        if (!obtainedDevice) {
            synchronized (this) {
                try {
                    final InetAddress localHost = InetAddress.getLocalHost();
                    final String hostname = localHost.getHostName();
                    final String hostAddress = localHost.getHostAddress();

                    String macAddress = null;
                    try {
                        // Just get the MAC of the first network interface for illustration purposes
                        final NetworkInterface ni = NetworkInterface.getNetworkInterfaces().nextElement();
                        byte[] hardwareAddress = ni.getHardwareAddress();
                        final String[] hexadecimal = new String[hardwareAddress.length];
                        for (int i = 0; i < hardwareAddress.length; i++) {
                            hexadecimal[i] = String.format("%02X", hardwareAddress[i]);
                        }
                        macAddress = String.join("-", hexadecimal);

                    } catch (SocketException e) {
                        LOGGER.warn("Unable to get MAC address due to", e);
                    }

                    storedDevice = Device.builder()
                            .withHostName(hostname)
                            .withIPAddress(hostAddress)
                            .withMACAddress(macAddress)
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
