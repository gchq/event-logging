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
package event.logging.log4j;

import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;

/**
 * A layout for log4j that actually applies no layout, i.e. just writes the log
 * message to the log file without any formatting or additional detail.
 */
public class NoLayout extends Layout {
    private final StringBuilder sb = new StringBuilder(128);

    public void activateOptions() {
        // Does nothing.
    }

    /**
     * Formats and outputs a log event. In this layout we just output the
     * message followed by a new line.
     * 
     * @param event
     *            The <code>LoggingEvent</code> to format.
     * @return A formatted string to write to the log.
     * @see org.apache.log4j.Layout#format(org.apache.log4j.spi.LoggingEvent)
     */
    @Override
    public String format(final LoggingEvent event) {
        sb.setLength(0);
        sb.append(event.getRenderedMessage());
        sb.append(LINE_SEP);
        return sb.toString();
    }

    /**
     * This layout does not handle the throwable object contained within
     * {@link LoggingEvent} so return true.
     * 
     * @return True if this layout ignores the throwable object.
     * @see org.apache.log4j.Layout#ignoresThrowable()
     */
    @Override
    public boolean ignoresThrowable() {
        return true;
    }
}
