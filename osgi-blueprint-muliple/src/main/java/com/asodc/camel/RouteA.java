package com.asodc.camel;

import org.apache.camel.builder.RouteBuilder;

public class RouteA extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("timer:timer?period=5000").routeId("Route A")
                .log("Producing a new message...")
                .setHeader("TestHeader").constant("HELLO WORLD!")
                .log("Sending message to Route B...")
                .to("vm:routeB")
                .log("Finished!");
    }
}
