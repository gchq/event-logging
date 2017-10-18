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

import java.util.ArrayList;
import java.util.List;

public class StringUtil {
    public static List<String> diffStrings(final String string1, final String string2) {

        if (string1 != null && string1.length() > 0 && string2 != null && string2.length() > 0) {

            final List<Character> union = new ArrayList<>();
            final List<Character> chars1 = new ArrayList<>();
            for (final Character chr : string1.toCharArray()) {
                chars1.add(chr);
                union.add(chr);
            }

            final List<Character> chars2 = new ArrayList<>();
            for (final Character chr : string2.toCharArray()) {
                chars2.add(chr);
                union.add(chr);
            }

            final List<Character> intersection = new ArrayList<>(chars1);
            intersection.retainAll(chars2);
            union.removeAll(intersection);

            // TODO need to finish this off if it is ever used
            return null;

        } else {
            throw new RuntimeException(
                    String.format("One or both strings are empty, string1: [%s], string2 [%s]", string1, string2));
        }
    }

}
