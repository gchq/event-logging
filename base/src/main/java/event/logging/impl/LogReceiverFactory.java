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
package event.logging.impl;

import event.logging.log4j.Log4JLogReceiver;
import event.logging.log4j.Log4JLogReceiver;

import org.apache.log4j.Logger;

public final class LogReceiverFactory {
    private static final Logger LOGGER = Logger
            .getLogger(LogReceiverFactory.class);
    private static final String PROP_LOG_RECEIVER = "event.logging.logreceiver";
    private static final String DEFAULT_LOG_RECEIVER = "event.logging.log4j.Log4JLogReceiver";

    private static LogReceiverFactory instance;

    private volatile String lastClassName;
    private volatile LogReceiver logReceiver;

    private LogReceiverFactory() {
        // Hidden constructor.
    }

    public static synchronized LogReceiverFactory getInstance() {
        if (instance == null) {
            instance = new LogReceiverFactory();
        }
        return instance;
    }

    public synchronized LogReceiver getLogReceiver() {
        // See if a class name has been set to supply a log receiver.
        String className = System.getProperty(PROP_LOG_RECEIVER);

        // Trim the class name.
        if (className != null) {
            className = className.trim();
        }

        // If no class name has been suppled then use the default.
        if (className == null || className.length() == 0) {
            className = DEFAULT_LOG_RECEIVER;
        }

        // If the class name has changed then try and get the new log receiver.
        if (!className.equals(lastClassName)) {
            // If a class name has been set then see if we can get it.
            try {
                LOGGER.info("Using '" + className + "' to receive logs");
                final Class<?> clazz = getClass().getClassLoader().loadClass(
                        className);
                if (LogReceiver.class.isAssignableFrom(clazz)) {
                    try {
                        logReceiver = (LogReceiver) clazz.newInstance();
                    } catch (final IllegalAccessException e) {
                        LOGGER.error(e, e);
                    } catch (final InstantiationException e) {
                        LOGGER.error(e, e);
                    }

                } else {
                    LOGGER.error("Specified class '" + className
                            + "' is not a valid LogReceiver");
                }

            } catch (final ClassNotFoundException e) {
                LOGGER.error(e, e);
            }
        }

        if (logReceiver == null) {
            logReceiver = new Log4JLogReceiver();
        }

        lastClassName = className;
        return logReceiver;
    }
}
