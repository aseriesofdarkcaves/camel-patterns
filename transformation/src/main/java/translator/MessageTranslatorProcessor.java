package translator;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.main.Main;

public class MessageTranslatorProcessor {
    private static final String LOGGER = MessageTranslatorProcessor.class.getCanonicalName();
    private static final String LOG_MESSAGE = "HEADERS:\r\n${headers}\r\nBODY:\r\n${body}";
    private static final String ROUTE_ID = MessageTranslatorProcessor.class.getCanonicalName();

    public static void main(String... args) throws Exception {
        Main main = new Main();
        CamelContext context = new DefaultCamelContext();
        context.setUseMDCLogging(true);

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("timer:timer?repeatCount=1")
                        .id(ROUTE_ID)
                        .setBody().constant("FirstField=1\r\nSecondField=2\r\nThirdField=3\r\n")
                        .log(LoggingLevel.INFO, LOGGER, LOG_MESSAGE)
                        .process(new Processor() {
                            @Override
                            public void process(Exchange exchange) throws Exception {
                                // get body and set as headers, SISO principles
                                String body = exchange.getIn().getBody(String.class);
                                String [] lines = body.split("\r\n");
                                for (String line : lines) {
                                    String [] keyValueMap = line.split("=");
                                    exchange.getIn().setHeader(keyValueMap[0], keyValueMap[1]);
                                }
                            }
                        })
                        .log(LoggingLevel.INFO, LOGGER, LOG_MESSAGE);
            }
        });

        context.start();
        main.getCamelContexts().add(context);
        main.run();
    }
}
