package com.asodc.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.main.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class MdcLogging {
    private static final String LOGGING_CATEGORY = MdcLogging.class.getCanonicalName();
    private static final String MY_MDC_KEY = "myMdcKey";
    private static final String LOG_MESSAGE = "<-- Can you see the MDC value?";

    public static void main(String... args) throws Exception {
        // This is here to enable logging at the core application level
        Logger applicatonLogger = LoggerFactory.getLogger(MdcLogging.class);
        applicatonLogger.info(LOG_MESSAGE);

        Main main = new Main();
        CamelContext context = new DefaultCamelContext();

        // MDC is thread-local only
        context.setUseMDCLogging(true);
        MDC.put(MY_MDC_KEY, "We're in MdCLogging.main()!");
        applicatonLogger.info(LOG_MESSAGE);

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                MDC.put(MY_MDC_KEY, "We're in com.asodc.camel.MdcLogging.main.configure()!");
                applicatonLogger.info(LOG_MESSAGE);
                from("timer:route1Timer?period=3000")
                        .id("Route1")
                        // This a way of injecting MDC values into a route:
                        .process(new Processor() {
                            @Override
                            public void process(Exchange exchange) throws Exception {
                                MDC.put(MY_MDC_KEY, "We're in com.asodc.camel.MdcLogging.main.configure.process()!");
                            }
                        })
                        // This is Camel Java DSL logging
                        .log(LoggingLevel.INFO, LOGGING_CATEGORY, LOG_MESSAGE);

                from("timer:route2Timer?period=3000")
                        .id("Route2")
                        // Let's see if MDC keys set earlier can be referenced in a different route...
                        .log(LoggingLevel.INFO, LOGGING_CATEGORY, LOG_MESSAGE);
            }
        });

        context.start();
        main.getCamelContexts().add(context);
        main.run();
    }
}
