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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default LogReceiver that uses the supplied serializer to create a string and
 * writes it to a log via Log4J.
 */
public class LoggerLogReceiver implements LogReceiver {
    private static final Logger LOGGER = LoggerFactory.getLogger("event-logger");

    /**
     * Logs the supplied event.
     *
     * @param data The event to log.
     */
    @Override
    public void log(final String data) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(data);
        }
    }
}
