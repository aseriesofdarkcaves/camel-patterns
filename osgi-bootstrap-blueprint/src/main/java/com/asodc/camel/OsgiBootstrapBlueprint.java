package com.asodc.camel;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

public class OsgiBootstrapBlueprint extends RouteBuilder {
    private static final String LOGGER = OsgiBootstrapBlueprint.class.getCanonicalName();
    private static final String LOG_MESSAGE = "HEADERS:\r\n${headers}\r\nBODY:\r\n${body}";
    private static final String ROUTE_ID = OsgiBootstrapBlueprint.class.getCanonicalName();

    @Override
    public void configure() throws Exception {
        from("timer:timer?period=5000")
                .id(ROUTE_ID)
                .log(LoggingLevel.INFO, LOGGER, LOG_MESSAGE);
    }
}
