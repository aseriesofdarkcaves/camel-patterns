package com.asodc.camel.jetty;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.main.Main;

public class RestServiceMain {
    public static void main (String... args) throws Exception {
        Main main = new Main();
        CamelContext context = new DefaultCamelContext();
        context.setUseMDCLogging(true);
        context.setDelayer(500L);
        context.addRoutes(new RestService());
        context.start();
        main.getCamelContexts().add(context);
        main.run();
    }
}
