package ru.otus;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class RabbitConsumerRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // Простой консюмер для queue1
        from("spring-rabbitmq:myExchange?queues=queue1&routingKey=routingKeyQueue1"
                + "&autoDeclare=false"  // Разрешаем автоматическое объявление
                + "&autoStartup=true"  // Автозапуск
                + "&concurrentConsumers=1"  // Один потребитель
                + "&bridgeErrorHandler=true")  // Пробрасываем ошибки в Camel Error Handler
                .log(" [QUEUE1] Получено сообщение из RabbitMQ")
                .process(exchange -> {
                    byte[] body = exchange.getIn().getBody(byte[].class);
                    if (body != null) {
                        String message = new String(body);
                        log.info(" [QUEUE1] Текст сообщения: {}", message);
                        log.info(" [QUEUE1] Размер сообщения: {} байт", body.length);
                        exchange.getIn().setBody(message);
                    } else {
                        log.warn("️ [QUEUE1] Получено пустое сообщение");
                    }
                })
                .log(" [QUEUE1] Обработка завершена: ${body}");

        // Простой консюмер для queue2
        from("spring-rabbitmq:myExchange?queues=queue2&routingKey=routingKeyQueue2"
                + "&autoDeclare=false"
                + "&autoStartup=true"
                + "&concurrentConsumers=1"
                + "&bridgeErrorHandler=true")
                .log(" [QUEUE2] Получено сообщение из RabbitMQ")
                .process(exchange -> {
                    byte[] body = exchange.getIn().getBody(byte[].class);
                    if (body != null) {
                        String message = new String(body);
                        log.info(" [QUEUE2] Текст сообщения: {}", message);
                        log.info(" [QUEUE2] Размер сообщения: {} байт", body.length);
                        exchange.getIn().setBody(message);
                    } else {
                        log.warn(" [QUEUE2] Получено пустое сообщение");
                    }
                })
                .log(" [QUEUE2] Обработка завершена: ${body}");
    }
}