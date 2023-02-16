package com.asodc.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http4.HttpMethods;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.main.Main;

public class HttpClient {
    public static void main(String... args) throws Exception {
        Main main = new Main();
        CamelContext context = new DefaultCamelContext();
        context.setUseMDCLogging(true);
        context.setStreamCaching(true);

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("timer://timer?repeatCount=1")
                        .removeHeaders("*")
                        .setHeader(Exchange.HTTP_QUERY, constant("format=json&action=parse&origin=*&page=Portal:Teams"))
                        .setHeader(Exchange.HTTP_METHOD, constant(HttpMethods.GET))
                        .to("http4://liquipedia.net/counterstrike/api.php")
                        .convertBodyTo(String.class, "UTF-8")
                        .setHeader("TeamList", jsonpath("$.parse.links[*].['*']"))
                        .log("TeamList: ${headers.TeamList}");
            }
        });

        context.start();
        main.getCamelContexts().add(context);
        main.run();
    }
}
