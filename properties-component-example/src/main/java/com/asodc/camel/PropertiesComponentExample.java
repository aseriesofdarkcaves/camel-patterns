package com.asodc.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.main.Main;

public class PropertiesComponentExample {
    private static final String LOGGER = PropertiesComponentExample.class.getCanonicalName();
    private static final String ROUTE_ID = PropertiesComponentExample.class.getCanonicalName();

    public static void main(String... args) throws Exception {
        Main main = new Main();
        CamelContext context = new DefaultCamelContext();
        context.setUseMDCLogging(true);

        PropertiesComponent properties = context.getComponent("properties", PropertiesComponent.class);
        properties.setLocation("classpath:com.asodc.camel.PropertiesComponentExample.properties");

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("timer:timer")
                        .id(ROUTE_ID)
                        .log(LoggingLevel.INFO, LOGGER, "{{message}}");
            }
        });

        main.getCamelContexts().add(context);
        main.run();
    }
}
