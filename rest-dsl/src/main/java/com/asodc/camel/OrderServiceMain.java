package com.asodc.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.SimpleRegistry;
import org.apache.camel.main.Main;

public class OrderServiceMain {
    public static void main(String... args) throws Exception {
        OrderService orderService = new OrderServiceImplementation();
        SimpleRegistry registry = new SimpleRegistry();
        registry.put("orderService", orderService);

        Main main = new Main();
        CamelContext context = new DefaultCamelContext();
        context.setUseMDCLogging(true);
        context.getGlobalOptions().put("CamelJacksonEnableTypeConverter", "true");
        context.getGlobalOptions().put("CamelJacksonTypeConverterToPojo", "true");
        context.addRoutes(new OrderServiceRouteBuilder());
        context.start();

        main.getCamelContexts().add(context);
        main.bind("orderService", orderService);
        main.run();
    }
}
