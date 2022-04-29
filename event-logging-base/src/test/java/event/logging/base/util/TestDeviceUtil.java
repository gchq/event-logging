package event.logging.base.util;

import event.logging.Device;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

class TestDeviceUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestDeviceUtil.class);

    @ParameterizedTest
    @CsvSource(value = {
            "'[0:0:0:0:0:0:0:1]','0:0:0:0:0:0:0:1'",
            "'[]',''",
            "'NULL','NULL'",
            "'',''",
    })
    void stripIpv6Brackets(final String input, final String expectedOutput) {
        final String cleanedIp = DeviceUtil.stripIpv6Brackets(input);

        Assertions.assertThat(cleanedIp)
                .isEqualTo(expectedOutput);

        // Now clean the cleaned thing to make sure it is unchanged.
        final String cleanedIp2 = DeviceUtil.stripIpv6Brackets(cleanedIp);

        Assertions.assertThat(cleanedIp2)
                .isEqualTo(cleanedIp);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "'192.168.0.1','192.168.0.1'",
            "' 192.168.0.1','192.168.0.1'",
            "'192.168.0.1 ','192.168.0.1'",
            "' 192.168.0.1 ','192.168.0.1'",
            "'0:0:0:0:0:0:0:1','0:0:0:0:0:0:0:1'",
            "'[0:0:0:0:0:0:0:1]','0:0:0:0:0:0:0:1'",
            "' [0:0:0:0:0:0:0:1] ','0:0:0:0:0:0:0:1'",
            "'NULL','NULL'",
            "'UNKNOWN','NULL'",
            "'','NULL'",
    }, nullValues = {"NULL"})
    void getValidIP(final String input, final String expectedOutput) {

        final String actual = DeviceUtil.getValidIP(input);
        LOGGER.debug("input [{}], expectedOutput [{}], actual [{}]", input, expectedOutput, actual);
        Assertions.assertThat(actual)
                .isEqualTo(expectedOutput);
    }

    @Test
    void createDeviceFromInetAddress_localhost() throws UnknownHostException {
        final InetAddress inetAddress = InetAddress.getLocalHost();
        final Device device = DeviceUtil.createDeviceFromInetAddress(inetAddress);
        logDeviceAddressDetails(device);
        Assertions.assertThat(device.getIPAddress())
                .matches("^(10\\.|127\\.|0{1,3}:).*");

        Assertions.assertThat(device.getHostName())
                .isNotNull();
    }

    @Test
    void createDeviceFromInetAddress_fromHost() throws UnknownHostException {
        final InetAddress inetAddress = InetAddress.getByName("10.0.0.9");
        final Device device = DeviceUtil.createDeviceFromInetAddress(inetAddress);
        logDeviceAddressDetails(device);

        Assertions.assertThat(device.getIPAddress())
                .isEqualTo("10.0.0.9");
        // Can't resolve a hostname for this ip so host name will be null
        Assertions.assertThat(device.getHostName())
                .isNull();
    }

    @Test
    void createDeviceFromInetAddress_ipv6() throws UnknownHostException {
        final InetAddress inetAddress = InetAddress.getByName("[0:0:0:0:0:0:0:1]");
        final Device device = DeviceUtil.createDeviceFromInetAddress(inetAddress);
        logDeviceAddressDetails(device);

        Assertions.assertThat(device.getIPAddress())
                .isEqualTo("0:0:0:0:0:0:0:1");
        // Will be localhost's hostname
        Assertions.assertThat(device.getHostName())
                .isNotNull();
    }

    @Test
    void createDeviceFromInetAddress_ipv6_2() throws UnknownHostException {
        final InetAddress inetAddress = InetAddress.getByName("0:0:0:0:0:0:0:1");
        final Device device = DeviceUtil.createDeviceFromInetAddress(inetAddress);
        logDeviceAddressDetails(device);

        Assertions.assertThat(device.getIPAddress())
                .isEqualTo("0:0:0:0:0:0:0:1");
        // Will be localhost's hostname
        Assertions.assertThat(device.getHostName())
                .isNotNull();
    }

    private void logDeviceAddressDetails(final Device device) {
        LOGGER.debug("Device: (ip: [{}], host: [{}], mac: [{}])",
                device.getIPAddress(),
                device.getHostName(),
                device.getMACAddress());
    }

    @Test
    void createDevice() {
        final String hostname = "host.domain";
        final String ip = "192.168.0.1";
        final Device device = DeviceUtil.createDevice(hostname, ip);
        logDeviceAddressDetails(device);

        Assertions.assertThat(device.getHostName())
                .isEqualTo(hostname);
        Assertions.assertThat(device.getIPAddress())
                .isEqualTo(ip);
    }
}
