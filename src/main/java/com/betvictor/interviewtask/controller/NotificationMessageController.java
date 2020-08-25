package com.betvictor.interviewtask.controller;

import com.betvictor.interviewtask.model.NotificationMessage;
import com.betvictor.interviewtask.repository.NotificationMessageRepository;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notification")
public class NotificationMessageController {

    private final NotificationMessageRepository notificationMessageRepository;

    public NotificationMessageController(NotificationMessageRepository notificationMessageRepository) {
        this.notificationMessageRepository = notificationMessageRepository;
    }

    @ApiOperation(value = "It will return all notifications that have been sent to business by queue")
    @RequestMapping(method = RequestMethod.GET, path = "/")
    public List<NotificationMessage> getAllNotificationMessages() {
        return notificationMessageRepository.findAll();
    }

}
