package com.asodc.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.main.Main;

public class CamelLogger {
    private static final String LOGGER = CamelLogger.class.getCanonicalName();
    private static final String LOG_MESSAGE = "HEADERS:\r\n${headers}\r\nBODY:\r\n${body}";
    private static final String ROUTE_ID = CamelLogger.class.getCanonicalName();

    public static void main(String... args) throws Exception {
        Main main = new Main();
        CamelContext context = new DefaultCamelContext();
        context.setUseMDCLogging(true);

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("timer:myTimer?delay=3000")
                        .id(ROUTE_ID)
                        .log(LoggingLevel.INFO, LOGGER, "Test!");
            }
        });

        context.start();
        main.getCamelContexts().add(context);
        main.run();
    }
}
