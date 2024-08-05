package event.logging.base.jaxb;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

class TestInstantAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestInstantAdapter.class);

    @Test
    void test() {
        final Instant instant = Instant.now();
        final String str = InstantAdapter.printDate(instant);

        LOGGER.info("str: '{}'", str);

        final Instant instant2 = InstantAdapter.parseDate(str);

        // Our standard date format is down to millis only
        assertThat(instant2)
                .isEqualTo(instant.truncatedTo(ChronoUnit.MILLIS));
    }
}
