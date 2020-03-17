package com.asodc.camel;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.main.Main;

public class MessageExchangePattern {
    public static void main(String... args) throws Exception{
        Main main = new Main();
        CamelContext context = new DefaultCamelContext();
        context.setUseMDCLogging(true);

        PooledConnectionFactory pooledConnection = new PooledConnectionFactory(new ActiveMQConnectionFactory("tcp://localhost:61616"));
        context.addComponent("jms", JmsComponent.jmsComponentTransacted(pooledConnection));

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                // InOnly JMS Producer to trigger the jmsInOutConsumer
                from("timer:timer?period=3000").routeId("jmsInOnlyProducer")
                        .setBody().constant("This message was produced via jmsInOnlyProducer")
                        .to("jms:queue:RequestReplyTest.Request");

                // InOut JMS Consumer
                from("jms:queue:RequestReplyTest.Request?replyTo=RequestReplyTest.Response").routeId("jmsInOutConsumer")
                        .setBody().constant("This message was produced by jmsInOutConsumer");
//                        .transform().constant("This message was produced by jmsInOutConsumer");

                // This just consumes the reply queue and logs a message to the console
                from("jms:queue:RequestReplyTest.Response").routeId("jmsConsumerLogger")
                        .log("Consumed JMS message from RequestReplyTest.Response queue... ${body}");
            }
        });

        context.start();
        main.getCamelContexts().add(context);
        main.run();
    }
}
