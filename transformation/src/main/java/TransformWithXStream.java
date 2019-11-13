import com.thoughtworks.xstream.XStream;
import model.Item;
import org.apache.camel.CamelContext;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.xstream.XStreamDataFormat;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.main.Main;

public class TransformWithXStream {
    private static final String LOGGER = TransformWithXStream.class.getCanonicalName();
    private static final String LOG_MESSAGE = "HEADERS:\r\n${headers}\r\nBODY:\r\n${body}";
    private static final String ROUTE_ID = TransformWithXStream.class.getCanonicalName();

    public static void main(String... args) throws Exception {
        Main main = new Main();
        CamelContext context = new DefaultCamelContext();
        context.setUseMDCLogging(true);

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                XStreamDataFormat xStreamFormat = new XStreamDataFormat();
                XStream xStream = new XStream();
                xStream.alias("item", Item.class);
                xStreamFormat.setXStream(xStream);

                Item item = new Item();
                item.setItemId("12345");
                item.setItemDescription("Can of beans");

                from("timer:timer?repeatCount=1")
                        .id(ROUTE_ID)
                        .setBody().constant(item)
                        .log(LoggingLevel.INFO, LOGGER, LOG_MESSAGE)
                        .marshal(xStreamFormat)
                        .log(LoggingLevel.INFO, LOGGER, LOG_MESSAGE);
            }
        });

        context.start();
        main.getCamelContexts().add(context);
        main.run();
    }
}
