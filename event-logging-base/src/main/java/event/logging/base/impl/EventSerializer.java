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

import event.logging.Event;

/**
 * This interface is used to define a method that will serialise an {@link Event} object tree
 * into a {@link String} representation of the object.
 * The default {@link EventSerializer} implementation is {@link DefaultEventSerializer}.
 */
public interface EventSerializer {
    /**
     * This method will turn the supplied event into a String.
     *
     * @param event The event to serialise.
     * @return A serialised representation of the supplied event.
     */
    String serialize(Event event);
}
