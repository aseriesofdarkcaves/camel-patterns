package com.asodc.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.main.Main;

public class PollEnrichFtp {
    public static void main(String... args) throws Exception{
        Main main = new Main();
        CamelContext context = new DefaultCamelContext();
        context.setUseMDCLogging(true);

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                from("timer://start?delay=1000")
                        .pollEnrich()
                        .simple("ftp://admin@localhost:21/content-enricher?fileName=Dummy.txt" +
                                "&charset=UTF-8" +
                                "&preMove=Staging" +
                                "&move=Success/${file:name.noext}-${date:now:yyyyMMddHHmmssSSS}.${file:ext}" +
                                "&moveFailed=Failed/${file:name.noext}-${date:now:yyyyMMddHHmmssSSS}.${file:ext}"
                        )
                        .log("Finished polling...");
            }
        });

        context.start();
        main.getCamelContexts().add(context);
        main.run();
    }
}
