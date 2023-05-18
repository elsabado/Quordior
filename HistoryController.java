package com.cs309.quoridorApp.controllers;

import com.cs309.quoridorApp.packets.ResultPacket;
import com.cs309.quoridorApp.player.History;
import com.cs309.quoridorApp.player.Player;
import com.cs309.quoridorApp.player.PlayerStats;
import com.cs309.quoridorApp.repository.HistoryRepository;
import com.cs309.quoridorApp.repository.PlayerStatsRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This controller is coresponds to the History entity class.
 * It will have GET, POST, PUT, and Delete Mapping
 * I yet to create these methods.
 */
@RestController
@RequestMapping("/api")
@Api(value = "History Controller")
public class HistoryController {

    @Autowired
    private HistoryRepository historyRepository;
    @Autowired
    private PlayerStatsRepository statsRepository;
    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    /**
     * This post mapping gives the list of perivous oppents played
     * @param username the Playerwhat wants access to the history
     * @return list of the prev opponents
     */
    @ApiOperation(value = "Get previous opponents")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved previous opponents"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @PostMapping(path = "/previousOpponents/{username}")
    List<History> getPreviousOpponents(@PathVariable String username){

        return historyRepository.findAll();
    }

    @ApiOperation(value = "Create player stats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully created player stats"),
            @ApiResponse(code = 400, message = "Bad request. Player stats does not exist")
    })
    @PostMapping(path = "/previousOpponents")
    ResultPacket createPlayerStats(History history){
        if (history == null){
            return new ResultPacket(false, "doesNotExist");
        }
        historyRepository.save(history);
        return new ResultPacket(true, "");
    }

    @ApiOperation(value = "Update previous opponents")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated previous opponents"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @PutMapping("/previousOpponents/{username}")
    List<String> updatePreviousOpponents(@PathVariable String username, @RequestBody History request){
        PlayerStats pStat = statsRepository.getOne(username);
        if(pStat == null)
            return null;
        return pStat.getPrevOpponents();
    }
}
