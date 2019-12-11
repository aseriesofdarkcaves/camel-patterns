package com.asodc.camel;

import com.asodc.camel.model.Item;
import com.asodc.camel.model.Order;
import org.apache.camel.CamelContext;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.main.Main;

public class TransformWithJackson {
    private static final String LOGGER = TransformWithJackson.class.getCanonicalName();
    private static final String LOG_MESSAGE = "HEADERS:\r\n${headers}\r\nBODY:\r\n${body}";
    private static final String ROUTE_ID = TransformWithJackson.class.getCanonicalName();

    public static void main(String... args) throws Exception {
        Main main = new Main();
        CamelContext context = new DefaultCamelContext();
        context.setUseMDCLogging(true);

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                JacksonDataFormat jsonFormat = new JacksonDataFormat();

                Item item1 = new Item("12345");
                item1.setItemDescription("Can of com.asodc.camel.beans");

                Item item2 = new Item("11111");
                item2.setItemDescription("Beer");

                Order order = new Order("21");
                order.addItem(item1);
                order.addItem(item2);

                from("timer:timer?repeatCount=1")
                        .id(ROUTE_ID)
                        .setBody().constant(order)
                        .log(LoggingLevel.INFO, LOGGER, LOG_MESSAGE)
                        .marshal(jsonFormat)
                        .log(LoggingLevel.INFO, LOGGER, LOG_MESSAGE);
            }
        });

        context.start();
        main.getCamelContexts().add(context);
        main.run();
    }
}
