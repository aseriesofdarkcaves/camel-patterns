package com.asodc.camel.jetty;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

public class RestService extends RouteBuilder {

    private static final String LOGGER = "RestService";
    @Override
    public void configure() throws Exception {
        restConfiguration()
                .component("jetty")
                .bindingMode(RestBindingMode.json)
                .dataFormatProperty("prettyPrint", "true")
                .contextPath("food")
                .port(8080);

        rest("/api").id("food-api")
                .consumes("application/json")
                .produces("application/json")
                .get("/bacon").to("direct:bacon")
                .get("/eggs").to("direct:eggs");

        from("direct:bacon").id("bacon-resource")
                .log(LoggingLevel.INFO, LOGGER, "Somebody wants bacon!")
                .transform().constant("bacon");

        from("direct:eggs").id("eggs-resource")
                .log(LoggingLevel.INFO, LOGGER, "Somebody wants eggs!")
                .transform().constant("eggs");
    }
}
