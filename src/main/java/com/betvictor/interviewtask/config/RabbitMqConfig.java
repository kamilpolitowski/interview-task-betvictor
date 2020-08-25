package com.betvictor.interviewtask.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Value("${rabbitMq.queues.notification-of-playing-too-much.name}")
    private String notificationQueueName;
    @Value("${rabbitMq.queues.game-action.name}")
    private String gameActionQueueName;

    /**
     * This beans is only for automatic queue creation
     */
    @Bean
    public Queue notificationMessageQueue() {
        return new Queue(notificationQueueName, false);
    }
    @Bean
    public Queue gameActionQueue() {
        return new Queue(gameActionQueueName, false);
    }
}
