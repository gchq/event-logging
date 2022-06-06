package event.logging.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.kjetland.jackson.jsonSchema.JsonSchemaGenerator;
import event.logging.Events;

import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SchemaGenerator {
    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        JsonSchemaGenerator jsonSchemaGenerator = new JsonSchemaGenerator(mapper);

        // If using JsonSchema to generate HTML5 GUI:
        // JsonSchemaGenerator html5 = new JsonSchemaGenerator(objectMapper, JsonSchemaConfig.html5EnabledSchema() );

        // If you want to configure it manually:
        // JsonSchemaConfig config = JsonSchemaConfig.create(...);
        // JsonSchemaGenerator generator = new JsonSchemaGenerator(objectMapper, config);


        JsonNode jsonSchema = jsonSchemaGenerator.generateJsonSchema(Events.class);

        String jsonSchemaAsString = mapper.writeValueAsString(jsonSchema);

        try (final Writer writer = Files.newBufferedWriter(Paths.get("event-logging-json").resolve("json-schema.json"))) {
            writer.write(jsonSchemaAsString);
        }

        final Path wrapperPath = Paths.get("event-logging-json").resolve("swagger-wrapper.json");
        final String wrapper = new String(Files.readAllBytes(wrapperPath), "UTF-8");
        jsonSchemaAsString = jsonSchemaAsString.replaceAll("\n", "\n  ");
        jsonSchemaAsString = jsonSchemaAsString.replaceAll("#/definitions/", "#/components/schemas/");
        int index = jsonSchemaAsString.indexOf("  \"definitions\" : {\n") + "  \"definitions\" : {\n".length();
        final String definitions = jsonSchemaAsString.substring(index);
        final String result = wrapper + definitions + "\n}\n";
        try (final Writer writer = Files.newBufferedWriter(Paths.get("event-logging-json").resolve("swagger-output.json"))) {
            writer.write(result);
        }
    }
}
