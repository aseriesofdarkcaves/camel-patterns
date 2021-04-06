package com.asodc.camel;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

public class OsgiBootstrapBlueprint extends RouteBuilder {
    private static final String LOGGER = OsgiBootstrapBlueprint.class.getCanonicalName();
    private static final String LOG_MESSAGE = "{{log.message}}";
    private static final String ROUTE_ID = OsgiBootstrapBlueprint.class.getCanonicalName();

    @Override
    public void configure() throws Exception {
        from("timer:timer?period=5000")
                .log(LoggingLevel.INFO, LOGGER, LOG_MESSAGE);
    }
}
