/*
 * Copyright 2018 Crown Copyright
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
package event.logging.base.impl;

import event.logging.base.EventLoggingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

/**
 * <p>If you want the serialised XML events to be consumed by something other than SLF4J then create
 * your own implementation of {@link LogReceiver} and set the Java system property
 * <code>event.logging.logreceiver</code> to the class name of your class.</p>
 */
public final class LogReceiverFactory {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(LogReceiverFactory.class);
    private static final String DEFAULT_LOG_RECEIVER = "event.logging.impl.LoggerLogReceiver";

    private static LogReceiverFactory instance;

    private volatile String lastClassName;
    private volatile event.logging.base.impl.LogReceiver logReceiver;

    private LogReceiverFactory() {
        // Hidden constructor.
    }

    public static synchronized LogReceiverFactory getInstance() {
        if (instance == null) {
            instance = new LogReceiverFactory();
        }
        return instance;
    }

    public synchronized event.logging.base.impl.LogReceiver getLogReceiver() {
        // See if a class name has been set to supply a log receiver.
        String className = System.getProperty(EventLoggingService.PROP_KEY_LOG_RECEIVER_CLASS);

        // Trim the class name.
        if (className != null) {
            className = className.trim();
        }

        // If no class name has been supplied then use the default.
        if (className == null || className.isEmpty()) {
            className = DEFAULT_LOG_RECEIVER;
        }

        // If the class name has changed then try and get the new log receiver.
        if (!className.equals(lastClassName)) {
            // If a class name has been set then see if we can get it.
            try {
                LOGGER.info("Using '" + className + "' to receive logs");
                final Class<?> clazz = getClass().getClassLoader().loadClass(
                        className);
                if (event.logging.base.impl.LogReceiver.class.isAssignableFrom(clazz)) {
                    try {
                        logReceiver = (LogReceiver) clazz.getDeclaredConstructor(new Class<?>[0]).newInstance();
                    } catch (final InstantiationException | IllegalAccessException
                                   | InvocationTargetException | NoSuchMethodException e) {
                        LOGGER.error(e.getMessage(), e);
                    }

                } else {
                    LOGGER.error("Specified class '" + className
                            + "' is not a valid LogReceiver");
                }

            } catch (final ClassNotFoundException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }

        if (logReceiver == null) {
            logReceiver = new LoggerLogReceiver();
        }

        lastClassName = className;
        return logReceiver;
    }
}
