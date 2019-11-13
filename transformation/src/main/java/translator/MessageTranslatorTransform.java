package translator;

import org.apache.camel.CamelContext;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.main.Main;

public class MessageTranslatorTransform {
    private static final String LOGGER = MessageTranslatorTransform.class.getCanonicalName();
    private static final String LOG_MESSAGE = "HEADERS:\r\n${headers}\r\nBODY:\r\n${body}";
    private static final String ROUTE_ID = MessageTranslatorTransform.class.getCanonicalName();
    private static final String BODY = "This is some dummy text.\r\nThis is some more dummy text";

    public static void main(String... args) throws Exception {
        Main main = new Main();
        CamelContext context = new DefaultCamelContext();
        context.setUseMDCLogging(true);

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("timer:timer?repeatCount=1")
                        .setBody().constant(BODY)
                        .log(LoggingLevel.INFO, LOGGER, LOG_MESSAGE)
                        .transform(body().regexReplaceAll("\r\n", "<br/>"))
                        .log(LoggingLevel.INFO, LOGGER, LOG_MESSAGE);
            }
        });

        context.start();
        main.getCamelContexts().add(context);
        main.run();
    }
}
