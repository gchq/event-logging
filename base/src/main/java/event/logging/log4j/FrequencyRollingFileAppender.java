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

import event.logging.util.DateUtil;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.FileAppender;
import org.apache.log4j.helpers.CountingQuietWriter;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;

/**
 * This class is an appender for log4j that should be used for generating
 * event logs. Unlike log4j's <code>DailyRollingFileAppended</code> this
 * class allows for logs to be rolled over every time a specified number of
 * minutes has elapsed (default is 10). Logs will not roll over as soon as the
 * period elapses but only when a new event is logged after the period has
 * elapsed.
 * <p>
 * In addition to rolling over by elapsed period, this appender will roll over
 * when a log reaches a maximum file size set by <code>MaxFileSize</code>. The
 * default maximum file size if 10Mb. Once reached the log will be rolled even
 * if the time period has not been reached.
 * </p>
 * <p>
 * The stem of the file name must be set in the log4j configuration so that all
 * logs of the same type can be identified, e.g. setting <code>Stem</code> to
 * "events" will produce log files in the form
 * "events_2010-01-01T00-00-00-000Z_2010-01-01T00-00-00-000Z.log".
 * </p>
 * <p>
 * The directory in which to read and write log files must also be set. The
 * directory is read when the application starts. This class will tray and find
 * the previous temporary log (one without and end period and ".log" extension),
 * and either roll the previous log or append to it. the directory is set by
 * configuring the <code>Dir</code> property.
 * </p>
 */
public class FrequencyRollingFileAppender extends FileAppender {
    /** Error message for failed date parsing of previous log file. */
    private static final String UNABLE_TO_PARSE_DATE = "Unable to parse date for previous log.";
    /** Error message when an invalid directory is set. */
    private static final String INVALID_DIR = "Dir is not valid directory location for appender: ";
    /** Error message when we are unable to create the log directory. */
    private static final String UNABLE_TO_CREATE = "Unable to create directory: ";
    /** Error message when an invalid stem is set. */
    private static final String INVALID_STEM = "Stem or Dir is not set for appender: ";
    /**
     * All time strings end with 'Z' so all temporary log files will end in 'Z'.
     */
    private static final String Z = "Z";
    /** Rolled logs will be given the extension ".log". */
    private static final String LOG_EXTENSION = ".log";
    /**
     * The stem, period start and period end used to make up a log file name are
     * separated by an underscore.
     */
    private static final String SEPARATOR = "_";
    /** Number of bytes in a Megabyte. */
    private static final long MB = 1000000;
    /** The default time interval between rolled log files is 10 minutes. */
    private static final int DEFAULT_INTERVAL = 10;
    /** The default maximum file size is 10MB. */
    private static final long DEFAULT_MAX_FILE_SIZE = 10 * MB;
    /** Use the UTC time zone - 'Z'. */
    private static final TimeZone UTC_TIME_ZONE = TimeZone.getTimeZone("UTC");

    /** A <code>StringBuilder</code> used to build file names. */
    private final StringBuilder sb = new StringBuilder(128);

    /** Milliseconds representing now. */
    private long now = System.currentTimeMillis();
    /** The period start milliseconds to use. */
    private long periodStartMs = System.currentTimeMillis();

    /**
     * The next time we estimate a rollover should occur.
     */
    private long nextCheck;

    /**
     * The rolling calendar instance to use when determining the next time we
     * should roll over.
     */
    private final RollingCalendar rc = new RollingCalendar(UTC_TIME_ZONE,
            Locale.UK);

    /** The directory to read/write log files to. */
    private String dir;
    /** The stem to add to the beginning of all log files. */
    private String stem;
    /** Stem + separator. */
    private String startString;
    /** The number of minutes to wait between roll over. */
    private int intervalMinutes = DEFAULT_INTERVAL;
    /** The maximum size of each log file. */
    private long maxFileSize = DEFAULT_MAX_FILE_SIZE;
    /** Whether to use Windows friendly file names. */
    private boolean windowsFriendly;

    /** The directory to read/write log files to. */
    private File directory;

    private final ReentrantLock lock = new ReentrantLock();

    /**
     * Activates the appender once the configuration for log4j has been read.
     *
     * @see org.apache.log4j.FileAppender#activateOptions()
     */
    @Override
    public void activateOptions() {
        if (stem != null && dir != null) {
            if (getEncoding() == null) {
                setEncoding("UTF-8");
            }

            directory = new File(dir);

            if (!directory.isDirectory()) {
                // Make the directory if it doesn't exist.
                if (!directory.mkdirs()) {
                    LogLog.error(UNABLE_TO_CREATE + dir);
                }
            }

            // Make sure the directory exists.
            if (directory.isDirectory()) {
                now = System.currentTimeMillis();
                rc.setIntervalMinutes(intervalMinutes);

                // Try to find a previously unrolled file to get the period
                // start from.
                final File[] files = directory.listFiles();
                File tmpFile = null;

                // Create the string that all files will start with.
                startString = stem + SEPARATOR;

                for (int i = 0; i < files.length && tmpFile == null; i++) {
                    final File file = files[i];
                    if (file.isFile() && file.getName().startsWith(startString)
                            && file.getName().endsWith(Z)) {
                        tmpFile = file;
                    }
                }

                // Set the period start date to now for the moment.
                periodStartMs = now;

                // If we found the previous tmp file then parse the period start
                // date out of it.
                if (tmpFile != null) {
                    String name = tmpFile.getName();
                    name = name.substring(startString.length());

                    try {
                        // Hopefully what we have left is a date.
                        final Long ms = DateUtil.parseDateTimeString(name);
                        periodStartMs = ms;
                    } catch (final ParseException ex) {
                        LogLog.error(UNABLE_TO_PARSE_DATE);
                    }
                }

                // The next check time should be the interval added to the
                // period start date.
                nextCheck = rc.getNextCheckMillis(periodStartMs);

                // Create the file name using the period start date.
                setTmpFile();

                // Make sure there is a layout.
                if (getLayout() == null) {
                    setLayout(new NoLayout());
                }
            } else {
                LogLog.error(INVALID_DIR + name);
            }

        } else {
            LogLog.error(INVALID_STEM + name);
        }

        super.activateOptions();
    }

    /**
     * Sets the current temporary file to write all new log events to.
     */
    private void setTmpFile() {
        try {
            sb.setLength(0);
            sb.append(startString);

            if (windowsFriendly) {
                sb.append(DateUtil.createFileDateTimeString(periodStartMs));
            } else {
                sb.append(DateUtil.createNormalDateTimeString(periodStartMs));
            }

            final File file = new File(directory, sb.toString());
            setFile(file.getAbsolutePath(), true, false, bufferSize);
        } catch (final IOException ex) {
            LogLog.error(ex.getMessage(), ex);
        }
    }

    /**
     * Rollover the current file to a new file.
     */
    public void rollOver() {
        try {
            // Lock.
            lock.lock();

            String periodEndString = null;

            if (windowsFriendly) {
                periodEndString = DateUtil.createFileDateTimeString(now);
            } else {
                periodEndString = DateUtil.createNormalDateTimeString(now);
            }

            sb.setLength(0);
            sb.append(getFile());
            sb.append(SEPARATOR);
            sb.append(periodEndString);
            sb.append(LOG_EXTENSION);
            final String rolledFileName = sb.toString();

            // Close current file, and rename it to datedFilename
            this.closeFile();

            final File target = new File(rolledFileName);
            if (target.exists()) {
                final boolean success = target.delete();
                if (!success) {
                    LogLog.error("Unable to delete file: " + rolledFileName);
                }
            }

            final File source = new File(getFile());
            boolean result = source.renameTo(target);
            if (result) {
                LogLog.debug(getFile() + " -> " + rolledFileName + " count="
                        + getCountingQuietWriter().getCount());
            } else {
                LogLog.error("Failed to rename [" + getFile() + "] to ["
                        + rolledFileName + "].");
            }

            periodStartMs = now;

            // This will also close the file. This is OK since multiple
            // close operations are safe.
            setTmpFile();

        } finally {
            // Unlock.
            lock.unlock();
        }
    }

    private CountingQuietWriter getCountingQuietWriter() {
        if (qw instanceof CountingQuietWriter) {
            return (CountingQuietWriter) qw;
        }

        throw new IllegalStateException("CountingQuietWriter was expected");
    }

    /**
     * Before actually logging, this method will check whether it is time to do
     * a rollover. If it is, it will schedule the next rollover time and then
     * rollover.
     *
     * @param event
     *            The log event to add to the current log.
     *
     * @see org.apache.log4j.WriterAppender#subAppend(org.apache.log4j.spi.LoggingEvent)
     */
    @Override
    protected void subAppend(final LoggingEvent event) {
        try {
            // Lock.
            lock.lock();

            long n = System.currentTimeMillis();
            if (n >= nextCheck
                    || getCountingQuietWriter().getCount() >= maxFileSize) {
                now = n;
                nextCheck = rc.getNextCheckMillis(now);
                rollOver();
            }
            super.subAppend(event);

        } finally {
            // Unlock.
            lock.unlock();
        }
    }

    /**
     * @param fileName
     *            The path to the log file.
     * @param append
     *            If true will append to fileName. Otherwise will truncate
     *            fileName.
     * @param bufferedIO
     *            Whether to buffer or not.
     * @param bufferSize
     *            The buffer size.
     * @throws IOException
     *             Thrown if file location does not exist.
     * @see org.apache.log4j.FileAppender#setFile(java.lang.String, boolean,
     *      boolean, int)
     */
    @Override
    public synchronized void setFile(final String fileName,
                                     final boolean append, final boolean bufferedIO, final int bufferSize)
            throws IOException {
        super.setFile(fileName, append, this.bufferedIO, this.bufferSize);
        if (append) {
            File f = new File(fileName);
            getCountingQuietWriter().setCount(f.length());
        }
    }

    @Override
    protected void setQWForFiles(final Writer writer) {
        this.qw = new CountingQuietWriter(writer, errorHandler);
    }

    /**
     * @return the directory to read/write log files from.
     */
    public String getDir() {
        return dir;
    }

    /**
     * @param dir
     *            the directory to read/write log files from.
     */
    public void setDir(final String dir) {
        this.dir = dir;
    }

    /**
     * @return the stem to be used for all log files.
     */
    public String getStem() {
        return stem;
    }

    /**
     * @param stem
     *            the stem to set for all log files.
     */
    public void setStem(final String stem) {
        this.stem = stem;
    }

    /**
     * @return the number of minutes to wait before rolling each log file.
     */
    public int getIntervalMinutes() {
        return intervalMinutes;
    }

    /**
     * @param intervalMinutes
     *            the the number of minutes to wait before rolling each log
     *            file.
     */
    public void setIntervalMinutes(final int intervalMinutes) {
        this.intervalMinutes = intervalMinutes;
    }

    /**
     * @return the maximum size (Mb) a log file can get to before it is rolled.
     */
    public long getMaxFileSize() {
        return maxFileSize;
    }

    /**
     * @param maxFileSize
     *            the maximum size (Mb) a log file can get to before it is
     *            rolled.
     */
    public void setMaxFileSize(final long maxFileSize) {
        this.maxFileSize = maxFileSize * MB;
    }

    /**
     * @return True if file names produced are Windows friendly.
     */
    public boolean isWindowsFriendly() {
        return windowsFriendly;
    }

    /**
     * @param windowsFriendly
     *            Determines if file names produced are Windows friendly.
     */
    public void setWindowsFriendly(boolean windowsFriendly) {
        this.windowsFriendly = windowsFriendly;
    }
}

/**
 * Determines the next time we will check the need for rollover by adding the
 * specified number of minutes to a supplied time.
 */
class RollingCalendar extends GregorianCalendar {
    private static final long serialVersionUID = -4301200043329291099L;

    private int intervalMinutes;

    RollingCalendar() {
        super();
    }

    RollingCalendar(final TimeZone tz, final Locale locale) {
        super(tz, locale);
    }

    void setIntervalMinutes(final int intervalMinutes) {
        this.intervalMinutes = intervalMinutes;
    }

    public long getNextCheckMillis(final long ms) {
        return getNextCheckMs(ms);
    }

    public long getNextCheckMs(final long ms) {
        this.setTime(new Date(ms));
        this.set(Calendar.SECOND, 0);
        this.set(Calendar.MILLISECOND, 0);
        this.add(Calendar.MINUTE, intervalMinutes);
        return getTime().getTime();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        return super.equals(obj);
    }
}
