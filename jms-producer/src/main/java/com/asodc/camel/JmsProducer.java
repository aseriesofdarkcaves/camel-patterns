package com.asodc.camel;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.main.Main;

import javax.jms.ConnectionFactory;

public class JmsProducer {
    public static void main(String... args) throws Exception {
        Main main = new Main();
        CamelContext context = new DefaultCamelContext();
        context.setUseMDCLogging(true);

        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        context.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("timer:timer?period=5000")
                        .setHeader("MessageType").constant("TestMessage")
                        .setBody().constant("Test body!")
                        .log("Logging message to jms!")
                        .to("jms:TestQueue");
            }
        });

        context.start();
        main.getCamelContexts().add(context);
        main.run();
    }
}
