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
package event.logging.base;

import event.logging.base.util.DateUtil;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests that the date utility works correctly.
 */
public class TestDateUtil {
    private static final int N3 = 3;

    /**
     * Tests that we can parse and construct 'normal' dates.
     */
    @Test
    public void testNormalFormatParse() throws Exception {
        final String in = "2010-04-03T13:54:23.078Z";
        final Long ms = DateUtil.parseDateTimeString(in);

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"),
                Locale.ROOT);
        cal.setTimeInMillis(ms);

        final SimpleDateFormat sdf = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss.SSS zzz");
        sdf.setCalendar(cal);
        final String out = sdf.format(new Date(ms));

        assertEquals("2010-04-03 13:54:23.078 UTC", out);

        final String fileString = DateUtil.createFileDateTimeString(ms);
        assertEquals("2010-04-03T13#54#23,078Z", fileString);

        final String normalString = DateUtil.createNormalDateTimeString(ms);
        assertEquals("2010-04-03T13:54:23.078Z", normalString);
    }

    /**
     * Tests that we can parse and construct 'file' dates.
     */
    @Test
    public void testFileFormatParse() throws Exception {
        final String in = "2010-04-03T13#54#23,078Z";
        final Long ms = DateUtil.parseDateTimeString(in);

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"),
                Locale.ROOT);
        cal.setTime(new Date(ms));

        final SimpleDateFormat sdf = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss.SSS zzz");
        sdf.setCalendar(cal);
        final String out = sdf.format(new Date(ms));

        assertEquals("2010-04-03 13:54:23.078 UTC", out);

        final String fileString = DateUtil.createFileDateTimeString(ms);
        assertEquals("2010-04-03T13#54#23,078Z", fileString);

        final String normalString = DateUtil.createNormalDateTimeString(ms);
        assertEquals("2010-04-03T13:54:23.078Z", normalString);
    }

    /**
     * Tests the <code>leftPad</code> method in <code>DateUtil</code>.
     */
    @Test
    public void testLeftPad() {
        assertEquals("1234", DateUtil.leftPad("1234", N3, '0'));
        assertEquals("123", DateUtil.leftPad("123", N3, '0'));
        assertEquals("012", DateUtil.leftPad("12", N3, '0'));
        assertEquals("001", DateUtil.leftPad("1", N3, '0'));
        assertEquals("000", DateUtil.leftPad("", N3, '0'));
        assertEquals("000", DateUtil.leftPad("0", N3, '0'));
        assertEquals("000", DateUtil.leftPad("00", N3, '0'));
        assertEquals("000", DateUtil.leftPad("000", N3, '0'));
        assertEquals("0000", DateUtil.leftPad("0000", N3, '0'));
    }
}
