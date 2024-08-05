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

/**
 * Should be implemented by classes providing a way to validate some XML.
 * The default implementation is {@link event.logging.base.impl.DefaultXMLValidator}.
 */
public interface XMLValidator {

    /**
     * Validates the <b>XML</b> and optionally writes the result to a file or
     * log.
     *
     * @param xml
     *            The XML to validate.
     * @throws event.logging.base.impl.ValidationException if the implementation chooses
     * to throw exceptions on validation failures.
     */
    void validate(String xml);
}
