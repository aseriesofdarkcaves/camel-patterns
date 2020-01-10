package com.asodc.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.main.Main;

public class CxfrsCamel {
    public static void main(String... args) throws Exception {
        Main main = new Main();
        CamelContext camelContext = new DefaultCamelContext();
        camelContext.addRoutes(new OrderRoute());
        main.getCamelContexts().add(camelContext);
        main.start();
    }
}
