package com.asodc.camel.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class PropertiesToCsvProcessor implements Processor {
    private static final String CSV_FIELD_DELIMITER = ";";
    private static final String CSV_ROW_DELIMITER = "\r\n";
    private static final String PROPERTIES_FIELD_DELIMITER = "=";
    private static final String PROPERTIES_ROW_DELIMITER = "\r\n";

    @Override
    public void process(Exchange exchange) throws Exception {
        StringBuilder csvHeaders = new StringBuilder();
        StringBuilder csvValues = new StringBuilder();

        String body = exchange.getIn().getBody(String.class);
        String [] rows = body.split(PROPERTIES_ROW_DELIMITER);

        int iterationCount = 1;
        for (String row : rows) {
            String [] fields = row.split(PROPERTIES_FIELD_DELIMITER);
            csvHeaders.append(fields[0]);
            csvValues.append(fields[1]);
            if (iterationCount < rows.length) {
                csvHeaders.append(CSV_FIELD_DELIMITER);
                csvValues.append(CSV_FIELD_DELIMITER);
            }
            iterationCount++;
        }
        csvHeaders.append(CSV_ROW_DELIMITER);
        csvValues.append(CSV_ROW_DELIMITER);

        exchange.getIn().setBody(csvHeaders.toString() + csvValues.toString());
    }
}
