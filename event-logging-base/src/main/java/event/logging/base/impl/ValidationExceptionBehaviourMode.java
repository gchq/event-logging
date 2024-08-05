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

public enum ValidationExceptionBehaviourMode {
    /**
     * Validation warnings/errors will be logged to a {@link org.slf4j.Logger}.
     * A single log message will be logged containing one or more validation messages.
     * It will log to ERROR or WARN level depending on the validation messages encountered.
     */
    LOG,

    /**
     * Validation errors will result in a {@link ValidationException} being thrown at the end
     * of validation if any errors or fatal errors are encounted.
     * The exception will contain one or more validation messages.
     * If only validation warnings are encountered, they will be logged as a single
     * {@link org.slf4j.Logger} log message at WARN level and containing one or more validation warnings.
     */
    THROW
}
