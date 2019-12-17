package com.asodc.camel;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.main.Main;

public class JmsConsumer {
    public static void main(String... args) throws Exception {
        Main main = new Main();
        CamelContext context = new DefaultCamelContext();
        context.setUseMDCLogging(true);

        //ConnectionFactory activeMqConnection = new ActiveMQConnectionFactory("tcp://localhost:61616");
        PooledConnectionFactory pooledConnection = new PooledConnectionFactory(new ActiveMQConnectionFactory("tcp://localhost:61616"));
        //pooledConnection.setConnectionFactory(activeMqConnection);
        context.addComponent("jms", JmsComponent.jmsComponentTransacted(pooledConnection));

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("jms:TestQueue")
                        .log("Recieved message from queue:\r\nHeaders:\r\n${headers}\r\n\r\nBody:\r\n${body}");
            }
        });

        context.start();
        main.getCamelContexts().add(context);
        main.run();
    }
}
