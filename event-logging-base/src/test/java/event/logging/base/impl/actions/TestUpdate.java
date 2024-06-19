package event.logging.base.impl.actions;

import event.logging.Changes;
import event.logging.File;
import event.logging.MultiObject;
import event.logging.UpdateEventAction;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestUpdate extends AbstractTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestUpdate.class);

    @Test
    void test() {

        final UpdateEventAction updateEventAction = UpdateEventAction.builder()
                .withAfter(MultiObject.builder()
                        .addFile(File.builder()
                                .withPath("/tmp/foo.zip")
                                .build())
                        .build())
                .withChanges(Changes.builder()
                        .withAdd(MultiObject.builder()
                                .addFile(File.builder()
                                        .withPath("/x.txt")
                                        .build())
                                .addFile(File.builder()
                                        .withPath("/y.txt")
                                        .build())
                                .build())
                        .withRemove(MultiObject.builder()
                                .addFile(File.builder()
                                        .withPath("a.txt")
                                        .build())
                                .build())
                        .build())
                .build();
        final String xml = getXML(createEvent(updateEventAction));

        LOGGER.info("updateEventAction: {}", xml);

        Assertions.assertThat(xml.replaceAll(">\\s*<", "><"))
                .contains("Update")
                .contains("<Update><After><File><Path>/tmp/foo.zip</Path></File></After>")
                .contains("<Changes><Add><File><Path>/x.txt</Path></File><File><Path>/y.txt</Path></File></Add>")
                .contains("<Remove><File><Path>a.txt</Path></File></Remove></Changes>");
    }
}
