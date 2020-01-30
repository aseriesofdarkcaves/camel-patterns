package com.asodc.camel;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.CompositeRegistry;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.SimpleRegistry;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;

import java.util.ArrayList;
import java.util.List;

public class OrderRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        final CamelContext camelContext = getContext();
        camelContext.setUseMDCLogging(true);
        camelContext.setStreamCaching(true);

        // TODO: figure out why registry is set up like this
        final CompositeRegistry compositeRegistry = new CompositeRegistry();
        final SimpleRegistry registry = new SimpleRegistry();
        compositeRegistry.addRegistry(camelContext.getRegistry());
        compositeRegistry.addRegistry(registry);
        ((DefaultCamelContext) camelContext).setRegistry(compositeRegistry);

        // instanciate service and place it in the registry
        OrderServiceImplementation orderService = new OrderServiceImplementation();
        registry.put("orderService", orderService);

        // TODO: is this correct?
        List<Object> providers = new ArrayList<>();
        providers.add(new JacksonJaxbJsonProvider());
//        providers.add(new JacksonJsonProvider());
        registry.put("providers", providers);

        // TODO: learn how to correctly configure a CXFRS service
        JAXRSServerFactoryBean jaxrsServerFactoryBean = new JAXRSServerFactoryBean();
        jaxrsServerFactoryBean.setProvider(JacksonJsonProvider.class);

        from("cxfrs:http://localhost:8080" +
                "?resourceClasses=com.asodc.camel.RestOrderService" +
                "&bindingStyle=SimpleConsumer")
                .toD("direct:${headers.operationName}");

        from("direct:getOrder")
                .log("GET ORDER")
                .bean("orderService", "getOrder(${headers.id})");

        from("direct:createOrder")
                .log("POST ORDER")
                .bean("orderService", "createOrder");

        from("direct:updateOrder")
                .log("PUT ORDER")
                .bean("orderService", "updateOrder");

        from("direct:deleteOrder")
                .log("DELETE ORDER")
                .bean("orderService", "deleteOrder(${headers.id})");
    }
}
