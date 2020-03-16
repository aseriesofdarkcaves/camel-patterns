package com.asodc.camel;

import com.asodc.camel.expression.StringToHtmlBreakTransformer;
import org.apache.camel.CamelContext;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.main.Main;

public class TransformWithTransformEip {
    private static final String LOGGER = TransformWithTransformEip.class.getCanonicalName();
    private static final String LOG_MESSAGE = "HEADERS:\r\n${headers}\r\nBODY:\r\n${body}";
    private static final String ROUTE_ID = TransformWithTransformEip.class.getCanonicalName();
    private static final String BODY = "This is some dummy text.\r\nThis is some more dummy text";

    public static void main(String... args) throws Exception {
        Main main = new Main();
        CamelContext context = new DefaultCamelContext();
        context.setUseMDCLogging(true);

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("timer:timer?repeatCount=1")
                        .id(ROUTE_ID)
                        .setBody().constant(BODY)
                        .log(LoggingLevel.INFO, LOGGER, LOG_MESSAGE)
                        // transform sets the body on the OUT message which gets passed to the next Exchange, where it become that Exchange's IN message
                        .transform(body().regexReplaceAll("\r\n", "<br/>"))
                        .transform(new StringToHtmlBreakTransformer())
                        .log(LoggingLevel.INFO, LOGGER, LOG_MESSAGE);
            }
        });

        context.start();
        main.getCamelContexts().add(context);
        main.run();
    }
}
