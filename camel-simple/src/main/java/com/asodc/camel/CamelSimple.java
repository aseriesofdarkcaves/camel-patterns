package com.asodc.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.main.Main;

public class CamelSimple {
    public static void main(String... args) throws Exception {
        Main main = new Main();
        CamelContext context = new DefaultCamelContext();
        context.setUseMDCLogging(true);

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("file:data/file-consumer/inbox?noop=true")
                        .id("OnglFileRoute")
                        .log("${body.class}")
                        .log("${body.class.name}");
            }
        });

        context.start();
        main.getCamelContexts().add(context);
        main.run();
    }
}
