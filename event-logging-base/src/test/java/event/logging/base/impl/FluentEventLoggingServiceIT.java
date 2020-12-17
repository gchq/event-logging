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
import event.logging.Query;
import event.logging.SearchEventAction;
import event.logging.SendEventAction;
import event.logging.Source;
import event.logging.SystemDetail;
import event.logging.Term;
import event.logging.TermCondition;
import event.logging.User;
import event.logging.base.EventLoggingService;
import event.logging.base.util.EventLoggingUtil;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Tests the creation of event logging data.
 */
class FluentEventLoggingServiceIT {
//    static {
//        BasicConfigurator.configure();
//    }

    private static final int WAIT_BETWEEN_RECORDS = 0; // 1000 to test time roll
    private static final int NUM_OF_THREADS = 10;
    private static final int TIMES_TO_RUN = 10;
    private static final int NUM_OF_RECORDS = 10;

    private EventLoggingService getEventLoggingService() {
        return new DefaultEventLoggingService(new ExceptionAndLoggingErrorHandler(),
                ValidationExceptionBehaviourMode.THROW);
    }

    @Test
    void testPartiallyFluentExample() {
        final EventLoggingService eventLoggingService = new DefaultEventLoggingService();

        final Event event = eventLoggingService.buildEvent()
                .withEventTime(EventTime.builder()
                        .withTimeCreated(new Date())
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

        final Event event = eventLoggingService.buildEvent()
                .withEventTime()
                        .withTimeCreated(new Date())
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
            event.getEventTime().setTimeCreated(new Date());
            eventLoggingService.log(event);

            Thread.sleep(WAIT_BETWEEN_RECORDS);
        }

        java.lang.System.out.println("Total time = " + (java.lang.System.currentTimeMillis() - time));
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

                        event.getEventTime().setTimeCreated(new Date());
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
                        .withTimeCreated(new Date())
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
                        .withTimeCreated(new Date())
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

        final AuthenticateEventAction.Builder authenticateBuilder = AuthenticateEventAction.builder()
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
            event.getEventTime().setTimeCreated(new Date());
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
            event.getEventTime().setTimeCreated(new Date());
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
            event.getEventTime().setTimeCreated(new Date());
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
            event.getEventTime().setTimeCreated(new Date());
            eventLoggingService.log(event);

            Thread.sleep(WAIT_BETWEEN_RECORDS);
        }

        java.lang.System.out.println("Total time = " + (java.lang.System.currentTimeMillis() - time));
    }
}