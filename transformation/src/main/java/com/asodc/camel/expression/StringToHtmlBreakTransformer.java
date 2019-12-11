package com.asodc.camel.expression;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;

public class StringToHtmlBreakTransformer implements Expression {
    @Override
    public <T> T evaluate(Exchange exchange, Class<T> type) {
        String body = exchange.getIn().getBody(String.class);
        body = body.replaceAll("\n", "<br/>");
        body = "<body>" + body + "</body>";
        return (T) body;
    }
}
