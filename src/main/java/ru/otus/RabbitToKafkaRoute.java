package ru.otus;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class RabbitToKafkaRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        // Маршрут для очереди queue1 (гласные буквы) → топик vowels-letters
        from("rabbitmq:myExchange?queue=queue1&routingKey=routingKeyQueue1&autoDelete=false")
                .log("Received from RabbitMQ (queue1): ${body}")
                .process(exchange -> {
                    // Преобразуем byte[] в String для Kafka
                    byte[] body = exchange.getIn().getBody(byte[].class);
                    if (body != null && body.length > 0) {
                        String letter = new String(body);
                        exchange.getIn().setBody(letter);
                    }
                })
                .to("kafka:vowels-letters?brokers=localhost:9093")
                .log("Sent to Kafka topic 'vowels-letters': ${body}");

        // Маршрут для очереди queue2 (согласные буквы) → топик consonant-letters
        from("rabbitmq:myExchange?queue=queue2&routingKey=routingKeyQueue2&autoDelete=false")
                .log("Received from RabbitMQ (queue2): ${body}")
                .process(exchange -> {
                    // Преобразуем byte[] в String для Kafka
                    byte[] body = exchange.getIn().getBody(byte[].class);
                    if (body != null && body.length > 0) {
                        String letter = new String(body);
                        exchange.getIn().setBody(letter);
                    }
                })
                .to("kafka:consonant-letters?brokers=localhost:9093")
                .log("Sent to Kafka topic 'consonant-letters': ${body}");
    }
}