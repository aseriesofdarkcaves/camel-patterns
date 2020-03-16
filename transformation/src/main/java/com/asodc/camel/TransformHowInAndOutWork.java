package com.asodc.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.main.Main;

public class TransformHowInAndOutWork {
    private static final String DUMMY_TEXT = "This is some dummy text.\r\nThis is some more dummy text";
    private static final String IN_MESSAGE_BODY = "Current Exchange's IN Message Body: ${body}";
    private static final String OUT_MESSAGE_BODY = "Current Exchange's OUT Message Body: ${out.body}";

    public static void main(String... args) throws Exception {
        Main main = new Main();
        CamelContext context = new DefaultCamelContext();
        context.setUseMDCLogging(true);

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                // default MEP here is InOnly
                from("timer:timer?repeatCount=1").routeId("timerRoute")
                        // setBody() applies to the current Exchange's IN Message
                        .setBody().constant(DUMMY_TEXT)
//                        .log(IN_MESSAGE_BODY).log(OUT_MESSAGE_BODY)
                        .to("direct:logBodies")
                        // transform() applies to the current Exchange's the OUT message
                        .transform(body().regexReplaceAll("\r\n", "<br/>"))
//                        .log(IN_MESSAGE_BODY).log(OUT_MESSAGE_BODY)
                        .to("direct:logBodies")
                        .log("END OF ROUTE!");

                from("direct:logBodies").routeId("logBodiesRoute")
                        // we will never see a non-null OUT Message
                        // as the next Exchange only ever has an IN Message at the beginning
                        .log(IN_MESSAGE_BODY).log(OUT_MESSAGE_BODY);
            }
        });

        context.start();
        main.getCamelContexts().add(context);
        main.run();
    }
}
