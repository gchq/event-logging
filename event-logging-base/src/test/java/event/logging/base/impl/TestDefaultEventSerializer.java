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

import event.logging.CreateEventAction;
import event.logging.Data;
import event.logging.Event;
import event.logging.EventDetail;
import event.logging.Outcome;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TestDefaultEventSerializer {
    @Test
    public void testSerialize() {
        final Event event = buildEvent();

        final EventSerializer serializer = new DefaultEventSerializer();

        final String xml = serializer.serialize(event);

        System.out.println(xml);

        final String expectedXML = "<Event xmlns=\"event-logging:3\">\n" + "  <EventDetail>\n"
                + "    <Create>\n" + "      <Outcome>\n"
                + "        <Description>This is my desc with a \"quoted\"text</Description>\n"
                + "        <Data Name=\"MyName\" Value=\"This is my attr with a &#34;quoted&#34;text\"/>\n"
                + "      </Outcome>\n" + "    </Create>\n" + "  </EventDetail>\n" + "</Event>";

        assertThat(xml).isEqualTo(expectedXML);
    }

    private Event buildEvent() {
        final Event event = new Event();

        final EventDetail eventDetail = new EventDetail();

        final CreateEventAction createEventAction = new CreateEventAction();
        final Outcome outcome = new Outcome();
        outcome.setDescription("This is my desc with a \"quoted\"text");
        final Data data = new Data();
        data.setName("MyName");
        data.setValue("This is my attr with a \"quoted\"text");
        outcome.getData().add(data);
        createEventAction.setOutcome(outcome);

        eventDetail.setEventAction(createEventAction);
        event.setEventDetail(eventDetail);

        return event;
    }
}
