package com.betvictor.interviewtask.controller;


import com.betvictor.interviewtask.model.GameAction;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/game-action/publisher-simulator")
public class GameActionSimulatorPublisherController {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitMq.queues.game-action.name}")
    private String gameActionQueueName;

    public GameActionSimulatorPublisherController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @ApiOperation(value = "It is simulator of publishing game action message from some communication layer")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accountId", value = "ID of existing account", dataTypeClass = Integer.class, required = true),
            @ApiImplicitParam(name = "stake", value = "Stake", dataTypeClass = BigDecimal.class, required = true),
            @ApiImplicitParam(name = "actionTime", value = "You can simulate when message came, in given format yyyy-mm-ddTHH:MM:SS")
    })
    @RequestMapping(method = RequestMethod.POST, path = "/")
    public void publishGameActionMessage(@RequestParam("accountId") Integer accountId,
                                         @RequestParam("stake") BigDecimal stake,
                                         @RequestParam(required = false, name = "actionTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                         @ApiParam(format = "yyyy-mm-ddTHH:MM:SS") LocalDateTime actionTime) {

        GameAction gameAction = GameAction.builder().accountId(accountId).stake(stake).actionTime(actionTime).build();

        rabbitTemplate.convertAndSend(gameActionQueueName, gameAction);
    }

}
