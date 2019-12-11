package com.asodc.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.main.Main;

public class FtpConsumer {
    private static final String LOGGER = FtpConsumer.class.getCanonicalName();
    private static final String LOG_MESSAGE = "HEADERS:\r\n${headers}\r\nBODY:\r\n${body}";
    private static final String ROUTE_ID = FtpConsumer.class.getCanonicalName();

    public static void main(String... args) throws Exception {
        Main main = new Main();
        CamelContext context = new DefaultCamelContext();
        context.setUseMDCLogging(true);

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("ftp:localhost?username=username&password=password")
                        .id(ROUTE_ID)
                        .log(LoggingLevel.INFO, LOGGER, LOG_MESSAGE)
                        .to("file:data/ftp-consumer");
            }
        });

        context.start();
        main.getCamelContexts().add(context);
        main.run();
    }
}
