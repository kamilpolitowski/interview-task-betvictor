package com.betvictor.interviewtask.service.notification;

import com.betvictor.interviewtask.model.NotificationMessage;
import com.betvictor.interviewtask.repository.NotificationMessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@Slf4j
@SpringBootTest(properties = {"spring.jpa.properties.hibernate.generate_statistics=true"})
class PlayingTooMuchNotificationServiceIntegrationTest {

    @MockBean
    private RabbitTemplate notificationMessageQueueTemplate;

    @MockBean
    private NotificationMessageRepository notificationMessageRepository;

    @Autowired
    private PlayingTooMuchNotificationService cut;

    @Autowired
    private EntityManager entityManager;

    private Statistics statistics;

    @BeforeEach
    public void initTest() {
        statistics = entityManager.unwrap(Session.class).getSessionFactory().getStatistics();
    }

    @AfterEach
    public void afterTest() {
        statistics.clear();
    }

    @Test
    @Sql("/account_before_limit.sql")
    @Transactional
    void shouldNotSendNotificationCauseLimitIsNotReached() {

        cut.checkAndSendNotification(10);
        Mockito.verify(notificationMessageQueueTemplate, Mockito.times(0)).convertAndSend(anyString(), any(NotificationMessage.class));
        Mockito.verify(notificationMessageRepository, Mockito.times(0)).save(any(NotificationMessage.class));

        Assertions.assertEquals(1, statistics.getQueryExecutionCount());
    }

    @Test
    @Sql("/account_over_limit.sql")
    @Transactional
    void shouldSendNotificationCauseLimitReached() {

        cut.checkAndSendNotification(10);
        Mockito.verify(notificationMessageQueueTemplate, Mockito.times(1)).convertAndSend(anyString(), any(NotificationMessage.class));
        Mockito.verify(notificationMessageRepository, Mockito.times(1)).save(any(NotificationMessage.class));

        Assertions.assertEquals(1, statistics.getQueryExecutionCount());
    }

    @Test
    @Sql("/account_was_over_limit_but_now_is_not.sql")
    @Transactional
    void shouldNotSendNotificationCauseLimitIsNotReachedNowButItWasBefore() {

        cut.checkAndSendNotification(10);
        Mockito.verify(notificationMessageQueueTemplate, Mockito.times(0)).convertAndSend(anyString(), any(NotificationMessage.class));
        Mockito.verify(notificationMessageRepository, Mockito.times(0)).save(any(NotificationMessage.class));

        Assertions.assertEquals(1, statistics.getQueryExecutionCount());
    }

    @Test
    @Sql("/account_over_limit.sql")
    @Transactional
    void shouldNotSendNotificationCauseAccountHasNoGamingActions() {

        cut.checkAndSendNotification(777);
        Mockito.verify(notificationMessageQueueTemplate, Mockito.times(0)).convertAndSend(anyString(), any(NotificationMessage.class));
        Mockito.verify(notificationMessageRepository, Mockito.times(0)).save(any(NotificationMessage.class));
    }

}