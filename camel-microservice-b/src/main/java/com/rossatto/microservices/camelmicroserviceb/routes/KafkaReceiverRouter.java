package com.rossatto.microservices.camelmicroserviceb.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class KafkaReceiverRouter extends RouteBuilder {

    private static final String ROUTE_KAFKA_TOPIC = "kafka:myKafkaTopic";

    @Override
    public void configure() throws Exception {

        from(ROUTE_KAFKA_TOPIC)
                .log("${body}")
                .to("log:reveived-message-from-kafka-topic");

    }

}
