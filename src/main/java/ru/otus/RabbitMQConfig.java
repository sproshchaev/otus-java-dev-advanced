package ru.otus;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE1 = "queue1";
    public static final String QUEUE2 = "queue2";
    public static final String EXCHANGE_NAME = "myExchange";
    public static final String ROUTING_KEY_QUEUE1 = "routingKeyQueue1";
    public static final String ROUTING_KEY_QUEUE2 = "routingKeyQueue2";

    /**
     * Создаем обменник (exchange) типа Direct
     */
    @Bean
    public DirectExchange myExchange() {
        return new DirectExchange(EXCHANGE_NAME, true, false);
    }

    /**
     * Создаем первую очередь
     */
    @Bean
    public Queue queue1() {
        return QueueBuilder.durable(QUEUE1).build();
    }

    /**
     * Создаем вторую очередь
     */
    @Bean
    public Queue queue2() {
        return QueueBuilder.durable(QUEUE2).build();
    }

    /**
     * Привязываем queue1 к обменнику с routingKeyQueue1
     */
    @Bean
    public Binding binding1() {
        return BindingBuilder.bind(queue1())
                .to(myExchange())
                .with(ROUTING_KEY_QUEUE1);
    }

    /**
     * Привязываем queue2 к обменнику с routingKeyQueue2
     */
    @Bean
    public Binding binding2() {
        return BindingBuilder.bind(queue2())
                .to(myExchange())
                .with(ROUTING_KEY_QUEUE2);
    }
}