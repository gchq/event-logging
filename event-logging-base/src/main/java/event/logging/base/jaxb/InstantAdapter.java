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
package event.logging.base.jaxb;

import event.logging.base.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.time.Instant;

public class InstantAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(InstantAdapter.class);

    public static Instant parseDate(final String string) {
        try {
            final Long millis = DateUtil.parseDateTimeString(string);
            return millis == null
                    ? null
                    : Instant.ofEpochMilli(millis);
        } catch (final ParseException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * @param date
     * @return The instant in the form "yyyy-MM-dd HH:mm:ss.SSS zzz"
     */
    public static String printDate(final Instant date) {
        if (date == null) {
            return null;
        }

        return DateUtil.createNormalDateTimeString(date.toEpochMilli());
    }
}
