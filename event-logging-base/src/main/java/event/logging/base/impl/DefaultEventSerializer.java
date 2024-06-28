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
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;

/**
 *
 */
public class DefaultEventSerializer implements EventSerializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultEventSerializer.class);

    @Override
    public String serialize(final Event event) {
        return getXML(event);
    }

    public static String getXML(final Event event) {
        try {
            final StringWriter writer = new StringWriter();
            final event.logging.base.impl.XMLWriter xmlWriter = new XMLWriter(writer);
            getMarshaller().marshal(event, xmlWriter);
            return writer.toString();

        } catch (final Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static Marshaller getMarshaller() {
        try {
            final Marshaller marshaller = getContext().createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

            return marshaller;
        } catch (final Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static JAXBContext getContext() {
        // Initialize-on-Demand, Holder Class Idiom
        return Holder.JAXB_CONTEXT;
    }

    private static JAXBContext createContext() {
        try {
            LOGGER.info("Creating JAXB context");
            return JAXBContext.newInstance(Event.class);
        } catch (final Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    // --------------------------------------------------------------------------------


    // Initialize-on-Demand, Holder Class Idiom
    private static class Holder {
        private static final JAXBContext JAXB_CONTEXT = createContext();
    }
}
