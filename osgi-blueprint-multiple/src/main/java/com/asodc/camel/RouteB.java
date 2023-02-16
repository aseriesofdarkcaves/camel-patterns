package com.asodc.camel;

import org.apache.camel.builder.RouteBuilder;

public class RouteB extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("direct://routeB").routeId("Route B")
                .log("Consuming a new message...")
                .log("Value of TestHeader: ${headers.TestHeader}")
                .log("Finished!");
    }
}
