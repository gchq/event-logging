package event.logging.base.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Properties;
import java.util.regex.Pattern;

public class BuildInfo {

    private static final Logger LOGGER = LoggerFactory.getLogger(BuildInfo.class);
    private static final Pattern LEADING_V_PATTERN = Pattern.compile("^[vV]");

    private static final String BUILD_PROPERTIES = "META-INF/build.properties";
    private static final String BUILD_VERSION;
    private static final String SCHEMA_VERSION;
    private static final Instant BUILD_TIME;

    static {
        final Properties properties = new Properties();
        try {
            // This file will be written by event-logging-base/build.gradle
            properties.load(BuildInfo.class.getClassLoader().getResourceAsStream(BUILD_PROPERTIES));
        } catch (final IOException e) {
            LOGGER.error("Unable to load {}", BUILD_PROPERTIES, e);
        }
        final String buildVersion = properties.getProperty("buildVersion");
        final String schemaVersion = properties.getProperty("schemaVersion");
        final String buildDate = properties.getProperty("buildDate");

        if (schemaVersion == null || schemaVersion.isBlank()) {
            throw new RuntimeException("Schema version is null/blank. It should be set in " + BUILD_PROPERTIES);
        }
        if (buildVersion == null || buildVersion.isBlank()) {
            throw new RuntimeException("Build version is null/blank. It should be set in " + BUILD_PROPERTIES);
        }
        if (buildDate == null || buildDate.isBlank()) {
            throw new RuntimeException("Build date is null/blank. It should be set in " + BUILD_PROPERTIES);
        }

        Instant buildTime = Instant.EPOCH;
        try {
            buildTime = ZonedDateTime.parse(buildDate).toInstant();
        } catch (final RuntimeException e) {
            LOGGER.debug(e.getMessage(), e);
        }
        BUILD_VERSION = LEADING_V_PATTERN.matcher(buildVersion.trim()).replaceFirst("");
        SCHEMA_VERSION = LEADING_V_PATTERN.matcher(schemaVersion.trim()).replaceFirst("");
        BUILD_TIME = buildTime;
    }

    /**
     * @return The version of the event-logging schema that this library was built against
     */
    static String getSchemaVersion() {
        return SCHEMA_VERSION;
    }

    /**
     * @return The version of this library
     */
    static String getBuildVersion() {
        return BUILD_VERSION;
    }

    /**
     * @return The build time of this library
     */
    static Instant getBuildTime() {
        return BUILD_TIME;
    }
}
