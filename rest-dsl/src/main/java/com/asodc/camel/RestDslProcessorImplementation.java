package com.asodc.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.main.Main;
import org.apache.camel.model.rest.RestBindingMode;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This is too low-level for any practical usage, but is good as an example on how NOT to do REST with Camel.
 */
public class RestDslProcessorImplementation {
    public static void main(String... args) throws Exception {
        Map<Integer, Order> orders = new HashMap<>();
        final AtomicInteger idGenerator = new AtomicInteger();

        Main main = new Main();

        CamelContext context = new DefaultCamelContext();
        context.setUseMDCLogging(true);
        context.getGlobalOptions().put("CamelJacksonEnableTypeConverter", "true");
        context.getGlobalOptions().put("CamelJacksonTypeConverterToPojo", "true");
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                restConfiguration()
                        .component("spark-rest")
                        .host("localhost")
                        .port("8080")
                        .bindingMode(RestBindingMode.json)
                        .skipBindingOnErrorCode(false)
                        .dataFormatProperty("prettyPrint", "true");

                rest("/orders")
                        .get("{id}").outType(Order.class)
                        .to("direct:getOrderWithId")
                        .post().type(Order.class).outType(Order.class)
                        .to("direct:createOrder")
                        .put("{id}").type(Order.class).outType(Order.class)
                        .to("direct:updateOrder")
                        .delete("{id}")
                        .to("direct:deleteOrder");

                from("direct:getOrderWithId")
                        .log("Incoming GET request for Order with ID: ${headers.id}...")
                        .process(new Processor() {
                            @Override
                            public void process(Exchange exchange) throws Exception {
                                int idToGet = exchange.getIn().getHeader("id", int.class);
                                Order orderToReturn = orders.get(idToGet);
                                exchange.getIn().setBody(orderToReturn, Order.class);
                                // TODO: handle not found
                                // TODO: remove non-required headers before response
                                exchange.getIn().removeHeaders("*", "breadcrumbId");
                                // TODO: handle bad requests
                            }
                        });

                from("direct:createOrder")
                        .log("Incoming POST request for Order with body:\r\n${body}")
                        .process(new Processor() {
                            @Override
                            public void process(Exchange exchange) throws Exception {
                                int newId = idGenerator.incrementAndGet();
                                Order newOrder = exchange.getIn().getBody(Order.class);
                                newOrder.setId(newId);
                                orders.put(newId, newOrder);
                                exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, 201);
                                // TODO: dynamic building of Location header
                                exchange.getIn().setHeader("Location", "http://localhost:8080/orders/" + newId);
                                // TODO: remove non-required headers before response
                                exchange.getIn().removeHeaders("*", "breadcrumbId", "Location");
                                // TODO: handle bad requests
                            }
                        });

                from("direct:updateOrder")
                        .log("Incoming PUT request for Order with body:\r\n${body}")
                        .process(new Processor() {
                            @Override
                            public void process(Exchange exchange) throws Exception {
                                int resourceId = exchange.getIn().getHeader("id", int.class);
                                int orderId = orders.get(resourceId).getId();
                                Order newOrder = exchange.getIn().getBody(Order.class);
                                newOrder.setId(orderId);
                                orders.put(orderId, newOrder);
                                exchange.getIn().setBody(newOrder, Order.class);
                                exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, 200);
                                // TODO: remove non-required headers before response
                                exchange.getIn().removeHeaders("*", "breadcrumbId");
                                // TODO: handle bad requests
                            }
                        });

                from("direct:deleteOrder")
                        .log("Incoming DELETE request for Order with ID: ${headers.id}...")
                        .process(new Processor() {
                            @Override
                            public void process(Exchange exchange) throws Exception {
                                int idToDelete = exchange.getIn().getHeader("id", int.class);
                                orders.remove(idToDelete);
                                exchange.getIn().setBody(null);
                                exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, 204);
                                // TODO: remove non-required headers before response
                                exchange.getIn().removeHeaders("*", "breadcrumbId");
                                // TODO: handle bad requests
                            }
                        });
            }
        });

        context.start();
        main.getCamelContexts().add(context);
        main.run();
    }
}
