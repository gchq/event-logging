module event.logging.api {
    requires java.xml.bind;
    requires java.xml.ws.annotation;
    requires slf4j.api;

    exports event.logging;
    exports event.logging.util;
}
