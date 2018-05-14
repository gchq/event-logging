module event.logging.api {
    exports event.logging;
    exports event.logging.impl;
    exports event.logging.util;
    requires java.xml.bind;
    requires slf4j.api;
}
