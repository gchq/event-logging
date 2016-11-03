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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Utility Class for standard date manipulation.
 */
public final class DateUtil {
    private static final TimeZone UTC_TIME_ZONE = TimeZone.getTimeZone("+0000");
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm:ss";
    private static final String MILLISECOND_SEPERATOR = ".";
    private static final String TIME_SEPERATOR = "T";
    private static final String TIME_ZONE = "Z";

    private static final String FILE_TIME_FORMAT = "HH#mm#ss";
    private static final String FILE_MILLISECOND_SEPERATOR = ",";

    private static final int MS_IN_SEC = 1000;
    private static final int THREE = 3;

    private static final String NULL = "NULL";

    private DateUtil() {
        // Private constructor.
    }

    /**
     * Create a 'file' format date string.
     * 
     * @param date
     *            The date to create the string for.
     * @return string The date as a 'file' format date string.
     */
    public static String createFileDateTimeString(final Long ms) {
        if (ms == null) {
            return NULL;
        }

        final SimpleDateFormat sdf = new SimpleDateFormat(FILE_TIME_FORMAT);
        sdf.setTimeZone(UTC_TIME_ZONE);
        sdf.getCalendar().setTimeZone(UTC_TIME_ZONE);
        return createDateTimeString(ms, sdf, FILE_MILLISECOND_SEPERATOR);
    }

    /**
     * Create a 'normal' format date string.
     * 
     * @param ms
     *            The date to create the string for.
     * @return string The date as a 'normal' format date string.
     */
    public static String createNormalDateTimeString(final Long ms) {
        if (ms == null) {
            return NULL;
        }

        final SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT);
        sdf.setTimeZone(UTC_TIME_ZONE);
        sdf.getCalendar().setTimeZone(UTC_TIME_ZONE);
        return createDateTimeString(ms, sdf, MILLISECOND_SEPERATOR);
    }

    /**
     * Creates a string from a date given the appropriate time format and millisecond separator.
     * 
     * @param ms
     *            The date to use.
     * @param timeFormat
     *            The time format to use for the time part.
     * @param milliSep
     *            The separator to place before the milliseconds.
     * @return A string representation of the supplied date.
     */
    private static String createDateTimeString(final long ms, final DateFormat timeFormat, final String milliSep) {
        final long seconds = ms / MS_IN_SEC;
        final long fraction = ms - seconds * MS_IN_SEC;

        String millis = Long.toString(fraction);
        millis = leftPad(millis, THREE, '0');

        final SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        sdf.setTimeZone(UTC_TIME_ZONE);

        final Date date = new Date(ms);
        final StringBuilder builder = new StringBuilder(128);
        builder.append(sdf.format(date));
        builder.append(TIME_SEPERATOR);
        builder.append(timeFormat.format(date));
        builder.append(milliSep);
        builder.append(millis);
        builder.append(TIME_ZONE);

        return builder.toString();
    }

    /**
     * @param date
     *            The Z time string date to parse in 'normal' of 'file' formats.
     * @return A Date object set to the supplied date.
     */
    public static Long parseDateTimeString(final String date) throws ParseException {
        if (NULL.equals(date) || date == null || date.length() == 0) {
            return null;
        }

        String tmp = date.replace('T', ' ');
        tmp = tmp.replaceAll("#", ":");
        tmp = tmp.replaceAll(FILE_MILLISECOND_SEPERATOR, ".");
        tmp = tmp.replace("Z", " +0000");

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS zzz");
        final Calendar cal = Calendar.getInstance(UTC_TIME_ZONE, Locale.ROOT);
        sdf.setCalendar(cal);
        sdf.parse(tmp);

        return cal.getTime().getTime();
    }

    /**
     * Utility method for padding a string to a certain number of characters. Characters are appended to the left hand
     * side of the string to make it the specified length. IF the string is already longer than the specified length is
     * is just returned and not truncated.
     * 
     * @param str
     *            The string to pad.
     * @param size
     *            The required padded size of the string.
     * @param padChar
     *            The character to pad the string with.
     * @return The padded string.
     */
    public static String leftPad(final String str, final int size, final char padChar) {
        final int len = str.length();
        if (len >= size) {
            return str;
        }

        final char[] padding = new char[size];
        Arrays.fill(padding, 0, size - len, padChar);
        str.getChars(0, len, padding, size - len);
        return new String(padding).toString();
    }
}
