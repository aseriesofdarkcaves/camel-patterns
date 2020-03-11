package com.asodc.camel;

import org.apache.camel.builder.RouteBuilder;

public class SuperBuilder extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        getContext().addRoutes(new RouteA());
        getContext().addRoutes(new RouteB());
    }
}
