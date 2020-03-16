package com.asodc.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.main.Main;

public class MessageExchangePattern {
    public static void main(String... args) throws Exception{
        Main main = new Main();
        CamelContext context = new DefaultCamelContext();
        context.setUseMDCLogging(true);

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                // file consumer MEP is InOnly by default
                from("file:data/camel-concepts/mep?noop=true").routeId("defaultInOnly")
                        .convertBodyTo(String.class, "UTF-8")
                        // setBody applied to the IN message of this Exchange
                        .setBody().simple("${body.replaceAll('A', 'B')}")
                        // transform applies to the OUT message of this Exchange
//                        .transform().simple("${body.replaceAll('A', 'B')}")
                        .process(new Processor() {
                            @Override
                            public void process(Exchange exchange) throws Exception {
                                String test = "Test";
                            }
                        })
                        .log("IN BODY: ${body}")
                        .log("OUT BODY: ${out.body}");
            }
        });

        context.start();
        main.getCamelContexts().add(context);
        main.run();
    }
}
