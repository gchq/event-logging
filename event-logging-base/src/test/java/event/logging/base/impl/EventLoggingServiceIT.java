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
import event.logging.File;
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
import event.logging.util.DeviceUtil;
import event.logging.util.EventLoggingUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Tests the creation of event logging data.
 */
public class EventLoggingServiceIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventLoggingServiceIT.class);

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

    /**
     * Tests the creation of some events using paths.
     *
     * @throws Exception Could be thrown.
     */
    @Test
    void testBasic() throws Exception {
        final long time = java.lang.System.currentTimeMillis();

        final User user = new User();
        user.setId("someuser");

        final AuthenticateEventAction authenticateEventAction = new AuthenticateEventAction();
        authenticateEventAction.setAction(AuthenticateAction.LOGON);
        authenticateEventAction.setAuthenticationEntity(user);

        final Event event = createBasicEvent("LOGIN", "LOGIN");
        event.getEventDetail().setEventAction(authenticateEventAction);

        final EventLoggingService eventLoggingService = getEventLoggingService();

        eventLoggingService.log(event);

        for (int i = 0; i < NUM_OF_RECORDS; i++) {
            event.getEventTime().setTimeCreated(Instant.now());
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
            final int run = i;

            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int j = 1; j <= NUM_OF_RECORDS; j++) {
                            final User user = new User();
                            user.setId("someuser");

                            final AuthenticateEventAction authenticateEventAction = new AuthenticateEventAction();
                            authenticateEventAction.setAction(AuthenticateAction.LOGON);
                            authenticateEventAction.setAuthenticationEntity(user);

                            final Event event = createBasicEvent("LOGIN", "LOGIN");
                            event.getEventDetail().setEventAction(authenticateEventAction);
                            event.getEventTime().setTimeCreated(Instant.now());
                            eventLoggingService.log(event);
                            done.incrementAndGet();
                        }
                    } catch (final Exception e) {
                        throw new RuntimeException(e.getMessage(), e);
                    }
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
        final EventTime eventTime = EventLoggingUtil.createEventTime(Instant.now());
        final Device device = DeviceUtil.createDevice(null, "123.123.123.123");
        final User user = EventLoggingUtil.createUser("someuser");

        final SystemDetail system = new SystemDetail();
        system.setName("Test System");
        system.setEnvironment("Test");

        final EventSource eventSource = new EventSource();
        eventSource.setSystem(system);
        eventSource.setGenerator("JUnit");
        eventSource.setDevice(device);
        eventSource.setUser(user);

        final EventDetail eventDetail = new EventDetail();
        eventDetail.setTypeId(typeId);
        eventDetail.setDescription(description);

        final Event event = getEventLoggingService().createEvent();
        event.setEventTime(eventTime);
        event.setEventSource(eventSource);
        event.setEventDetail(eventDetail);

        return event;
    }

    /**
     * Tests the creation of some events using paths and attributes.
     *
     * @throws Exception Could be thrown.
     */
    @Test
    void testAttributes() throws Exception {
        final long time = java.lang.System.currentTimeMillis();

        final User user = EventLoggingUtil.createUser("someuser");

        final AuthenticateEventAction authenticateEventAction = new AuthenticateEventAction();
        authenticateEventAction.setAction(AuthenticateAction.LOGON);
        authenticateEventAction.setAuthenticationEntity(user);

        final Event event = createBasicEvent("LOGIN", "LOGIN");
        event.getEventDetail().setEventAction(authenticateEventAction);

        for (int i = 0; i < 5; i++) {
            authenticateEventAction.getData().add(EventLoggingUtil.createData("somename" + i, "somevalue" + i));
            authenticateEventAction.getData().add(EventLoggingUtil.createData("someothername" + i, "someothervalue" + i));
        }

        final EventLoggingService eventLoggingService = getEventLoggingService();

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

        final Document document = new Document();
        document.setId("Test Id");
        document.setTitle("Test Title");

        final Outcome outcome = new Outcome();
        outcome.setSuccess(Boolean.TRUE);

        final CreateEventAction createEventAction = new CreateEventAction();
        createEventAction.setOutcome(outcome);
        createEventAction.getObjects().add(document);

        final Event event = createBasicEvent("Create", "Create object");
        event.getEventDetail().setEventAction(createEventAction);

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

//    @Test
//    void testAntiMalware() throws Exception {
//        final long time = java.lang.System.currentTimeMillis();
//
//        final Document document = new Document();
//        document.setId("Test Id");
//        document.setTitle("Test Title");
//
//        final Outcome outcome = new Outcome();
//        outcome.setSuccess(Boolean.TRUE);
//
//        final Software software = new Software();
//        software.setManufacturer("AVToolsInc.");
//        software.setName("Anti-virus");
//
//        final Signature signature = new Signature();
//        signature.setUpdated(Instant.now());
//        signature.setVersion("1.5");
//
//        final AntiMalware value = new AntiMalware();
//        value.setProduct(software);
//        value.setSignature(signature);
//
//        final AntiMalwareEventAction antiMalwareAction = new AntiMalwareEventAction();
//        antiMalwareAction.setScanEngineUpdated(value);
//
//        final Event event = createBasicEvent("Create", "Create object");
//        event.getEventDetail().setAntiMalwareEventAction(antiMalwareAction);
//
//        final EventLoggingService eventLoggingService = getEventLoggingService();
//
//        eventLoggingService.setValidate(true);
//        eventLoggingService.log(event);
//
//        for (int i = 0; i < NUM_OF_RECORDS; i++) {
//            event.getEventTime().setTimeCreated(Instant.now());
//            eventLoggingService.log(event);
//
//            Thread.sleep(WAIT_BETWEEN_RECORDS);
//        }
//
//        java.lang.System.out.println("Total time = " + (java.lang.System.currentTimeMillis() - time));
//    }

    @Test
    void testNastyChars() {
        final Query query = new Query();
        query.setRaw(
                "(?'v?v&amp;?6?#46?R?6????????r????????-w?)::TYPE_TDI| additionalSearchParameters={includeAutoExpIdentifiers=SELECTED, caseInsensitiveMatching=SELECTED, allowWildcards=SELECTED}");

        final SearchEventAction searchEventAction = new SearchEventAction();
        searchEventAction.setQuery(query);

        final Event event = createBasicEvent("Search", "Nasty search");
        event.getEventDetail().setEventAction(searchEventAction);

        final EventLoggingService eventLoggingService = getEventLoggingService();

        eventLoggingService.setValidate(true);
        eventLoggingService.log(event);
    }

    @Test
    void testSmartQuotes() {
        final Query query = new Query();
        query.setRaw("Daves quote");

        final SearchEventAction searcheventAction = new SearchEventAction();
        searcheventAction.setQuery(query);

        final Event event = createBasicEvent("Search", "Nasty search");
        event.getEventDetail().setEventAction(searcheventAction);

        final EventLoggingService eventLoggingService = getEventLoggingService();

        eventLoggingService.setValidate(true);
        eventLoggingService.log(event);
    }

    @Test
    void testNastyChars2() {
        final Query query = new Query();

        final StringBuilder sb = new StringBuilder();
        for (int i = Character.MIN_VALUE; i <= Character.MAX_VALUE; i++) {
            if (i % 100 == 0) {
                sb.append('\n');
            }
            sb.append((char) i);
        }

        query.setRaw(sb.toString());

        final SearchEventAction searcheventAction = new SearchEventAction();
        searcheventAction.setQuery(query);

        final Event event = createBasicEvent("Search", "Nasty search");
        event.getEventDetail().setEventAction(searcheventAction);

        final EventLoggingService eventLoggingService = getEventLoggingService();

        eventLoggingService.setValidate(true);
        eventLoggingService.log(event);
    }

    @Test
    void testSendReceive() {

        final Event event = createBasicEvent("Send", "Send event");

        final User sourceUser = new User();
        sourceUser.setId("sourceUserId");

        final Device sourceDevice = new Device();
        sourceDevice.setHostName("sourceHost");

        final User destUser = new User();
        destUser.setId("destUserId");

        final Device destDevice = new Device();
        destDevice.setHostName("destHost");

        final Source source = new Source();
        source.getEndpoints().add(sourceUser);
        source.getEndpoints().add(sourceDevice);

        final Destination destination = new Destination();
        destination.getEndpoints().add(destUser);
        destination.getEndpoints().add(destDevice);

        final SendEventAction sendEventAction = new SendEventAction();
        sendEventAction.setSource(source);
        sendEventAction.setDestination(destination);

        event.getEventDetail().setEventAction(sendEventAction);

        final EventLoggingService eventLoggingService = getEventLoggingService();

        eventLoggingService.setValidate(true);
        eventLoggingService.log(event);

    }

    @Test
    void testImport() {

        final Event event = createBasicEvent("Import", "Import event");

        final MultiObject source = new MultiObject();

        final File sourceFile = new File();
        sourceFile.setName("SourceFile");

        source.getObjects().add(sourceFile);

        final MultiObject dest = new MultiObject();

        final File destFile = new File();
        destFile.setName("DestFile");
        dest.getObjects().add(destFile);

        final ImportEventAction importElmAction = new ImportEventAction();
        importElmAction.setSource(source);
        importElmAction.setDestination(dest);

        event.getEventDetail().setEventAction(importElmAction);

        final EventLoggingService eventLoggingService = getEventLoggingService();

        eventLoggingService.setValidate(true);
        eventLoggingService.log(event);
    }

    @Test
    void testCreateQuery() {

        final Event event = createBasicEvent("Query", "Simple query");

        final User sourceUser = new User();
        sourceUser.setId("sourceUserId");

        final Device sourceDevice = new Device();
        sourceDevice.setHostName("sourceHost");

        final User destUser = new User();
        destUser.setId("destUserId");

        final Device destDevice = new Device();
        destDevice.setHostName("destHost");

        final Query query = new Query();
        query.setRaw("my query string!");

        final SearchEventAction searcheventAction = new SearchEventAction();
        searcheventAction.setQuery(query);

        event.getEventDetail().setEventAction(searcheventAction);

        final EventLoggingService eventLoggingService = getEventLoggingService();

        eventLoggingService.setValidate(true);
        eventLoggingService.log(event);
    }

    @Test
    void testCreateExportAdvancedQuery() {

        final Event event = createBasicEvent("Export-Criteria", "Export-Criteria-Search");

        final Term term = new Term();
        term.setName("date_modified");
        term.setCondition(TermCondition.GREATER_THAN);
        term.setValue("56789");

        final AdvancedQuery advancedQuery = new AdvancedQuery();
        advancedQuery.getQueryItems().add(term);

        final Query query = new Query();
        query.setAdvanced(advancedQuery);

        final Criteria criteria = new Criteria();
        criteria.setQuery(query);

        final MultiObject source = new MultiObject();

        source.getObjects().add(criteria);

        final ExportEventAction exportEventAction = new ExportEventAction();
        exportEventAction.setSource(source);

        final Data data = EventLoggingUtil.createData("MyName", "MyValue");

        exportEventAction.getData().add(data);

        event.getEventDetail().setEventAction(exportEventAction);

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

        final Document document = new Document();
        document.setId("Test Id");
        document.setTitle("Test Title");

        final Outcome outcome = new Outcome();
        outcome.setSuccess(Boolean.TRUE);

        final CreateEventAction createEventAction = new CreateEventAction();
        createEventAction.setOutcome(outcome);
        createEventAction.getObjects().add(document);

        final Event event = createBasicEvent("Create", "Create object");
        event.getEventDetail().setEventAction(createEventAction);

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

        final Document document = new Document();
        document.setId("Test Id");
        document.setTitle("Test Title");

        final Outcome outcome = new Outcome();
        outcome.setSuccess(Boolean.TRUE);

        final CreateEventAction createEventAction = new CreateEventAction();
        createEventAction.setOutcome(outcome);
        createEventAction.getObjects().add(document);

        final Event event = createBasicEvent("Create", "Create object");
        event.getEventDetail().setEventAction(createEventAction);

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

//    private Node buildPayload() throws JAXBException, ParserConfigurationException {
//
//        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//        dbf.setNamespaceAware(false);
//        final DocumentBuilder db = dbf.newDocumentBuilder();
//        final org.w3c.dom.Document doc = db.newDocument();
//
//        final JAXBContext jaxbContext = JAXBContext.newInstance(Payload.class);
//
//        final Payload payload = new Payload();
//        payload.setValue("thisIsMyValue");
//
//        jaxbContext.createMarshaller().marshal(payload, java.lang.System.out);
//
//        jaxbContext.createMarshaller().marshal(payload, doc);
//
//        final Element docElm = doc.getDocumentElement();
//
//        return docElm;
//    }
}
