package com.asodc.camel;

import org.apache.camel.builder.RouteBuilder;

public class OrderRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("cxfrs:http://localhost:8080" +
                "?resourceClasses=com.asodc.camel.RestOrderService" +
                "&bindingStyle=SimpleConsumer")
                .toD("direct:${headers.operationName}");

        // here we need to decide how the request is handled after it is sent to the direct endpoint
        // pure camel
        from("direct:getOrder")
                .log("GET ORDER");
        from("direct:createOrder")
                .log("GET ORDER");
        from("direct:updateOrder")
                .log("GET ORDER");
        from("direct:deleteOrder")
                .log("GET ORDER");

        // bean usage
//        from("direct:getOrder")
//                .bean("orderService", "getOrder(${headers.id})");
//        from("direct:createOrder")
//                .bean("orderService", "createOrder");
//        from("direct:updateOrder")
//                .bean("orderService", "updateOrder");
//        from("direct:deleteOrder")
//                .bean("orderService", "deleteOrder(${headers.id})");

    }
}
