/*
 * Copyright 2016 Crown Copyright
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package event.logging.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Locale;

import event.logging.Device;
import org.apache.log4j.Logger;

public class DeviceUtil {
    private static final Logger LOGGER = Logger.getLogger(DeviceUtil.class);
    private static final String UNKNOWN_IP = "UNKNOWN";

    public static Device createDevice(final String hostName,
            final String ipAddress) {
        final Device device = new Device();
        device.setHostName(hostName);
        device.setIPAddress(ipAddress);
        return device;
    }

    /**
     * Creates a Device object from an InetAddress.
     * 
     * @param inetAddress
     *            The InetAddress to create the Device object from.
     * @return The newly created device object.
     */
    public static Device createDeviceFromInetAddress(
            final InetAddress inetAddress) {
        final Device device = new Device();
        try {
            final String ipAddress = getValidIP(inetAddress.getHostAddress());
            final String hostName = getFQDN(inetAddress);
            final String macAddress = getMACAddress(inetAddress);

            device.setIPAddress(ipAddress);
            device.setHostName(hostName);
            device.setMACAddress(macAddress);

        } catch (final RuntimeException e) {
            LOGGER.warn("Problem getting device from InetAddress", e);
        }
        return device;
    }

    /**
     * Manipulates a string to ensure IP addresses are consistent.
     * 
     * @param ip
     *            The IP string to check.
     * @return The IP address string in upper case or null if invalid.
     */
    public static String getValidIP(final String ip) {
        if (ip == null) {
            return null;
        }

        final String tmp = ip.trim().toUpperCase(Locale.ENGLISH);
        if (tmp.length() == 0 || tmp.contains(UNKNOWN_IP)) {
            return null;
        }

        return tmp;
    }

    /**
     * Gets a canonical host name from an InetAddress.
     * 
     * @param inetAddress
     *            The InetAddress to get the host name from.
     * @return The canonical host name.
     */
    private static String getFQDN(final InetAddress inetAddress) {
        try {
            final String fqdn = inetAddress.getCanonicalHostName();
            /*
             * If a security exception occurs then the getHostAddess method just
             * returns the IP address. We don't want the ip address so return
             * null in this case.
             */
            if (fqdn == null || fqdn.equals(inetAddress.getHostAddress())) {
                LOGGER.warn("Problem resolving FQDN");
                return null;
            }
            return fqdn;
        } catch (final RuntimeException e) {
            LOGGER.warn("Problem resolving FQDN", e);
        }

        return null;
    }

    /**
     * Determines a MAC address using the java.net classes.
     * 
     * @return String containing the MAC address.
     */
    private static String getMACAddress(final InetAddress inetAddress) {
        try {
            final NetworkInterface ni = NetworkInterface
                    .getByInetAddress(inetAddress);
            if (ni != null) {
                final byte[] mac = ni.getHardwareAddress();
                if (mac != null) {
                    final StringBuilder sb = new StringBuilder();
                    for (final byte field : mac) {
                        sb.append(String.format("%02X", field));
                        sb.append("-");
                    }
                    sb.setLength(sb.length() - 1);
                    return sb.toString();
                }
            }
        } catch (final RuntimeException e) {
            LOGGER.warn("Problem identifying MAC address", e);
        } catch (final SocketException e) {
            LOGGER.warn("Problem identifying MAC address", e);
        }

        return null;
    }
}
