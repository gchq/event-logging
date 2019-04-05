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

import event.logging.AuthenticateAction;
import event.logging.BaseAntiMalware;
import event.logging.Criteria;
import event.logging.Data;
import event.logging.Device;
import event.logging.Document;
import event.logging.Event;
import event.logging.base.EventLoggingService;
import event.logging.Export;
import event.logging.File;
import event.logging.Import;
import event.logging.MultiObject;
import event.logging.ObjectOutcome;
import event.logging.Outcome;
import event.logging.base.Payload;
import event.logging.Query;
import event.logging.Search;
import event.logging.SendReceive;
import event.logging.Software;
import event.logging.System;
import event.logging.Term;
import event.logging.TermCondition;
import event.logging.User;
import event.logging.base.util.DeviceUtil;
import event.logging.base.util.EventLoggingUtil;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Tests the creation of event logging data.
 */
public class EventLoggingServiceIT {
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

    /**
     * Tests the creation of some events using paths.
     *
     * @throws Exception Could be thrown.
     */
    @Test
    public void testBasic() throws Exception {
        final long time = java.lang.System.currentTimeMillis();

        final User user = new User();
        user.setId("someuser");

        final Event.EventDetail.Authenticate authenticate = new Event.EventDetail.Authenticate();
        authenticate.setAction(AuthenticateAction.LOGON);
        authenticate.setUser(user);

        final Event event = createBasicEvent("LOGIN", "LOGIN");
        event.getEventDetail().setAuthenticate(authenticate);

        final EventLoggingService eventLoggingService = getEventLoggingService();

//        Event event2 = Event.builder()
//                .withEventSource()
//                    .withClient()
//                        .withHostName("xxx")
//                        .end()
//                    .withSystem()
//                        .withName("MySystem")
//                        .withEnvironment("OPS")
//                        .end()
//                    .withUser()
//                        .withId("user1")
//                        .end()
//                    .end()
//                .withEventDetail()
//                    .withAuthenticate()
//                        .withAction(AuthenticateAction.LOGON)
//                        .withUser()
//                            .withId("user1")
//                            .end()
//                        .end()
//                .end()
//                .build();


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
    public void testMultiThread() throws Exception {
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

                            final Event.EventDetail.Authenticate authenticate = new Event.EventDetail.Authenticate();
                            authenticate.setAction(AuthenticateAction.LOGON);
                            authenticate.setUser(user);

                            final Event event = createBasicEvent("LOGIN", "LOGIN");
                            event.getEventDetail().setAuthenticate(authenticate);
                            event.getEventTime().setTimeCreated(new Date());
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
        final Event.EventTime eventTime = EventLoggingUtil.createEventTime(new Date());
        final Device device = DeviceUtil.createDevice(null, "123.123.123.123");
        final User user = EventLoggingUtil.createUser("someuser");

        final System system = new System();
        system.setName("Test System");
        system.setEnvironment("Test");

        final Event.EventSource eventSource = new Event.EventSource();
        eventSource.setSystem(system);
        eventSource.setGenerator("JUnit");
        eventSource.setDevice(device);
        eventSource.setUser(user);

        final Event.EventDetail eventDetail = new Event.EventDetail();
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
    public void testAttributes() throws Exception {
        final long time = java.lang.System.currentTimeMillis();

        final User user = EventLoggingUtil.createUser("someuser");

        final Event.EventDetail.Authenticate authenticate = new Event.EventDetail.Authenticate();
        authenticate.setAction(AuthenticateAction.LOGON);
        authenticate.setUser(user);

        final Event event = createBasicEvent("LOGIN", "LOGIN");
        event.getEventDetail().setAuthenticate(authenticate);

        for (int i = 0; i < 5; i++) {
            authenticate.getData().add(EventLoggingUtil.createData("somename" + i, "somevalue" + i));
            authenticate.getData().add(EventLoggingUtil.createData("someothername" + i, "someothervalue" + i));
        }

        final EventLoggingService eventLoggingService = getEventLoggingService();

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
    public void testCreateEvent() throws Exception {
        final long time = java.lang.System.currentTimeMillis();

        final Document document = new Document();
        document.setId("Test Id");
        document.setTitle("Test Title");

        final Outcome outcome = new Outcome();
        outcome.setSuccess(Boolean.TRUE);

        final ObjectOutcome objectOutcome = new ObjectOutcome();
        objectOutcome.setOutcome(outcome);
        objectOutcome.getObjects().add(document);

        final Event event = createBasicEvent("Create", "Create object");
        event.getEventDetail().setCreate(objectOutcome);

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
    public void testAntiMalware() throws Exception {
        final long time = java.lang.System.currentTimeMillis();

        final Document document = new Document();
        document.setId("Test Id");
        document.setTitle("Test Title");

        final Outcome outcome = new Outcome();
        outcome.setSuccess(Boolean.TRUE);

        final Software software = new Software();
        software.setManufacturer("AVToolsInc.");
        software.setName("Anti-virus");

        final BaseAntiMalware.Signature signature = new BaseAntiMalware.Signature();
        signature.setUpdated(new Date());
        signature.setVersion("1.5");

        final event.logging.AntiMalware value = new event.logging.AntiMalware();
        value.setProduct(software);
        value.setSignature(signature);

        final Event.EventDetail.AntiMalware antiMalware = new Event.EventDetail.AntiMalware();
        antiMalware.setScanEngineUpdated(value);

        final Event event = createBasicEvent("Create", "Create object");
        event.getEventDetail().setAntiMalware(antiMalware);

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
    public void testNastyChars() throws Exception {
        final Query query = new Query();
        query.setRaw(
                "(?'v?v&amp;?6?#46?R?6????????r????????-w?)::TYPE_TDI| additionalSearchParameters={includeAutoExpIdentifiers=SELECTED, caseInsensitiveMatching=SELECTED, allowWildcards=SELECTED}");

        final Search search = new Search();
        search.setQuery(query);

        final Event event = createBasicEvent("Search", "Nasty search");
        event.getEventDetail().setSearch(search);

        final EventLoggingService eventLoggingService = getEventLoggingService();

        eventLoggingService.setValidate(true);
        eventLoggingService.log(event);
    }

    @Test
    public void testSmartQuotes() throws Exception {
        final Query query = new Query();
        query.setRaw("Daves quote");

        final Search search = new Search();
        search.setQuery(query);

        final Event event = createBasicEvent("Search", "Nasty search");
        event.getEventDetail().setSearch(search);

        final EventLoggingService eventLoggingService = getEventLoggingService();

        eventLoggingService.setValidate(true);
        eventLoggingService.log(event);
    }

    @Test
    public void testNastyChars2() throws Exception {
        final Query query = new Query();

        final StringBuilder sb = new StringBuilder();
        for (int i = Character.MIN_VALUE; i <= Character.MAX_VALUE; i++) {
            if (i % 100 == 0) {
                sb.append('\n');
            }
            sb.append((char) i);
        }

        query.setRaw(sb.toString());

        final Search search = new Search();
        search.setQuery(query);

        final Event event = createBasicEvent("Search", "Nasty search");
        event.getEventDetail().setSearch(search);

        final EventLoggingService eventLoggingService = getEventLoggingService();

        eventLoggingService.setValidate(true);
        eventLoggingService.log(event);
    }

    @Test
    public void testSendReceive() throws Exception {

        final Event event = createBasicEvent("Send", "Send event");

        final User sourceUser = new User();
        sourceUser.setId("sourceUserId");

        final Device sourceDevice = new Device();
        sourceDevice.setHostName("sourceHost");

        final User destUser = new User();
        destUser.setId("destUserId");

        final Device destDevice = new Device();
        destDevice.setHostName("destHost");

        final SendReceive.Source source = new SendReceive.Source();
        source.getUserOrDevice().add(sourceUser);
        source.getUserOrDevice().add(sourceDevice);

        final SendReceive.Destination destination = new SendReceive.Destination();
        destination.getUserOrDevice().add(destUser);
        destination.getUserOrDevice().add(destDevice);

        final SendReceive sendReceive = new SendReceive();
        sendReceive.setSource(source);
        sendReceive.setDestination(destination);

        event.getEventDetail().setSend(sendReceive);

        final EventLoggingService eventLoggingService = getEventLoggingService();

        eventLoggingService.setValidate(true);
        eventLoggingService.log(event);

    }

    @Test
    public void testImport() throws Exception {

        final Event event = createBasicEvent("Import", "Import event");

        final MultiObject source = new MultiObject();

        final File sourceFile = new File();
        sourceFile.setName("SourceFile");

        source.getObjects().add(sourceFile);

        final MultiObject dest = new MultiObject();

        final File destFile = new File();
        destFile.setName("DestFile");
        dest.getObjects().add(destFile);

        final Import importElm = new Import();
        importElm.setSource(source);
        importElm.setDestination(dest);

        event.getEventDetail().setImport(importElm);

        final EventLoggingService eventLoggingService = getEventLoggingService();

        eventLoggingService.setValidate(true);
        eventLoggingService.log(event);
    }

    @Test
    public void testCreateQuery() throws Exception {

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

        final Search search = new Search();
        search.setQuery(query);

        event.getEventDetail().setSearch(search);

        final EventLoggingService eventLoggingService = getEventLoggingService();

        eventLoggingService.setValidate(true);
        eventLoggingService.log(event);
    }

    @Test
    public void testCreateExportAdvancedQuery() throws Exception {

        final Event event = createBasicEvent("Export-Criteria", "Export-Criteria-Search");

        final Term term = new Term();
        term.setName("date_modified");
        term.setCondition(TermCondition.GREATER_THAN);
        term.setValue("56789");

        final Query.Advanced advancedQuery = new Query.Advanced();
        advancedQuery.getAdvancedQueryItems().add(term);

        final Query query = new Query();
        query.setAdvanced(advancedQuery);

        final Criteria criteria = new Criteria();
        criteria.setQuery(query);

        final MultiObject source = new MultiObject();

        source.getObjects().add(criteria);

        final Export export = new Export();
        export.setSource(source);

        final Data data = EventLoggingUtil.createData("MyName", "MyValue");

        export.getData().add(data);

        event.getEventDetail().setExport(export);

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
    public void testMetaWithJSON() throws Exception {
        final long time = java.lang.System.currentTimeMillis();

        final Document document = new Document();
        document.setId("Test Id");
        document.setTitle("Test Title");

        final Outcome outcome = new Outcome();
        outcome.setSuccess(Boolean.TRUE);

        final ObjectOutcome objectOutcome = new ObjectOutcome();
        objectOutcome.setOutcome(outcome);
        objectOutcome.getObjects().add(document);

        final Event event = createBasicEvent("Create", "Create object");
        event.getEventDetail().setCreate(objectOutcome);

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
    public void testMetaWithXML() throws Exception {
        final long time = java.lang.System.currentTimeMillis();

        final Document document = new Document();
        document.setId("Test Id");
        document.setTitle("Test Title");

        final Outcome outcome = new Outcome();
        outcome.setSuccess(Boolean.TRUE);

        final ObjectOutcome objectOutcome = new ObjectOutcome();
        objectOutcome.setOutcome(outcome);
        objectOutcome.getObjects().add(document);

        final Event event = createBasicEvent("Create", "Create object");
        event.getEventDetail().setCreate(objectOutcome);

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

    private Node buildPayload() throws JAXBException, ParserConfigurationException {

        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(false);
        final DocumentBuilder db = dbf.newDocumentBuilder();
        final org.w3c.dom.Document doc = db.newDocument();

        final JAXBContext jaxbContext = JAXBContext.newInstance(Payload.class);

        final Payload payload = new Payload();
        payload.setValue("thisIsMyValue");

        jaxbContext.createMarshaller().marshal(payload, java.lang.System.out);

        jaxbContext.createMarshaller().marshal(payload, doc);

        final Element docElm = doc.getDocumentElement();

        return docElm;
    }
}