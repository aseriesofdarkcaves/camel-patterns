package com.asodc.camel;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

public class OrderServiceRouteBuilder extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        restConfiguration()
                .component("spark-rest")
                .port(8080)
                .bindingMode(RestBindingMode.json)
                .dataFormatProperty("prettyPrint", "true");

//        rest("/orders")
//                .get("{id}").outType(Order.class)
//                .to("direct:getOrderWithId")
//                .post().type(Order.class)
//                .to("direct:createOrder")
//                .put().type(Order.class)
//                .to("direct:updateOrder")
//                .delete("{id}")
//                .to("direct:deleteOrder");
//
//        from("direct:getOrderWithId")
//                .log("HEADERS:\r\n${headers}\r\nBODY:\r\n${body}")
//                .bean(OrderService.class, "getOrder(${header.id})");
//
//        from("direct:createOrder")
//                .log("HEADERS:\r\n${headers}\r\nBODY:\r\n${body}")
//                .bean(OrderService.class, "createOrder");
//
//        from("direct:updateOrder")
//                .log("HEADERS:\r\n${headers}\r\nBODY:\r\n${body}")
//                .bean(OrderService.class, "updateOrder");
//
//        from("direct:deleteOrder")
//                .log("HEADERS:\r\n${headers}\r\nBODY:\r\n${body}")
//                .bean(OrderService.class, "deleteOrder(${header.id})");

        rest("/orders")
                .get("{id}").outType(Order.class)
                .to("bean:orderService?method=getOrder(${header.id})")
                .post().type(Order.class)
                .to("bean:orderService?method=createOrder")
                .put().type(Order.class)
                .to("bean:orderService?method=updateOrder")
                .delete("{id}")
                .to("bean:orderService?method=deleteOrder(${header.id})");
    }
}
