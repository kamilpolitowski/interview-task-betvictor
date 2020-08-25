package com.betvictor.interviewtask.service.notification;

import com.betvictor.interviewtask.model.NotificationMessage;
import com.betvictor.interviewtask.repository.GameActionRepository;
import com.betvictor.interviewtask.repository.NotificationMessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.LocalDateTime;

@Slf4j
@Service
@ConditionalOnProperty(
        value = "business-config.notification-of-playing-too-much.enabled", havingValue = "true")
public class PlayingTooMuchNotificationService implements NotificationFunctionality {

    private final GameActionRepository gameActionRepository;

    private final NotificationMessageRepository notificationMessageRepository;

    private final RabbitTemplate notificationMessageQueueTemplate;

    @Value("${business-config.notification-of-playing-too-much.queue.name}")
    private String queueName;
    @Value("${business-config.notification-of-playing-too-much.threshold.time.minutes}")
    private Integer thresholdTimeMinutes;
    @Value("${business-config.notification-of-playing-too-much.threshold.stake}")
    private BigDecimal thresholdMaxStake;

    public PlayingTooMuchNotificationService(GameActionRepository gameActionRepository, NotificationMessageRepository notificationMessageRepository, RabbitTemplate template) {
        this.gameActionRepository = gameActionRepository;
        this.notificationMessageRepository = notificationMessageRepository;
        this.notificationMessageQueueTemplate = template;
    }

    @Override
    public void checkAndSendNotification(Integer accountId) {
        log.debug("PlayingTooMuchNotificationService enabled, start processing...");

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime thresholdTime = now.minusMinutes(thresholdTimeMinutes);
        BigDecimal stakeSumForAccount = gameActionRepository.sumStakeForThresholdTime(accountId, thresholdTime).orElse(BigDecimal.ZERO);

        if (istMomentToSendNotification(stakeSumForAccount)) {
            NotificationMessage message = buildNotificationMessage(accountId, now, thresholdTime, stakeSumForAccount);
            notificationMessageQueueTemplate.convertAndSend(queueName, message);
            notificationMessageRepository.save(message);
            log.debug(message.getMessage());
        } else {
            log.debug("PlayingTooMuchNotificationService, nothing to notify...");
        }
    }

    /**
     * @param stakeSum sum of all stake for
     *                 return true when stakeSum is greater then $thresholdMaxStake parameter
     */
    private boolean istMomentToSendNotification(BigDecimal stakeSum) {
        return stakeSum.compareTo(thresholdMaxStake) == 1;
    }

    private NotificationMessage buildNotificationMessage(Integer accountId, LocalDateTime now, LocalDateTime thresholdTime, BigDecimal stakeSumForAccount) {
        NotificationMessage message = new NotificationMessage();
        message.setAccountId(accountId);
        message.setMessage(MessageFormat.format("Account ID [{0}] is spending over the limit. His cumulated amount is [{1}] for the time window [{2}] - [{3}]. Current limit is [{4}]",
                accountId, stakeSumForAccount, thresholdTime, now, thresholdMaxStake));
        return message;
    }
}
