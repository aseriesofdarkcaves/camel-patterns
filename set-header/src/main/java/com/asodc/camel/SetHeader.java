package com.asodc.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.main.Main;

public class SetHeader {
    public static void main(String... args) throws Exception{
        Main main = new Main();
        CamelContext context = new DefaultCamelContext();
        context.setUseMDCLogging(true);

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                from("timer:start?repeatCount=1")
                        .setHeader("SetHeader-1").constant("Header set via fluent builder chain")
                        .setHeader("SetHeader-2", constant("Header set via expression"))
                        .log("Headers:\r\n${headers}\r\n\r\nBody:\r\n${body}");
            }
        });

        context.start();
        main.getCamelContexts().add(context);
        main.run();
    }
}
