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

import event.logging.AdvancedQuery;
import event.logging.And;
import event.logging.AnyContent;
import event.logging.AuthenticateAction;
import event.logging.AuthenticateEventAction;
import event.logging.AuthenticateOutcome;
import event.logging.AuthenticateOutcomeReason;
import event.logging.CreateEventAction;
import event.logging.Criteria;
import event.logging.Data;
import event.logging.Destination;
import event.logging.Device;
import event.logging.Document;
import event.logging.Event;
import event.logging.EventDetail;
import event.logging.EventSource;
import event.logging.EventTime;
import event.logging.ExportEventAction;
import event.logging.ImportEventAction;
import event.logging.MultiObject;
import event.logging.Outcome;
import event.logging.Purpose;
import event.logging.Query;
import event.logging.SearchEventAction;
import event.logging.SendEventAction;
import event.logging.Source;
import event.logging.SystemDetail;
import event.logging.Term;
import event.logging.TermCondition;
import event.logging.User;
import event.logging.base.ComplexLoggedOutcome;
import event.logging.base.EventLoggingService;
import event.logging.util.EventLoggingUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests the creation of event logging data.
 * <p>
 * IMPORTANT You need to be careful about which versions of classes you use in here, i.e. event.logging
 * or event.logging.base. When the classes in .base. are copied into event.logging they have the .base. part
 * removed.
 */
class FluentEventLoggingServiceIT {
    private static final Logger LOGGER = LoggerFactory.getLogger(FluentEventLoggingServiceIT.class);
//    static {
//        BasicConfigurator.configure();
//    }

    private static final int WAIT_BETWEEN_RECORDS = 0; // 1000 to test time roll
    private static final int NUM_OF_THREADS = 10;
    private static final int TIMES_TO_RUN = 10;
    private static final int NUM_OF_RECORDS = 10;

    private EventLoggingService getEventLoggingService() {
        return new DefaultEventLoggingService(
                ExceptionAndLoggingErrorHandler::new,
                ValidationExceptionBehaviourMode.THROW);
    }

    @Test
    void testPartiallyFluentExample() {
        final EventLoggingService eventLoggingService = new DefaultEventLoggingService();

        final Event event = Event.builder()
                .withEventTime(EventTime.builder()
                        .withTimeCreated(Instant.now())
                        .build())
                .withEventSource(EventSource.builder()
                        .withSystem(SystemDetail.builder()
                                .withName("Test System")
                                .withEnvironment("Test")
                                .build())
                        .withGenerator("JUnit")
                        .withDevice(Device.builder()
                                .withIPAddress("123.123.123.123")
                                .build())
                        .withUser(User.builder()
                                .withId("someuser")
                                .build())
                        .build())
                .withEventDetail(EventDetail.builder()
                        .withTypeId("LOGON")
                        .withDescription("A user logon")
                        .withAuthenticate(AuthenticateEventAction.builder()
                                .withAction(AuthenticateAction.LOGON)
                                .withUser(User.builder()
                                        .withId("someuser")
                                        .build())
                                .build())
                        .build())
                .build();

        eventLoggingService.log(event);
    }

    @Test
    void testFullyFluentExample() {
        final EventLoggingService eventLoggingService = new DefaultEventLoggingService();

        // @formatter:off
        final Event event = Event.builder()
                .withEventTime()
                    .withTimeCreated(Instant.now())
                    .end()
                .withEventSource()
                    .withSystem()
                        .withName("Test System")
                        .withEnvironment("Test")
                        .end()
                    .withGenerator("JUnit")
                    .withDevice()
                        .withIPAddress("123.123.123.123")
                        .end()
                    .withUser()
                        .withId("someuser")
                        .end()
                    .end()
                .withEventDetail()
                    .withTypeId("LOGON")
                    .withDescription("A user logon")
                    .withAuthenticate()
                        .withAction(AuthenticateAction.LOGON)
                        .withUser()
                            .withId("someuser")
                        .end()
                    .end()
                .end()
                .build();
        // @formatter:on

        eventLoggingService.log(event);
    }

    /**
     * Tests the creation of some events using paths.
     *
     * @throws Exception Could be thrown.
     */
    @Test
    void testBasic() throws Exception {
        final long time = java.lang.System.currentTimeMillis();

        final Event event = createBasicEvent("LOGIN", "LOGIN");
        event.getEventDetail()
                .setEventAction(AuthenticateEventAction.builder()
                        .withAction(AuthenticateAction.LOGON)
                        .withUser(User.builder()
                                .withId("user1")
                                .build())
                        .build());

        final EventLoggingService eventLoggingService = getEventLoggingService();
        eventLoggingService.setValidate(true);
        eventLoggingService.log(event);

        for (int i = 0; i < NUM_OF_RECORDS; i++) {
            event.getEventTime().setTimeCreated(Instant.now());
            eventLoggingService.log(event);

            Thread.sleep(WAIT_BETWEEN_RECORDS);
        }

        java.lang.System.out.println("Total time = " + (java.lang.System.currentTimeMillis() - time));
    }

    @Test
    void testLoggedResult_Simple() {

        final String username = "jbloggs";
        final Integer result = getEventLoggingService().loggedWorkBuilder()
                .withTypeId("login")
                .withDescription("User " + username + " logged in")
                .withDefaultEventAction(AuthenticateEventAction.builder()
                        .withUser(User.builder()
                                .withName(username)
                                .build())
                        .withAction(AuthenticateAction.LOGON)
                        .build())
                .withSimpleLoggedResult(() -> {
                    // Now do the logged work and return the result of it
                    return 42;
                })
                .getResultAndLog();

        Assertions.assertThat(result)
                .isEqualTo(42);
    }

    @Test
    void testLoggedAction_Simple() {

        final String username = "jbloggs";
        getEventLoggingService().loggedWorkBuilder()
                .withTypeId("login")
                .withDescription("User " + username + " logged in")
                .withDefaultEventAction(AuthenticateEventAction.builder()
                        .withUser(User.builder()
                                .withName(username)
                                .build())
                        .withAction(AuthenticateAction.LOGON)
                        .build())
                .withSimpleLoggedAction(() -> {
                    // Now do the logged work and return the result of it
                    System.out.println("Doing some work");
                })
                .runActionAndLog();
    }

    @Test
    void testLoggedResult_Advanced() {

        final String username = "jbloggs";
        final Integer result = getEventLoggingService().loggedWorkBuilder()
                .withTypeId("login")
                .withDescription("User " + username + " logged in")
                .withDefaultEventAction(AuthenticateEventAction.builder()
                        .withUser(User.builder()
                                .withName(username)
                                .build())
                        .withAction(AuthenticateAction.LOGON)
                        .build())
                .withComplexLoggedResult(eventAction -> {
                    // Now do the logged work and return the result of it
                    final int theAnswer = 42;
                    final AuthenticateEventAction newEventAction = eventAction.newCopyBuilder()
                            .withData(Data.builder()
                                    .withName("extraInfo")
                                    .withValue("a value")
                                    .build())
                            .build();
                    return ComplexLoggedOutcome.success(42, newEventAction);
                })
                .withPurpose(Purpose.builder()
                        .withJustification("Approval No. 23433393")
                        .build())
                .withCustomExceptionHandler((eventAction, throwable) ->
                        eventAction.newCopyBuilder()
                                .withOutcome(AuthenticateOutcome.builder()
                                        .withReason(AuthenticateOutcomeReason.ACCOUNT_LOCKED)
                                        .withDescription(throwable.getMessage())
                                        .withSuccess(false)
                                        .build())
                                .build()
                )
                .getResultAndLog();

        Assertions.assertThat(result)
                .isEqualTo(42);
    }

    @Test
    void testLoggedAction_Advanced() {

        final String username = "jbloggs";
        getEventLoggingService().loggedWorkBuilder()
                .withTypeId("login")
                .withDescription("User " + username + " logged in")
                .withDefaultEventAction(AuthenticateEventAction.builder()
                        .withUser(User.builder()
                                .withName(username)
                                .build())
                        .withAction(AuthenticateAction.LOGON)
                        .build())
                .withComplexLoggedAction(eventAction -> {

                    // Now do the logged work and return the outcome

                    AuthenticateEventAction newEventAction = eventAction.newCopyBuilder()
                            .withData(Data.builder()
                                    .withName("extraInfo")
                                    .withValue("a value")
                                    .build())
                            .build();

                    return ComplexLoggedOutcome.success(newEventAction);
                })
                .withCustomExceptionHandler((eventAction, throwable) ->
                        eventAction.newCopyBuilder()
                                .withOutcome(AuthenticateOutcome.builder()
                                        .withReason(AuthenticateOutcomeReason.ACCOUNT_LOCKED)
                                        .withDescription(throwable.getMessage())
                                        .withSuccess(false)
                                        .build())
                                .build()
                )
                .runActionAndLog();
    }

    /**
     * Tests the creation of some events using paths.
     *
     * @throws Exception Could be thrown.
     */
    @Test
    void testMultiThread() throws Exception {
        final long time = java.lang.System.currentTimeMillis();

        final ExecutorService executorService = Executors.newFixedThreadPool(NUM_OF_THREADS);
        final AtomicInteger done = new AtomicInteger();

        final EventLoggingService eventLoggingService = getEventLoggingService();

        for (int i = 1; i <= TIMES_TO_RUN; i++) {
            executorService.execute(() -> {
                try {
                    for (int j = 1; j <= NUM_OF_RECORDS; j++) {

                        final Event event = createBasicEvent("LOGIN", "LOGIN");
                        event.getEventDetail()
                                .setEventAction(AuthenticateEventAction.builder()
                                        .withAction(AuthenticateAction.LOGON)
                                        .withUser(User.builder()
                                                .withId("someuser")
                                                .build())
                                        .build());

                        event.getEventTime().setTimeCreated(Instant.now());
                        eventLoggingService.setValidate(true);
                        eventLoggingService.log(event);
                        done.incrementAndGet();
                    }
                } catch (final Exception e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            });
        }

        final int total = TIMES_TO_RUN * NUM_OF_RECORDS;
        while (done.get() < total) {
            Thread.sleep(WAIT_BETWEEN_RECORDS);
        }

        java.lang.System.out.println("Total time = " + (java.lang.System.currentTimeMillis() - time));
    }

    private Event createBasicEvent(final String typeId, final String description) {

        return Event.builder()
                .withEventTime(EventTime.builder()
                        .withTimeCreated(Instant.now())
                        .build())
                .withEventSource(EventSource.builder()
                        .withSystem(SystemDetail.builder()
                                .withName("Test System")
                                .withEnvironment("Test")
                                .build())
                        .withGenerator("JUnit")
                        .withDevice(Device.builder()
                                .withIPAddress("123.123.123.123")
                                .build())
                        .withUser(User.builder()
                                .withId("someuser")
                                .build())
                        .build())
                .withEventDetail(EventDetail.builder()
                        .withTypeId(typeId)
                        .withDescription(description)
                        .withEventAction(AuthenticateEventAction.builder()
                                .withAction(AuthenticateAction.LOGON)
                                .build())
                        .build())
                .build();
    }

    private Event createBasicEvent(final EventDetail eventDetail) {

        Objects.requireNonNull(eventDetail);

        return Event.builder()
                .withEventTime(EventTime.builder()
                        .withTimeCreated(Instant.now())
                        .build())
                .withEventSource(EventSource.builder()
                        .withSystem(SystemDetail.builder()
                                .withName("Test System")
                                .withEnvironment("Test")
                                .build())
                        .withGenerator("JUnit")
                        .withDevice(Device.builder()
                                .withIPAddress("123.123.123.123")
                                .build())
                        .withUser(User.builder()
                                .withId("someuser")
                                .build())
                        .build())
                .withEventDetail(eventDetail)
                .build();
    }

    /**
     * Tests the creation of some events using paths and attributes.
     *
     * @throws Exception Could be thrown.
     */
    @Test
    void testAttributes() throws Exception {
        final long time = java.lang.System.currentTimeMillis();


        final Event event = createBasicEvent("LOGIN", "LOGIN");

        final AuthenticateEventAction.Builder<Void> authenticateBuilder = AuthenticateEventAction.builder()
                .withAction(AuthenticateAction.LOGON)
                .withUser(EventLoggingUtil.createUser("someuser"));

        for (int i = 0; i < 5; i++) {
            authenticateBuilder.addData(EventLoggingUtil.createData("somename" + i, "somevalue" + i));
            authenticateBuilder.addData(EventLoggingUtil.createData("someothername" + i, "someothervalue" + i));
        }

        event.getEventDetail().setEventAction(authenticateBuilder.build());

        final EventLoggingService eventLoggingService = getEventLoggingService();

        eventLoggingService.setValidate(true);
        eventLoggingService.log(event);

        for (int i = 0; i < NUM_OF_RECORDS; i++) {
            event.getEventTime().setTimeCreated(Instant.now());
            eventLoggingService.log(event);

            Thread.sleep(WAIT_BETWEEN_RECORDS);
        }

        java.lang.System.out.println("Total time = " + (java.lang.System.currentTimeMillis() - time));
    }

    /**
     * Tests the creation of some events using paths and attributes.
     *
     * @throws Exception Could be thrown.
     */
    @Test
    void testCreateEvent() throws Exception {
        final long time = java.lang.System.currentTimeMillis();

        final Event event = createBasicEvent(
                EventDetail.builder()
                        .withTypeId("Create")
                        .withDescription("Create object")
                        .withEventAction(CreateEventAction.builder()
                                .addDocument(Document.builder()
                                        .withId("Test Id")
                                        .withTitle("Test Title")
                                        .build())
                                .withOutcome(Outcome.builder()
                                        .withSuccess(Boolean.TRUE)
                                        .build())
                                .build())
                        .build());

        final EventLoggingService eventLoggingService = getEventLoggingService();

        eventLoggingService.setValidate(true);
        eventLoggingService.log(event);

        for (int i = 0; i < NUM_OF_RECORDS; i++) {
            event.getEventTime().setTimeCreated(Instant.now());
            eventLoggingService.log(event);

            Thread.sleep(WAIT_BETWEEN_RECORDS);
        }

        java.lang.System.out.println("Total time = " + (java.lang.System.currentTimeMillis() - time));
    }

    @Test
    void testNastyChars() {
        final String rawQuery = "(?'v?v&amp;?6?#46?R?6????????r????????-w?)::TYPE_TDI| additionalSearchParameters={includeAutoExpIdentifiers=SELECTED, caseInsensitiveMatching=SELECTED, allowWildcards=SELECTED}";

        final Event event = createBasicEvent(
                EventDetail.builder()
                        .withTypeId("Search")
                        .withDescription("Nasty search")
                        .withEventAction(SearchEventAction.builder()
                                .withQuery(Query.builder()
                                        .withRaw(rawQuery)
                                        .build())
                                .build()).build());

        final EventLoggingService eventLoggingService = getEventLoggingService();

        eventLoggingService.setValidate(true);
        eventLoggingService.log(event);
    }

    @Test
    void testSmartQuotes() {

        final Event event = createBasicEvent(
                EventDetail.builder()
                        .withTypeId("Search")
                        .withDescription("Nasty search")
                        .withEventAction(SearchEventAction.builder()
                                .withQuery(Query.builder()
                                        .withRaw("Daves quote")
                                        .build())
                                .build()).build());

        final EventLoggingService eventLoggingService = getEventLoggingService();

        eventLoggingService.setValidate(true);
        eventLoggingService.log(event);
    }

    @Test
    void testNastyChars2() {
        final StringBuilder sb = new StringBuilder();
        for (int i = Character.MIN_VALUE; i <= Character.MAX_VALUE; i++) {
            if (i % 100 == 0) {
                sb.append('\n');
            }
            sb.append((char) i);
        }

        final Event event = createBasicEvent(
                EventDetail.builder()
                        .withTypeId("Search")
                        .withDescription("Nasty search")
                        .withEventAction(SearchEventAction.builder()
                                .withQuery(Query.builder()
                                        .withRaw(sb.toString())
                                        .build())
                                .build()).build());

        final EventLoggingService eventLoggingService = getEventLoggingService();

        eventLoggingService.setValidate(true);
        eventLoggingService.log(event);
    }

    @Test
    void testSendReceive() {

        final Event event = createBasicEvent(
                EventDetail.builder()
                        .withTypeId("send")
                        .withDescription("send event")
                        .withEventAction(SendEventAction.builder()
                                .withSource(Source.builder()
                                        .addUser()
                                        .withId("sourceUserId")
                                        .end()
                                        .addDevice()
                                        .withHostName("sourceHost")
                                        .end()
                                        .build())
                                .withDestination(Destination.builder()
                                        .addUser()
                                        .withId("destUserId")
                                        .end()
                                        .addDevice()
                                        .withHostName("destHost")
                                        .end()
                                        .build())
                                .build())
                        .build());

        final EventLoggingService eventLoggingService = getEventLoggingService();

        eventLoggingService.setValidate(true);
        eventLoggingService.log(event);
    }

    @Test
    void testImport() {

        final Event event = createBasicEvent(EventDetail.builder()
                .withTypeId("Import")
                .withDescription("Import event")
                .withEventAction(ImportEventAction.builder()
                        .withSource()
                        .addFile()
                        .withName("sourceFile")
                        .end()
                        .end()
                        .withDestination()
                        .addFile()
                        .withName("destFile")
                        .end()
                        .end()
                        .build())
                .build());

        final EventLoggingService eventLoggingService = getEventLoggingService();

        eventLoggingService.setValidate(true);
        eventLoggingService.log(event);
    }

    @Test
    void testCreateQuery() {

        final Event event = createBasicEvent(EventDetail.builder()
                .withTypeId("Query")
                .withDescription("Query event")
                .withEventAction(SearchEventAction.builder()
                        .withQuery(Query.builder()
                                .withRaw("my query string!")
                                .build())
                        .build())
                .build());

        final EventLoggingService eventLoggingService = getEventLoggingService();

        eventLoggingService.setValidate(true);
        eventLoggingService.log(event);
    }

    @Test
    void testCreateExportAdvancedQuery() {

        final Event event = createBasicEvent(EventDetail.builder()
                .withTypeId("Export-Criteria")
                .withDescription("Export-Criteria-Search")
                .withEventAction(ExportEventAction.builder()
                        .withSource(MultiObject.builder()
                                .addCriteria(Criteria.builder()
                                        .withQuery(Query.builder()
                                                .withAdvanced(AdvancedQuery.builder()
                                                        .addAnd(And.builder()
                                                                .addTerm(Term.builder()
                                                                        .withName("date_modified")
                                                                        .withCondition(TermCondition.GREATER_THAN)
                                                                        .withValue("56789")
                                                                        .build())
                                                                .addTerm(Term.builder()
                                                                        .withName("date_modified")
                                                                        .withCondition(TermCondition.LESS_THAN)
                                                                        .withValue("98765")
                                                                        .build())
                                                                .build())
                                                        .build())
                                                .build())
                                        .build())
                                .build())
                        .withData(Data.builder()
                                .withName("MyName")
                                .withValue("MyValue")
                                .build())
                        .build())
                .build());
        final EventLoggingService eventLoggingService = getEventLoggingService();

        eventLoggingService.setValidate(true);
        eventLoggingService.log(event);
    }

    /**
     * Tests the creation of an event containing JSON data in the Meta
     *
     * @throws Exception Could be thrown.
     */
    @Test
    void testMetaWithJSON() throws Exception {
        final long time = java.lang.System.currentTimeMillis();

        final Event event = createBasicEvent(EventDetail.builder()
                .withTypeId("Create")
                .withDescription("Create object")
                .withEventAction(CreateEventAction.builder()
                        .addDocument(Document.builder()
                                .withId("TestId")
                                .withTitle("Test Title")
                                .withMeta(AnyContent.builder()
                                        .withContentType("JSON:streamMeta")
                                        .withVersion("1.2.3")
                                        .withContent("{\"streamMeta\":{\"streamId\":\"12345\",\"eventId\":\"45678\"}}")
                                        .build())
                                .build())
                        .withOutcome(Outcome.builder()
                                .withSuccess(Boolean.TRUE)
                                .build())
                        .build())
                .build());

        final EventLoggingService eventLoggingService = getEventLoggingService();

        eventLoggingService.setValidate(true);
        eventLoggingService.log(event);

        for (int i = 0; i < NUM_OF_RECORDS; i++) {
            event.getEventTime().setTimeCreated(Instant.now());
            eventLoggingService.log(event);

            Thread.sleep(WAIT_BETWEEN_RECORDS);
        }

        java.lang.System.out.println("Total time = " + (java.lang.System.currentTimeMillis() - time));
    }

    /**
     * Tests the creation of an event containing XML data in the Meta
     *
     * @throws Exception Could be thrown.
     */
    @Test
    void testMetaWithXML() throws Exception {
        final long time = java.lang.System.currentTimeMillis();

        final Event event = createBasicEvent(EventDetail.builder()
                .withTypeId("Create")
                .withDescription("Create object")
                .withEventAction(CreateEventAction.builder()
                        .addDocument(Document.builder()
                                .withId("TestId")
                                .withTitle("Test Title")
                                .withMeta(AnyContent.builder()
                                        .withContentType("JSON:streamMeta")
                                        .withVersion("1.2.3")
                                        .withContent("<MyMeta xmlns=\"http://myorg.mydomain.mymeta\"><ElementA>value A</ElementA><ElementB>value B</ElementB></MyMeta>")
                                        .build())
                                .build())
                        .withOutcome(Outcome.builder()
                                .withSuccess(Boolean.TRUE)
                                .build())
                        .build())
                .build());

        final EventLoggingService eventLoggingService = getEventLoggingService();

        eventLoggingService.setValidate(true);
        eventLoggingService.log(event);

        for (int i = 0; i < NUM_OF_RECORDS; i++) {
            event.getEventTime().setTimeCreated(Instant.now());
            eventLoggingService.log(event);

            Thread.sleep(WAIT_BETWEEN_RECORDS);
        }

        java.lang.System.out.println("Total time = " + (java.lang.System.currentTimeMillis() - time));
    }

    @Test
    void testLoggedAction_simple_success() {
        final List<Event> events = new ArrayList<>();

        final EventLoggingService eventLoggingServiceSpy = buildEventLoggingServiceSpy(events);

        AtomicBoolean isWorkDone = new AtomicBoolean(false);

        eventLoggingServiceSpy.loggedWorkBuilder()
                .withTypeId("MyTypeId")
                .withDescription("My description")
                .withDefaultEventAction(SearchEventAction.builder()
                        .withQuery(Query.builder()
                                .withRaw("Find stuff")
                                .build())
                        .build())
                .withSimpleLoggedAction(() ->
                        isWorkDone.set(true))
                .runActionAndLog();

        assertThat(isWorkDone)
                .isTrue();
        assertThat(events)
                .hasSize(1);
        assertThat(events.get(0).getEventDetail().getEventAction())
                .isInstanceOf(SearchEventAction.class);
        SearchEventAction searchEventAction = (SearchEventAction) events.get(0)
                .getEventDetail()
                .getEventAction();

        assertThat(searchEventAction.getQuery().getRaw())
                .isEqualTo("Find stuff");

        // Success is assumed with no outcome
        assertThat(searchEventAction.getOutcome())
                .isNull();
    }

    @Test
    void testLoggedAction_simple_failure() {
        final List<Event> events = new ArrayList<>();

        final EventLoggingService eventLoggingServiceSpy = buildEventLoggingServiceSpy(events);

        AtomicBoolean isWorkDone = new AtomicBoolean(false);

        Assertions.assertThatExceptionOfType(RuntimeException.class)
                .describedAs("Boom")
                .isThrownBy(() ->
                        eventLoggingServiceSpy.loggedWorkBuilder()
                                .withTypeId("MyTypeId")
                                .withDescription("My description")
                                .withDefaultEventAction(SearchEventAction.builder()
                                        .withQuery(Query.builder()
                                                .withRaw("Find stuff")
                                                .build())
                                        .build())
                                .withSimpleLoggedAction(() -> {
                                    throw new RuntimeException("Boom");
                                })
                                .runActionAndLog());

        assertThat(isWorkDone)
                .isFalse();
        assertThat(events)
                .hasSize(1);
        assertThat(events.get(0).getEventDetail().getEventAction())
                .isInstanceOf(SearchEventAction.class);
        SearchEventAction searchEventAction = (SearchEventAction) events.get(0)
                .getEventDetail()
                .getEventAction();

        assertThat(searchEventAction.getQuery().getRaw())
                .isEqualTo("Find stuff");

        // Check we have failure outcome
        assertThat(searchEventAction.getOutcome())
                .isNotNull();
        assertThat(searchEventAction.getOutcome().isSuccess())
                .isFalse();
        assertThat(searchEventAction.getOutcome().getDescription())
                .contains("Boom");
    }

    @Test
    void testLoggedAction_advanced_failure() {
        final List<Event> events = new ArrayList<>();

        final EventLoggingService eventLoggingServiceSpy = buildEventLoggingServiceSpy(events);

        AtomicBoolean isWorkDone = new AtomicBoolean(false);

        Assertions.assertThatExceptionOfType(RuntimeException.class)
                .describedAs("Boom")
                .isThrownBy(() -> {
                    eventLoggingServiceSpy.loggedWorkBuilder()
                            .withTypeId("MyTypeId")
                            .withDescription("My description")
                            .withDefaultEventAction(SearchEventAction.builder()
                                    .withQuery(Query.builder()
                                            .withRaw("Find stuff")
                                            .build())
                                    .build())
                            .withSimpleLoggedAction(() -> {
                                throw new RuntimeException("Boom");
                            })
                            .withCustomExceptionHandler((eventAction, throwable) ->
                                    eventAction.newCopyBuilder()
                                            .withOutcome(Outcome.builder()
                                                    .withSuccess(false)
                                                    .withDescription("It went boom!!!")
                                                    .build())
                                            .build())
                            .runActionAndLog();
                });

        assertThat(isWorkDone)
                .isFalse();
        assertThat(events)
                .hasSize(1);
        assertThat(events.get(0).getEventDetail().getEventAction())
                .isInstanceOf(SearchEventAction.class);
        SearchEventAction searchEventAction = (SearchEventAction) events.get(0)
                .getEventDetail()
                .getEventAction();

        assertThat(searchEventAction.getQuery().getRaw())
                .isEqualTo("Find stuff");

        // Check we have failure outcome
        assertThat(searchEventAction.getOutcome())
                .isNotNull();
        assertThat(searchEventAction.getOutcome().isSuccess())
                .isFalse();

        // Non standard outcome msg
        assertThat(searchEventAction.getOutcome().getDescription())
                .contains("It went boom!!!");
    }

    @Test
    void testLoggedResult_advanced_success() {
        final List<Event> events = new ArrayList<>();

        final EventLoggingService eventLoggingServiceSpy = buildEventLoggingServiceSpy(events);

        final int expectedResult = 42;
        final int result = eventLoggingServiceSpy.loggedWorkBuilder()
                .withTypeId("MyTypeId")
                .withDescription("My description")
                .withDefaultEventAction(SearchEventAction.builder()
                        .withQuery(Query.builder()
                                .withRaw("Find stuff")
                                .build())
                        .build())
                .withComplexLoggedResult(eventAction -> {
                    eventAction.getQuery().setRaw("Find stuff 2");
                    return ComplexLoggedOutcome.success(expectedResult, eventAction);
                })
                .getResultAndLog();

        assertThat(result)
                .isEqualTo(expectedResult);
        assertThat(events)
                .hasSize(1);
        assertThat(events.get(0).getEventDetail().getEventAction())
                .isInstanceOf(SearchEventAction.class);
        SearchEventAction searchEventAction = (SearchEventAction) events.get(0)
                .getEventDetail()
                .getEventAction();

        assertThat(searchEventAction.getQuery().getRaw())
                .isEqualTo("Find stuff 2");

        // Success is assumed with no outcome
        assertThat(searchEventAction.getOutcome())
                .isNull();
    }

    @Test
    void testBuildLogger_simpleResult() {
        final List<Event> events = new ArrayList<>();
        final EventLoggingService eventLoggingServiceSpy = buildEventLoggingServiceSpy(events);

        final long result = eventLoggingServiceSpy.loggedWorkBuilder()
                .withTypeId("MyTypeId")
                .withDescription("My description")
                .withDefaultEventAction(SearchEventAction.builder()
                       .build())
                .withSimpleLoggedResult(() -> {
                    // Do logged work
                    return 42L;
                })
                .withPurpose(Purpose.builder()
                        .withJustification("Just because")
                        .build())
                .withLoggingRequiredWhen(true)
                .getResultAndLog();

        Assertions.assertThat(result)
                .isEqualTo(42L);
    }

    @Test
    void testBuildLogger_complexResult() {
        final List<Event> events = new ArrayList<>();
        final EventLoggingService eventLoggingServiceSpy = buildEventLoggingServiceSpy(events);

        final long result = eventLoggingServiceSpy.loggedWorkBuilder()
                .withTypeId("MyTypeId")
                .withDescription("My description")
                .withDefaultEventAction(SearchEventAction.builder()
                        .build())
                .withComplexLoggedResult(eventAction -> {
                    // Do logged work

                    return ComplexLoggedOutcome.success(42L, eventAction);
                })
                .withPurpose(Purpose.builder()
                        .withJustification("Just because")
                        .build())
                .withLoggingRequiredWhen(true)
                .getResultAndLog();

        Assertions.assertThat(result)
                .isEqualTo(42L);
        final Event event = events.get(0);
        Assertions.assertThat(event)
                .isNotNull();
        Assertions.assertThat(event.getEventDetail().getPurpose().getJustification())
                .isEqualTo("Just because");
        Assertions.assertThat(event.getEventDetail().getTypeId())
                .isEqualTo("MyTypeId");
        Assertions.assertThat(event.getEventDetail().getDescription())
                .isEqualTo("My description");
    }

    @Test
    void testBuildLogger_notLogged() {
        final List<Event> events = new ArrayList<>();
        final EventLoggingService eventLoggingServiceSpy = buildEventLoggingServiceSpy(events);

        final long result = eventLoggingServiceSpy.loggedWorkBuilder()
                .withTypeId("MyTypeId")
                .withDescription("My description")
                .withDefaultEventAction(SearchEventAction.builder()
                        .build())
                .withSimpleLoggedResult(() -> {
                    // Do logged work
                    return 42L;
                })
                .withPurpose(Purpose.builder()
                        .withJustification("Just because")
                        .build())
                .withLoggingRequiredWhen(false)
                .getResultAndLog();

        Assertions.assertThat(result)
                .isEqualTo(42L);

        Assertions.assertThat(events)
                .isEmpty();
    }

    private EventLoggingService buildEventLoggingServiceSpy(final List<Event> events) {
        final EventLoggingService eventLoggingService = getEventLoggingService();
        eventLoggingService.setValidate(false);

        final EventLoggingService eventLoggingServiceSpy = Mockito.spy(eventLoggingService);

        // Override the behaviour of the log() method so we can see what gets logged
        Mockito.doAnswer(invocation -> {
            final Event event = invocation.getArgument(0, Event.class);
            events.add(event);
            return null;
        })
                .when(eventLoggingServiceSpy).log(Mockito.any());
        return eventLoggingServiceSpy;
    }

}
