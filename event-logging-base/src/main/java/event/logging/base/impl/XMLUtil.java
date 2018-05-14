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

/**
 * A utility class used to escape XML content.
 */
public final class XMLUtil {
    private static final int CHAR_REF_LEN = 10;
    private static final int N0X2028 = 0x2028;
    private static final int N160 = 160;
    private static final int N128 = 128;
    private static final int N127 = 127;
    private static final int N32 = 32;
    private static final int N31 = 31;
    /**
     * An array of booleans that indicate if a character has to be escaped in
     * text content.
     */
    private static boolean[] specialInText;
    /**
     * An array of booleans that indicate if a character has to be escaped in an
     * attribute.
     */
    private static boolean[] specialInAtt;

    /**
     * Create lookup arrays.
     */
    static {
        specialInText = new boolean[N128];
        for (int i = 0; i <= N31; i++) {
            // Allowed in XML 1.1 as character.
            specialInText[i] = true;
        }

        // References
        for (int i = N32; i <= N127; i++) {
            specialInText[i] = false;
        }

        // Note, 0 is used to switch escaping on and off for mapped characters.
        specialInText['\n'] = false;
        specialInText['\t'] = false;
        specialInText['\r'] = true;
        specialInText['<'] = true;
        specialInText['>'] = true;
        specialInText['&'] = true;

        specialInAtt = new boolean[N128];

        for (int i = 0; i <= N31; i++) {
            // Allowed in XML 1.1 as character.
            specialInAtt[i] = true;
        }

        // References
        for (int i = N32; i <= N127; i++) {
            specialInAtt[i] = false;
        }

        // Used to switch escaping on and off for mapped characters.
        specialInAtt[(char) 0] = true;

        specialInAtt['\r'] = true;
        specialInAtt['\n'] = true;
        specialInAtt['\t'] = true;
        specialInAtt['<'] = true;
        specialInAtt['>'] = true;
        specialInAtt['&'] = true;
        specialInAtt['\"'] = true;
    }

    /**
     * Utility class so private constructor.
     */
    private XMLUtil() {
        // Utility class.
    }

    /**
     * Write contents of array to current writer, after escaping special
     * characters. This method converts the XML special characters (such as &lt;
     * and &amp;) into their predefined entities.
     * 
     * @param value
     *            The character sequence containing the string.
     * @param inAttribute
     *            Set to true if the text is in an attribute value.
     * @param output
     *            The <code>StringBuilder</code> to write the output to.
     */
    public static void escape(final String value, final boolean inAttribute,
            final StringBuilder output) {
        int segstart = 0;

        // Choose which set of special characters to use.
        boolean[] specialChars = specialInText;
        if (inAttribute) {
            specialChars = specialInAtt;
        }

        final int clength = value.length();
        while (segstart < clength) {
            int i = segstart;
            // find a maximal sequence of "ordinary" characters
            while (i < clength) {
                final char c = value.charAt(i);
                if (c < N127) {
                    if (specialChars[c]) {
                        break;
                    } else {
                        i++;
                    }
                } else if (c < N160) {
                    break;
                } else if (c == N0X2028) {
                    break;
                } else {
                    i++;
                }
            }

            // if this was the whole string write it out and exit
            if (i >= clength) {
                if (segstart == 0) {
                    output.append(value);
                } else {
                    output.append(value.subSequence(segstart, i));
                }

                return;
            }

            // otherwise write out this sequence
            if (i > segstart) {
                output.append(value.subSequence(segstart, i));
            }

            // examine the special character that interrupted the scan
            final char c = value.charAt(i);

            if (c >= N127 && c < N160) {
                // XML 1.1 requires these characters to be written as character
                // references
                outputCharacterReference(c, output);
            } else if (c >= N160) {
                if (c == N0X2028) {
                    outputCharacterReference(c, output);
                } else {
                    // process characters not available in the current encoding
                    outputCharacterReference(c, output);
                }

            } else {
                // process special ASCII characters
                if (c == '<') {
                    output.append("&lt;");
                } else if (c == '>') {
                    output.append("&gt;");
                } else if (c == '&') {
                    output.append("&amp;");
                } else if (c == '\"') {
                    output.append("&#34;");
                } else if (c == '\n') {
                    output.append("&#xA;");
                } else if (c == '\r') {
                    output.append("&#xD;");
                } else if (c == '\t') {
                    output.append("&#x9;");
                } else {
                    // C0 control characters
                    outputCharacterReference(c, output);
                }
            }

            segstart = ++i;
        }
    }

    private static void outputCharacterReference(final int charval,
            final StringBuilder output) {
        int o = 0;
        char[] charref = new char[CHAR_REF_LEN];
        charref[o++] = '&';
        charref[o++] = '#';
        String code = Integer.toString(charval);
        int len = code.length();
        for (int k = 0; k < len; k++) {
            charref[o++] = code.charAt(k);
        }
        charref[o++] = ';';
        output.append(charref, 0, o);
    }
}
