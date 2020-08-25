package com.betvictor.interviewtask.service;

import com.betvictor.interviewtask.model.NotificationMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationReceiverService {

    /**
     * This method and class is only to show that the app real sent message and we are able to consume it from queue
     * - business can consume message from this topic to receive notification
     * @param message incoming message from topic
     */
    @RabbitListener(queues = "${rabbitMq.queues.notification-of-playing-too-much.name}")
    public void receive(NotificationMessage message) {
        log.info("Receive notification from queue: {}", message);
    }

}
