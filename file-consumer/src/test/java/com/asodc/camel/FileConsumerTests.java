package com.asodc.camel;

import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class FileConsumerTests extends CamelTestSupport {
    @Test
    public void placeholderTest() throws Exception {
        // Use this to make the test fail - as there is no advising of the route, it starts automatically
        //context.stop();

        assertTrue("The camel context was not started.", context.getStatus().isStarted());
    }
}
