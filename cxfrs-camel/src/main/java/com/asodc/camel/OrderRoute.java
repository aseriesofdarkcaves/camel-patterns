package com.asodc.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.jaxrs.CamelResourceProvider;
import org.apache.camel.impl.CompositeRegistry;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.SimpleRegistry;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.provider.SourceProvider;

import java.util.ArrayList;
import java.util.List;

public class OrderRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        final CamelContext camelContext = getContext();
        camelContext.setUseMDCLogging(true);
        camelContext.setStreamCaching(true);

        // TODO: figure out why registry is set up like this
        final SimpleRegistry registry = new SimpleRegistry();
        final CompositeRegistry compositeRegistry = new CompositeRegistry();
        compositeRegistry.addRegistry(camelContext.getRegistry());
        compositeRegistry.addRegistry(registry);
        ((DefaultCamelContext) camelContext).setRegistry(compositeRegistry);

        List<Object> providers = new ArrayList<>();
        providers.add(new com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider());
        registry.put("providers", providers);

        // TODO: learn how to correctly configure a CXFRS service
        JAXRSServerFactoryBean jaxrsServerFactoryBean = new JAXRSServerFactoryBean();
        jaxrsServerFactoryBean.setServiceClass(RestOrderService.class);
        jaxrsServerFactoryBean.setResourceProvider(RestOrderService.class, new CamelResourceProvider(RestOrderService.class));
        // avoid JAXBException in runtime
        jaxrsServerFactoryBean.setProvider(new SourceProvider());

        // TODO: how to instantiate the bean? should I put it in the registry?
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
