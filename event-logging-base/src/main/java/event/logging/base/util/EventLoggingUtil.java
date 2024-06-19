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

import event.logging.BaseOutcome;
import event.logging.Data;
import event.logging.EventDetail;
import event.logging.EventTime;
import event.logging.Outcome;
import event.logging.Term;
import event.logging.TermCondition;
import event.logging.User;

import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.util.Date;

public final class EventLoggingUtil {

    private EventLoggingUtil() {
        // Utility class.
    }

    /**
     * Use {@link EventLoggingUtil#createEventTime(Instant)} instead.
     */
    @Deprecated(forRemoval = true)
    public static EventTime createEventTime(final Date date) {
        final Instant instant = date != null
                ? date.toInstant()
                : null;
        return createEventTime(instant);
    }

    public static EventTime createEventTime(final Instant date) {
        return EventTime.builder()
                .withTimeCreated(date)
                .build();
    }

    /**
     * @return An EventTime for the current time, i.e. {@link Instant#now()}
     */
    public static EventTime createCurrentEventTime() {
        return EventTime.builder()
                .withTimeCreated(Instant.now())
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

    /**
     * Create a new instance of {@link Outcome} with success == false using
     * the {@link Throwable} to set the outcome description.
     */
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

    /**
     * Create a new instance of {@link Outcome} with success == false using
     * the {@link Throwable} to set the outcome description.
     * @param outcomeType The sub-class of {@link BaseOutcome} required.
     */
    public static <T extends BaseOutcome> T createOutcome(final Class<T> outcomeType,
                                                          final Throwable throwable) {
        final T outcome;
        if (throwable != null) {
            outcome = createOutcome(
                    outcomeType,
                    false,
                    throwable.getMessage() != null
                            ? throwable.getMessage()
                            : throwable.getClass().getName());
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

    /**
     * Create a new instance of {@link Outcome} setting success and description
     * @param outcomeType The sub-class of {@link BaseOutcome} required.
     */
    public static <T extends BaseOutcome> T createOutcome(final Class<T> outcomeType,
                                                          final Boolean success,
                                                          final String description) {
        final T outcome;
        try {
            outcome = outcomeType.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("Error invoking noargs constructor on " + outcomeType.getName(), e);
        }
        outcome.setSuccess(success);
        outcome.setDescription(description);
        return outcome;
    }
}
