package com.betvictor.interviewtask.controller;

import com.betvictor.interviewtask.model.GameAction;
import com.betvictor.interviewtask.service.GameActionService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/game-action")
public class GameActionController {

    private final GameActionService gameActionService;

    public GameActionController(GameActionService actionService) {
        this.gameActionService = actionService;
    }

    @ApiOperation(value = "This method will return all stored game action in database")
    @RequestMapping(method = RequestMethod.GET, path = "/")
    public List<GameAction> getAllActions() {
        return gameActionService.getAllActions();
    }

    @ApiOperation(value = "This method will return all stored game action in database for given accountId")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accountId", value = "ID account", dataTypeClass = Integer.class, required = true),
    })
    @RequestMapping(method = RequestMethod.GET, path = "/{accountId}")
    public List<GameAction> getGameActionByAccountId(@PathVariable("accountId") final Integer accountId) {
        return gameActionService.findActions(accountId);
    }

}
