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
package event.logging.gen;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 * Helper class for resources.
 */
public final class StreamUtil {
    /**
     * Buffer to use
     */
    public static final int BUFFER_SIZE = 8192;

    public final static String DEFAULT_CHARSET_NAME = "UTF-8";

    private StreamUtil() {
        // NA Utility
    }

    /**
     * Convert a resource to a string.
     * 
     * @param stream
     *            thing to convert
     * @return the string
     */
    public static String streamToString(final InputStream stream) {
        return streamToString(stream, DEFAULT_CHARSET_NAME);
    }

    /**
     * Convert a resource to a string.
     * 
     * @param stream
     *            thing to convert
     * @return the string
     */
    public static String streamToString(final InputStream stream, boolean close) {
        return streamToString(stream, DEFAULT_CHARSET_NAME, close);
    }

    /**
     * Convert a resource to a string.
     * 
     * @param stream
     *            thing to convert
     * @return the string
     */
    public static String streamToString(final InputStream stream,
            final String charsetName) {
        return streamToString(stream, charsetName, true);
    }

    /**
     * Convert a resource to a string.
     * 
     * @param stream
     *            thing to convert
     * @return the string
     */
    public static String streamToString(final InputStream stream,
            final String charsetName, boolean close) {
        try {
            if (stream == null) {
                return null;
            }

            final ByteArrayOutputStream baos = streamToBuffer(stream, close);
            return baos.toString(charsetName);
        } catch (IOException ioEx) {
            // Wrap it
            throw new RuntimeException(ioEx);
        }
    }

    /**
     * @param stream
     *            to read (and close)
     * @return buffer
     */
    public static ByteArrayOutputStream streamToBuffer(final InputStream stream) {
        return streamToBuffer(stream, true);
    }

    /**
     * @param stream
     *            to read
     * @param close
     *            or not
     * @return buffer
     */
    public static ByteArrayOutputStream streamToBuffer(
            final InputStream stream, final boolean close) {
        if (stream == null) {
            return null;
        }
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            final byte[] buffer = new byte[BUFFER_SIZE];
            int len;
            while ((len = stream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
            if (close) {
                stream.close();
            }
            return byteArrayOutputStream;
        } catch (IOException ioEx) {
            // Wrap it
            throw new RuntimeException(ioEx);
        }
    }

    /**
     * Takes a string and writes it to a file.
     * 
     * @param string
     * @param outputFileName
     */
    public static void stringToFile(final String string, final File file) {
        stringToFile(string, file, DEFAULT_CHARSET_NAME);
    }

    /**
     * Takes a string and writes it to a file.
     * 
     * @param string
     * @param outputFileName
     * @param charsetName
     */
    public static void stringToFile(final String string, final File file,
            final String charsetName) {
        try {
            if (file.isFile()) {
                file.delete();
            }
            file.getParentFile().mkdirs();

            final FileOutputStream fos = new FileOutputStream(file);
            final BufferedOutputStream bos = new BufferedOutputStream(fos);
            bos.write(string.getBytes(charsetName));
            bos.flush();
            bos.close();

        } catch (IOException ioEx) {
            // Wrap it
            throw new RuntimeException(ioEx);
        }
    }
    
    /**
     * Takes a string and writes it to an output stream.
     * 
     * @param string
     * @param outputStream
     */
    public static void stringToStream(final String string, final OutputStream outputStream) {
        stringToStream(string, outputStream, DEFAULT_CHARSET_NAME);
    }

    /**
     * Takes a string and writes it to an output stream.
     * 
     * @param string
     * @param outputStream
     * @param charsetName
     */
    public static void stringToStream(final String string, final OutputStream outputStream,
            final String charsetName) {
        try {
            outputStream.write(string.getBytes(charsetName));
            outputStream.flush();
            outputStream.close();

        } catch (IOException ioEx) {
            // Wrap it
            throw new RuntimeException(ioEx);
        }
    }

    public static String fileToString(final File file) {
        return fileToString(file, DEFAULT_CHARSET_NAME);
    }

    /**
     * Reads a file and returns it as a string.
     * 
     * @param string
     * @param outputFileName
     */
    public static String fileToString(final File file, final String charsetName) {
        return streamToString(fileToStream(file), charsetName);
    }
    
    /**
     * Reads a file and returns it as a stream.
     * 
     * @param string
     * @param outputFileName
     */
    public static InputStream fileToStream(final File file) {
        try {
            return new BufferedInputStream(new FileInputStream(
                    file));
        } catch (IOException ioEx) {
            // Wrap it
            throw new RuntimeException(ioEx);
        }
    }

    /**
     * Take a stream to a file.
     * 
     * @param inputStream
     *            to read and close
     * @param outputFileName
     *            to (over)write and close
     */
    public static void streamToFile(final InputStream inputStream,
            final String outputFileName) {
        try {
            final File file = new File(outputFileName);
            if (file.isFile()) {
                file.delete();
            }
            file.getParentFile().mkdirs();

            final FileOutputStream fos = new FileOutputStream(file);
            streamToStream(inputStream, fos);
        } catch (IOException ioEx) {
            // Wrap it
            throw new RuntimeException(ioEx);
        }
    }

    /**
     * Take a stream to another stream (AND CLOSE BOTH).
     * 
     * @param inputStream
     *            to read and close
     * @param outputStream
     *            to (over)write and close
     */
    public static void streamToStream(final InputStream inputStream,
            final OutputStream outputStream) {
        streamToStream(inputStream, outputStream, true);
    }

    /**
     * Take a stream to another stream.
     * 
     * @param inputStream
     *            to read
     * @param outputStream
     *            to (over)write
     * @param close
     *            or not
     */
    public static long streamToStream(final InputStream inputStream,
            final OutputStream outputStream, final boolean close) {
        long bytesWritten = 0;
        try {

            final byte[] buffer = new byte[BUFFER_SIZE];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
                bytesWritten += len;
            }
            if (close) {
                outputStream.close();
                inputStream.close();
            }
            return bytesWritten;
        } catch (IOException ioEx) {
            // Wrap it
            throw new RuntimeException(ioEx);
        }
    }

    /**
     * Convert a string to a stream.
     * 
     * @param string
     *            to convert
     * @return the stream or null
     */
    public static InputStream stringToStream(final String string) {
        return stringToStream(string, DEFAULT_CHARSET_NAME);
    }

    /**
     * Convert a string to a stream.
     * 
     * @param string
     *            to convert
     * @return the stream or null
     */
    public static InputStream stringToStream(final String string,
            final String charsetName) {
        if (string != null) {
            try {
                return new ByteArrayInputStream(string.getBytes(charsetName));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    /**
     * Try a read a full buffer from a stream.
     * 
     * @param stream
     *            to read from
     * @param buffer
     *            to read into
     * @throws IOException
     *             if error
     */
    public static int eagerRead(final InputStream stream, final byte[] buffer)
            throws IOException {

        int read = 0;
        int offset = 0;
        int maxToRead = buffer.length;
        while ((read = stream.read(buffer, offset, maxToRead)) != -1) {
            maxToRead = maxToRead - read;
            offset = offset + read;
            if (maxToRead == 0) {
                break;
            }
        }
        // Did not read anything ... must be finished
        if (offset == 0) {
            return -1;
        }
        // Length read
        return offset;

    }

    /**
     * Read an exact number of bytes into a buffer. Throws an exception if the
     * number of bytes are not available.
     * 
     * @param stream
     *            to read from
     * @param buffer
     *            to read into
     * @throws IOException
     *             if error
     */
    public static void fillBuffer(final InputStream stream, final byte[] buffer)
            throws IOException {
        fillBuffer(stream, buffer, 0, buffer.length);
    }

    /**
     * Read an exact number of bytes into a buffer. Throws an exception if the
     * number of bytes are not available.
     * 
     * @param stream
     *            to read from
     * @param buffer
     *            to read into
     * @param offset
     *            to use
     * @param len
     *            length
     * @throws IOException
     *             if error
     */
    public static void fillBuffer(final InputStream stream,
            final byte[] buffer, final int offset, final int len)
            throws IOException {
        int realLen = stream.read(buffer, offset, len);

        if (realLen == -1) {
            throw new IOException("Unable to fill buffer");
        }
        if (realLen != len) {
            // Try Again
            fillBuffer(stream, buffer, offset + realLen, len - realLen);
        }
    }

    /**
     * Wrap a stream and don't let it close
     * 
     * @param outputStream
     * @return
     */
    public static OutputStream ignoreClose(OutputStream outputStream) {
        return new FilterOutputStream(outputStream) {
            @Override
            public void close() throws IOException {
                flush();
                // Ignore Close
            }
        };
    }

    public static void close(OutputStream outputStream) {
        try {
            if (outputStream != null) {
                outputStream.close();
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void close(InputStream inputStream) {
        try {
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
