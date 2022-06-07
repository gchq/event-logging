package event.logging.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.kjetland.jackson.jsonSchema.JsonSchemaGenerator;
import event.logging.Events;

import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SchemaGenerator {

    private static final String MODULE_NAME = "event-logging-json";

    public static void main(String[] args) throws Exception {
        final ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
                .configure(SerializationFeature.INDENT_OUTPUT, true)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);

        final JsonSchemaGenerator jsonSchemaGenerator = new JsonSchemaGenerator(mapper);

        // If using JsonSchema to generate HTML5 GUI:
        // JsonSchemaGenerator html5 = new JsonSchemaGenerator(objectMapper, JsonSchemaConfig.html5EnabledSchema() );

        // If you want to configure it manually:
        // JsonSchemaConfig config = JsonSchemaConfig.create(...);
        // JsonSchemaGenerator generator = new JsonSchemaGenerator(objectMapper, config);

        final JsonNode jsonSchema = jsonSchemaGenerator.generateJsonSchema(Events.class);

        String jsonSchemaAsString = mapper.writeValueAsString(jsonSchema);

        Path rootDir = Paths.get(".").normalize().toAbsolutePath();
        if (!rootDir.endsWith(MODULE_NAME)) {
            //running from within the module so go up one
            rootDir = rootDir.resolve(MODULE_NAME).normalize().toAbsolutePath();
        }
        System.out.println("Using root directory: " + rootDir);

        final Path schemaPath = rootDir.resolve("json-schema.json");
        final Path swaggerSpecPath = rootDir.resolve("swagger-spec.json");
        final Path wrapperPath = rootDir.resolve("swagger-wrapper.json");

        System.out.println("Writing schema file: " + schemaPath.toAbsolutePath().normalize());
        try (final Writer writer = Files.newBufferedWriter(schemaPath)) {
            writer.write(jsonSchemaAsString);
        }

        System.out.println("Reading wrapper file: " + wrapperPath.toAbsolutePath().normalize());
        final String wrapper = new String(Files.readAllBytes(wrapperPath), StandardCharsets.UTF_8);

        jsonSchemaAsString = jsonSchemaAsString.replaceAll("\n", "\n  ")
                .replaceAll("#/definitions/", "#/components/schemas/");
        int index = jsonSchemaAsString.indexOf("  \"definitions\" : {\n") + "  \"definitions\" : {\n".length();
        final String definitions = jsonSchemaAsString.substring(index);
        final String result = wrapper + definitions + "\n}\n";

        System.out.println("Writing swagger spec file: " + swaggerSpecPath.toAbsolutePath().normalize());

        try (final Writer writer = Files.newBufferedWriter(swaggerSpecPath)) {
            writer.write(result);
        }
        System.out.println("Done");
    }
}
