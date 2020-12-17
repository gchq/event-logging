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
package event.logging.base.util;

import event.logging.*;

import java.util.Date;

public final class EventLoggingUtil {

    private EventLoggingUtil() {
        // Utility class.
    }

    public static EventTime createEventTime(final Date date) {
        return EventTime.builder()
                .withTimeCreated(date)
                .build();
    }

    public static User createUser(final String userId) {
        return User.builder()
               .withId(userId)
                .build();
    }

    public static EventDetail createEventDetail(final String typeId,
                                                final String description) {
        return EventDetail.builder()
                .withTypeId(typeId)
                .withDescription(description)
                .build();
    }

    public static Data createData(final String name, final String value) {
        return Data.builder()
                .withName(name)
                .withValue(value)
                .build();
    }

    public static Term createTerm(final String name,
                                  final TermCondition condition,
                                  final String value) {
        return Term.builder()
                .withName(name)
                .withCondition(condition)
                .withValue(value)
                .build();
    }

    public static Outcome createOutcome(final Throwable throwable) {
        final Outcome outcome;
        if (throwable != null) {
            outcome = Outcome.builder()
                    .withSuccess(false)
                    .withDescription(throwable.getMessage() != null
                            ? throwable.getMessage()
                            : throwable.getClass().getName())
                    .build();
        } else {
            outcome = null;
        }
        return outcome;
    }

    public static Outcome createOutcome(final Boolean success,
                                        final String description) {
        return Outcome.builder()
                .withSuccess(success)
                .withDescription(description)
                .build();
    }
}
