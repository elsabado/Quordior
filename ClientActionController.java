package com.cs309.quoridorApp.controllers;

import com.cs309.quoridorApp.game.GameHolder;
import com.cs309.quoridorApp.packets.AddFriendPacket;
import com.cs309.quoridorApp.packets.ResultPacket;
import com.cs309.quoridorApp.packets.SessionCheckPacket;
import com.cs309.quoridorApp.player.Player;
import com.cs309.quoridorApp.repository.PlayerRepository;
import com.cs309.quoridorApp.repository.SessionRepository;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/api")
@Api(value = "Client Actions", tags = { "Client Actions" })
public class ClientActionController
{
    /*
    meant for things that the client can do as a non player
    like adding friends, looking for lobbies,
     */

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    SessionRepository sessionRepository;


    //TODO: add sorting packet and change to list of GameData
    @ApiOperation(value = "Get list of open games")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list of open games"),
            @ApiResponse(code = 404, message = "Session not found")
    })
    @PostMapping("/openGames")
    public List<String> getGames(@RequestBody SessionCheckPacket packet)
    {
        List<String> toReturn = new ArrayList<>();
        if(!sessionRepository.existsById(packet.getId()))
            return toReturn;
        GameHolder.getGames().forEach(game -> toReturn.add((String) game.id));
        return toReturn;
    }

    //TODO: add sorting packet
    @ApiOperation(value = "Get list of friends")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list of friends"),
            @ApiResponse(code = 404, message = "Session not found")
    })
    @PostMapping("/friends")
    public List<Player> getFriends(@RequestBody SessionCheckPacket packet)
    {
        if(!sessionRepository.existsById(packet.getId()))
            return new ArrayList<>();
        return sessionRepository.getPlayer(packet.getId()).getFriends();
    }

    //TODO: add sorting packet
    @ApiOperation(value = "Get list of friend requests")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list of friend requests"),
            @ApiResponse(code = 404, message = "Session not found")
    })
    @PostMapping("/friends/requests")
    public List<Player> getFriendRequests(@RequestBody SessionCheckPacket packet)
    {
        if(!sessionRepository.existsById(packet.getId()))
            return new ArrayList<>();
        return sessionRepository.getPlayer(packet.getId()).getFriendRequests();
    }

    @ApiOperation(value = "Add friend")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added friend"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Session not found"),
            @ApiResponse(code = 409, message = "Friend request already sent")
    })
    @PostMapping("/friends/add")
    public ResultPacket addFriends(@RequestBody AddFriendPacket packet)
    {
        if(!sessionRepository.existsById(packet.getId()))
            return new ResultPacket(false, "noSession");
        else if(!playerRepository.existsById(packet.getFriend()))
            return new ResultPacket(false, "playerDoesNotExist");
        else if(playerRepository.getOne(packet.getFriend())
                .getFriendRequests().contains(sessionRepository.getPlayer(packet.getId())))
            return new ResultPacket(false, "alreadySentRequest");

        Player p = sessionRepository.getPlayer(packet.getId());
        Player f = playerRepository.getOne(packet.getFriend());
        if(p.getFriendRequests().contains(f))
        {
            p.getFriendRequests().removeIf(friend ->
                    {return friend.getUsername().equals(packet.getFriend());}
            );
            p.getFriends().add(f);
            playerRepository.save(p);

            f.getRequestedFriends().removeIf(player ->
                    {return player.getUsername().equals(p.getUsername());}
            );
            f.getFriends().add(p);
            playerRepository.save(f);
            return new ResultPacket(true, "acceptedFriendRequest");
        }
        else
        {
            p.getRequestedFriends().add(f);
            playerRepository.save(p);

            f.getFriendRequests().add(p);
            playerRepository.save(f);
            return new ResultPacket(true, "sentFriendRequest");
        }
    }

}
