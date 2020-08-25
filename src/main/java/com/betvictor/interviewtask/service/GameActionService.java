package com.betvictor.interviewtask.service;

import com.betvictor.interviewtask.model.GameAction;
import com.betvictor.interviewtask.repository.GameActionRepository;
import com.betvictor.interviewtask.service.notification.NotificationFunctionality;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
public class GameActionService {

    private final GameActionRepository accountActionRepository;

    private final List<NotificationFunctionality> notificationFunctionalities;

    public GameActionService(GameActionRepository accountActionReposity, List<NotificationFunctionality> notificationFunctionalities) {
        this.accountActionRepository = accountActionReposity;
        this.notificationFunctionalities = notificationFunctionalities;
    }

    /**
     * This is main functionality of this service
     *
     * @param gameAction object represents incoming message from communication layer
     */
    @Transactional
    @RabbitListener(queues = "${rabbitMq.queues.game-action.name}")
    public void consumeGameAction(final GameAction gameAction) {
        log.info("Receive gameAction from queue: {}", gameAction);
        accountActionRepository.save(gameAction);

        //run notification functionalities for accountId
        notificationFunctionalities.forEach(notificationFunctionality -> notificationFunctionality.checkAndSendNotification(gameAction.getAccountId()));
    }

    public List<GameAction> getAllActions() {
        return accountActionRepository.findAll();
    }

    public List<GameAction> findActions(Integer accountId) {
        return accountActionRepository.findByAccountId(accountId);
    }
}
