package com.cs309.quoridorApp.controllers;

import com.cs309.quoridorApp.packets.ResultPacket;
import com.cs309.quoridorApp.player.Player;
import com.cs309.quoridorApp.player.PlayerSession;
import com.cs309.quoridorApp.repository.PlayerRepository;
import com.cs309.quoridorApp.repository.SessionRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@Api(value = "Player API", description = "REST API for managing players", tags = { "Players" })
@RequestMapping("/api")
public class PlayerController
{
    /**
    stores the mapping for player input in the actual game
     */

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private SessionRepository sessionRepository;

    /**
     * Returns a list of all players
     * @return A list of all players in the playerRepository
     */
    @PostMapping("/allPlayers/")
    @ApiOperation(value = "Get all players")
    @ApiResponse(code = 200, message = "List of all players", response = Player.class, responseContainer = "List")
    List<Player> GetAllPlayers(){
        return playerRepository.findAll();
    }

    /**
     * Creates a new player with the given username, password, and name
     * @param username The player's username
     * @param password The player's password
     * @param name The player's name
     * @return The newly created player object
     */
    @PostMapping("player/post/{username}/{password}/{name}")
    @ApiOperation(value = "Create a new player by path variables")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Player created successfully", response = Player.class),
            @ApiResponse(code = 400, message = "Invalid input parameters")
    })
    Player PostPlayerByPath(@ApiParam(value = "The player's username") @PathVariable String username,
                            @ApiParam(value = "The player's password") @PathVariable String password,
                            @ApiParam(value = "The player's name") @PathVariable String name){
        Player newPlayer = new Player(username, password);
        playerRepository.save(newPlayer);
        return newPlayer;
    }

    /**
     * Creates a new player with the given Player object
     * @param newPlayer The player object to create
     * @return The newly created player object
     */
    @PostMapping("player/post")
    @ApiOperation(value = "Create a new player by request body")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Player created successfully", response = Player.class),
            @ApiResponse(code = 400, message = "Invalid input parameters")
    })
    Player PostPlayerByPath(@ApiParam(value = "The player object to create") @RequestBody Player newPlayer){
        playerRepository.save(newPlayer);
        return newPlayer;
    }

    /**
     * Deletes user from repository with an ID number
     * @param username Username of user to be deleted
     * @return either a success or fail ResultPacket with corresponding reasons
     */
    @DeleteMapping(path = "/player/{username}")
    @ApiOperation(value = "Delete a player by username")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Player deleted successfully", response = ResultPacket.class),
            @ApiResponse(code = 400, message = "Invalid input parameters"),
            @ApiResponse(code = 404, message = "Player not found")
    })
    ResultPacket deletePlayer(@ApiParam(value = "Username of the player to delete") @PathVariable String username){
        if(!playerRepository.existsById(username)){
            return new ResultPacket(false, "ID of player does not exist in database");
        }
        Player player = playerRepository.getOne(username);
        for(PlayerSession session : sessionRepository.findByPlayerUsername(username))
            sessionRepository.delete(session);
        playerRepository.superDelete(player);
        return new ResultPacket(true, "");
    }


    /**
     * Deletes all players from the playerRepository
     */
    @DeleteMapping("/allPlayers")
    @ApiOperation(value = "Delete all players")
    @ApiResponse(code = 204, message = "All players deleted successfully")
    public void deleteAllPlayers()
    {
        for(Player player : playerRepository.findAll())
        {
            for(PlayerSession session : sessionRepository.findByPlayerUsername(player.getUsername()))
                sessionRepository.delete(session);
            playerRepository.superDelete(player);
        }
    }

  
}
