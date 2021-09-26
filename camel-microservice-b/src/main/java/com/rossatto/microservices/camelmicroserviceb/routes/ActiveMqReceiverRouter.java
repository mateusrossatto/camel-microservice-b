package com.rossatto.microservices.camelmicroserviceb.routes;

import com.rossatto.microservices.camelmicroserviceb.CurrencyExchange;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ActiveMqReceiverRouter extends RouteBuilder {

    private static final String ROUTE_ACTIVEMQ_QUEUE = "activemq:my-activemq-queue";
    private static final String ROUTE_ACTIVEMQ_QUEUE_XML = "activemq:my-activemq-xml-queue";

    @Autowired
    private MyCurrencyExchangeProcessor myCurrencyExchangeProcessor;

    @Autowired
    private MyCurrencyExchangeTransformer myCurrencyExchangeTransformer;

    @Override
    public void configure() throws Exception {

        from(ROUTE_ACTIVEMQ_QUEUE)
                .unmarshal()
                    .json(JsonLibrary.Jackson, CurrencyExchange.class)
                .bean(myCurrencyExchangeProcessor, "processMessage")
                .bean(myCurrencyExchangeTransformer)
                .log("${body}")
                .to("log: " + ROUTE_ACTIVEMQ_QUEUE);

        from(ROUTE_ACTIVEMQ_QUEUE_XML)
                .unmarshal().jacksonxml(CurrencyExchange.class)
                .to("log: " + ROUTE_ACTIVEMQ_QUEUE_XML);

    }

}

@Component
class MyCurrencyExchangeProcessor {

    Logger logger = LoggerFactory.getLogger(MyCurrencyExchangeProcessor.class);

    public void processMessage(CurrencyExchange currencyExchange) {

        logger.info("currencyExchange.getConversionMultiple() {}", currencyExchange.getConversionMultiple());

    }

}


@Component
class MyCurrencyExchangeTransformer {

    Logger logger = LoggerFactory.getLogger(MyCurrencyExchangeProcessor.class);

    public String processMessage(CurrencyExchange currencyExchange) {

        currencyExchange.setConversionMultiple(currencyExchange.getConversionMultiple().multiply(BigDecimal.TEN));
        return currencyExchange.toString();

    }

}