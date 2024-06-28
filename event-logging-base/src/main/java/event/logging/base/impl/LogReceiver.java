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
 * <p>Interface for classes used to receive serialised events.</p>
 *
 * <p>Implementing classes can persist the serialised event and/or send it on to a
 * system like Stroom. </p>
 *
 * By default {@link LoggerLogReceiver} will be used as the implementation.
 * If you want to use an alternative {@link LogReceiver} implementation, set the system property
 * {@code event.logging.logreceiver} to the fully qualified class name or your desired
 * {@link LogReceiver} implementation (see {@link LogReceiverFactory}.
 */
public interface LogReceiver {

    /**
     * Called for each event being logged.
     *
     * @param data
     *            The event in serialised form.
     */
    void log(String data);
}
