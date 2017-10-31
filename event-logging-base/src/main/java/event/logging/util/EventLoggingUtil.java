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
package event.logging.util;

import event.logging.*;
import event.logging.*;
import event.logging.Event.EventDetail;
import event.logging.Event.EventTime;

import java.util.Date;

public final class EventLoggingUtil {
    private EventLoggingUtil() {
        // Utility class.
    }

    public static Event.EventTime createEventTime(final Date date) {
        final Event.EventTime eventTime = new Event.EventTime();
        eventTime.setTimeCreated(date);
        return eventTime;
    }

    public static User createUser(final String userId) {
        final User user = new User();
        user.setId(userId);
        return user;
    }

    public static Event.EventDetail createEventDetail(final String typeId,
                                                      final String description) {
        final Event.EventDetail eventDetail = new Event.EventDetail();
        eventDetail.setTypeId(typeId);
        eventDetail.setDescription(description);
        return eventDetail;
    }

    public static Data createData(final String name, final String value) {
        final Data data = new Data();
        data.setName(name);
        data.setValue(value);
        return data;
    }

    public static Term createTerm(final String name, final TermCondition condition,
                                  final String value) {
        final Term term = new Term();
        term.setName(name);
        term.setCondition(condition);
        term.setValue(value);
        return term;
    }

    public static Outcome createOutcome(final Throwable throwable) {
        if (throwable != null) {
            if (throwable.getMessage() != null) {
                return createOutcome(Boolean.FALSE, throwable.getMessage());
            } else {
                return createOutcome(Boolean.FALSE, throwable.getClass()
                        .getName());
            }
        }

        return null;
    }

    public static Outcome createOutcome(final Boolean success,
            final String description) {
        final Outcome outcome = new Outcome();
        outcome.setSuccess(Boolean.FALSE);
        outcome.setDescription(description);
        return outcome;
    }
}
