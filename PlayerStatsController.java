package com.cs309.quoridorApp.controllers;


import com.cs309.quoridorApp.packets.ResultPacket;
import com.cs309.quoridorApp.player.Player;
import com.cs309.quoridorApp.player.PlayerSession;
import com.cs309.quoridorApp.player.PlayerStats;
import com.cs309.quoridorApp.repository.PlayerRepository;
import com.cs309.quoridorApp.repository.SessionRepository;
import com.cs309.quoridorApp.repository.PlayerStatsRepository;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This PLayerStats controller class controls the Stats in Repo
 * Has GET, POST, PUT mappings
 */
@RestController
//@Api(value = "Player Stats Controller", tags = { "Player Stats" })
//@RequestMapping("/api")
public class PlayerStatsController {

    @Autowired
    private PlayerStatsRepository playerStatsRepository;
    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";


    /**
     * GET request to get the playerStats.
     * @return All playerStats from a player
     */
    @PostMapping(path = "/allPlayerStats")
//    @ApiOperation(value = "Get all player stats")
    List<PlayerStats> getAllPlayerStats(){
        return playerStatsRepository.findAll();
    }


    /**
     * GET request for a PlayerStats with a specific username
     * @param username The username of the playerStats
     * @return PlayerStats with that username
     */
    @PostMapping(path = "/playerStats/{un}")
    public @ResponseBody PlayerStats getPlayerStatsByUsername(@PathVariable String un) {
        return playerStatsRepository.getOne(un);
    }

    /**
     * POST request to create a new playerStats in the repository.
     * @param playerStats playerStats object to add to repository
     * @return a success message if successful or a fail message if failed to add to repository
     */
    @PostMapping(path = "/playerStats")
    @ApiOperation(value = "Create player stats")
    ResultPacket createPlayerStats(PlayerStats playerStats){
        if (playerStats == null){
            return new ResultPacket(false, "doesNotExist");
        }
        playerStatsRepository.save(playerStats);
        return new ResultPacket(true, "");
    }


    /**
     * PUT request to update an existing playerStats in the repository.
     *
     * @param username the username of the playerStat to be updated
     * @param request Updates to that playerStat
     * @return The updated note
     */
    @PutMapping("/playerstats/{username}")
    @ApiOperation(value = "Update player stats")
    PlayerStats updatePlayerStats(@PathVariable String username, @RequestBody PlayerStats request){
        PlayerStats playerStats = playerStatsRepository.getOne(username);
        if(playerStats == null)
            return null;
        playerStatsRepository.save(request);
        return playerStatsRepository.getOne(username);
    }

}