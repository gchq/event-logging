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
package event.logging.util;

import event.logging.AuthenticateAction;
import event.logging.AuthenticateOutcome;
import event.logging.AuthenticateOutcomeReason;
import event.logging.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class AuthenticateUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticateUtil.class);

    private static final String LOGON = "Logon";
    private static final String LOGOFF = "Logoff";

    private AuthenticateUtil() {
        // Utility class.
    }

    public static void logon(final Event event, final String userId, final Boolean successful,
                             final Boolean interactive, final AuthenticateOutcomeReason reason) {
        try {
            // Create authenticate object.
            final Event.EventDetail.Authenticate authenticate = new Event.EventDetail.Authenticate();
            authenticate.setAction(AuthenticateAction.LOGON);
            if (userId != null) {
                authenticate.setUser(EventLoggingUtil.createUser(userId));
            }
            if (!successful) {
                final AuthenticateOutcome outcome = new AuthenticateOutcome();
                outcome.setSuccess(Boolean.FALSE);

                // Store reason for failure.
                if (reason != null) {
                    outcome.setReason(reason);
                }

                authenticate.setOutcome(outcome);
            }

            // Create event detail.
            final Event.EventDetail eventDetail = EventLoggingUtil.createEventDetail(LOGON, LOGON);
            eventDetail.setAuthenticate(authenticate);

            if (userId != null) {
                event.getEventSource().setUser(EventLoggingUtil.createUser(userId));
            }
            event.setEventDetail(eventDetail);

        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public static void logoff(final Event event, final String userId, final Boolean successful) {
        try {
            // Create authenticate object.
            final Event.EventDetail.Authenticate authenticate = new Event.EventDetail.Authenticate();
            authenticate.setAction(AuthenticateAction.LOGOFF);
            if (userId != null) {
                authenticate.setUser(EventLoggingUtil.createUser(userId));
            }
            if (!successful) {
                final AuthenticateOutcome outcome = new AuthenticateOutcome();
                outcome.setSuccess(Boolean.FALSE);

                authenticate.setOutcome(outcome);
            }

            // Create event detail.
            final Event.EventDetail eventDetail = EventLoggingUtil.createEventDetail(LOGOFF, LOGOFF);
            eventDetail.setAuthenticate(authenticate);

            if (userId != null) {
                event.getEventSource().setUser(EventLoggingUtil.createUser(userId));
            }
            event.setEventDetail(eventDetail);

        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
