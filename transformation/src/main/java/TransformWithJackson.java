import model.Item;
import org.apache.camel.CamelContext;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.main.Main;

public class TransformWithJackson {
    private static final String LOGGER = TransformWithJackson.class.getCanonicalName();
    private static final String LOG_MESSAGE = "HEADERS:\r\n${headers}\r\nBODY:\r\n${body}";
    private static final String ROUTE_ID = TransformWithJackson.class.getCanonicalName();

    public static void main(String... args) throws Exception {
        Main main = new Main();
        CamelContext context = new DefaultCamelContext();
        context.setUseMDCLogging(true);

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                JacksonDataFormat jsonFormat = new JacksonDataFormat();

                Item item = new Item();
                item.setItemId("12345");
                item.setItemDescription("Can of beans");

                // TODO: figure out how this is supposed to work
                from("timer:timer?repeatCount=1")
                        .id(ROUTE_ID)
                        .setBody().constant(item)
                        .log(LoggingLevel.INFO, LOGGER, LOG_MESSAGE)
                        .marshal(jsonFormat)
                        .log(LoggingLevel.INFO, LOGGER, LOG_MESSAGE);
            }
        });

        context.start();
        main.getCamelContexts().add(context);
        main.run();
    }
}
