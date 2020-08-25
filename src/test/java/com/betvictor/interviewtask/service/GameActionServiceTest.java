package com.betvictor.interviewtask.service;

import com.betvictor.interviewtask.model.GameAction;
import com.betvictor.interviewtask.repository.GameActionRepository;
import com.betvictor.interviewtask.service.notification.NotificationFunctionality;
import com.betvictor.interviewtask.service.notification.PlayingTooMuchNotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;


@ExtendWith(MockitoExtension.class)
public class GameActionServiceTest {

    private GameActionService cut;

    @Mock
    private GameActionRepository accountActionRepository;

    @Mock
    private PlayingTooMuchNotificationService playingTooMuchNotificationService;

    @Mock
    // Empty implementation of NotificationFunctionality
    private NotificationFunctionality anotherNotificationFunction = (accountId) -> {};

    @BeforeEach
    public void before() {
        cut = new GameActionService(accountActionRepository, Arrays.asList(playingTooMuchNotificationService));
    }

    @Test
    public void shouldSaveGameActionAndRunNotificationFunctionality() {
        GameAction gameAction = GameAction.builder().stake(new BigDecimal(100)).accountId(10).build();

        cut.consumeGameAction(gameAction);

        Mockito.verify(accountActionRepository).save(gameAction);
        Mockito.verify(playingTooMuchNotificationService).checkAndSendNotification(10);
    }

    @Test
    public void shouldSaveGameActionAndNotRunNotificationFunctionalityForNullAccountId() {
        GameAction gameAction = GameAction.builder().stake(new BigDecimal(100)).accountId(12).build();

        cut.consumeGameAction(gameAction);

        Mockito.verify(accountActionRepository).save(gameAction);
        Mockito.verify(playingTooMuchNotificationService).checkAndSendNotification(12);
        Mockito.verify(playingTooMuchNotificationService, Mockito.times(0)).checkAndSendNotification(null);
    }

    @Test
    public void shouldRunTwoNotificationFunctionalities() {
        GameAction gameAction = GameAction.builder().stake(new BigDecimal(10)).accountId(12).build();

        List<NotificationFunctionality> notificationFunctionalities = Arrays.asList(playingTooMuchNotificationService, anotherNotificationFunction);
        cut = new GameActionService(accountActionRepository, notificationFunctionalities);

        cut.consumeGameAction(gameAction);

        Mockito.verify(accountActionRepository,Mockito.times(1)).save(gameAction);

        //for every NotificationFunctionality checkAndSendNotification should be run once.
        notificationFunctionalities.forEach(
                notificationFunctionality -> {
                    Mockito.verify(notificationFunctionality, Mockito.times(1)).checkAndSendNotification(12);
                }
        );
    }
}