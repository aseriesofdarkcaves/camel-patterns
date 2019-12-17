package com.asodc.camel;

import org.apache.camel.builder.RouteBuilder;

public class CxfContractFirst extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("cxf:bean:orderEndpoint")
                .id("orderEndpoint")
                .to("seda:incomingOrders")
                .transform().constant("OK")
                .log("Done!");
    }
}
