package ru.otus.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RabbitToKafkaRoute extends RouteBuilder {

    @Value("${kafka.bootstrap-servers}")
    private String kafkaBootstrapServers;

    @Value("${kafka.topics.queue1-messages}")
    private String queue1Topic;

    @Value("${kafka.topics.queue2-messages}")
    private String queue2Topic;

    @Override
    public void configure() throws Exception {

        // Маршрут для обработки сообщений из queue1 и отправки в Kafka
        from("spring-rabbitmq:myExchange?queues=queue1&routingKey=routingKeyQueue1"
                + "&autoDeclare=false"
                + "&autoStartup=true"
                + "&concurrentConsumers=1"
                + "&bridgeErrorHandler=true")
                .id("queue1-to-kafka") // Добавляем ID маршрута
                .log(" [QUEUE1->KAFKA] Получено сообщение из RabbitMQ")
                .process(exchange -> {
                    byte[] body = exchange.getIn().getBody(byte[].class);
                    if (body != null) {
                        String message = new String(body);
                        log.info(" [QUEUE1->KAFKA] Текст сообщения: {}", message);
                        log.info(" [QUEUE1->KAFKA] Размер сообщения: {} байт", body.length);
                        exchange.getIn().setBody(message);

                        // Устанавливаем ключ для партиционирования в Kafka
                        exchange.getIn().setHeader("kafka.KEY", "queue1-" + System.currentTimeMillis());
                    } else {
                        log.warn(" [QUEUE1->KAFKA] Получено пустое сообщение");
                    }
                })
                .log(" [QUEUE1->KAFKA] Отправка в Kafka топик: ${body}")
                .to("kafka:" + queue1Topic +
                        "?brokers=" + kafkaBootstrapServers +
                        "&keySerializer=org.apache.kafka.common.serialization.StringSerializer" +
                        "&valueSerializer=org.apache.kafka.common.serialization.StringSerializer")
                .log(" [QUEUE1->KAFKA] Сообщение успешно отправлено в Kafka");

        // Маршрут для обработки сообщений из queue2 и отправки в Kafka
        from("spring-rabbitmq:myExchange?queues=queue2&routingKey=routingKeyQueue2"
                + "&autoDeclare=false"
                + "&autoStartup=true"
                + "&concurrentConsumers=1"
                + "&bridgeErrorHandler=true")
                .id("queue2-to-kafka") // Добавляем ID маршрута
                .log(" [QUEUE2->KAFKA] Получено сообщение из RabbitMQ")
                .process(exchange -> {
                    byte[] body = exchange.getIn().getBody(byte[].class);
                    if (body != null) {
                        String message = new String(body);
                        log.info(" [QUEUE2->KAFKA] Текст сообщения: {}", message);
                        log.info(" [QUEUE2->KAFKA] Размер сообщения: {} байт", body.length);
                        exchange.getIn().setBody(message);

                        // Устанавливаем ключ для партиционирования в Kafka
                        exchange.getIn().setHeader("kafka.KEY", "queue2-" + System.currentTimeMillis());
                    } else {
                        log.warn(" [QUEUE2->KAFKA] Получено пустое сообщение");
                    }
                })
                .log(" [QUEUE2->KAFKA] Отправка в Kafka топик: ${body}")
                .to("kafka:" + queue2Topic +
                        "?brokers=" + kafkaBootstrapServers +
                        "&keySerializer=org.apache.kafka.common.serialization.StringSerializer" +
                        "&valueSerializer=org.apache.kafka.common.serialization.StringSerializer")
                .log(" [QUEUE2->KAFKA] Сообщение успешно отправлено в Kafka");
    }
}