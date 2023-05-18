package com.cs309.quoridorApp.controllers;

import com.cs309.quoridorApp.dataPackets.SuperPacket;
import com.cs309.quoridorApp.dataPackets.toClient.ClientPacket;
import com.cs309.quoridorApp.packets.GameSettingsPacket;
import com.cs309.quoridorApp.packets.SessionCheckPacket;
import com.cs309.quoridorApp.player.BotPlayer;
import com.cs309.quoridorApp.repository.HistoryRepository;
import com.cs309.quoridorApp.repository.PlayerRepository;
import com.cs309.quoridorApp.repository.PlayerStatsRepository;
import com.cs309.quoridorApp.repository.SessionRepository;
import com.cs309.quoridorApp.game.Game;
import com.cs309.quoridorApp.game.GameHolder;
import com.cs309.quoridorApp.packets.ResultPacket;
import com.cs309.quoridorApp.packets.GamePacket;
import com.cs309.quoridorApp.player.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * The controller that holds the general mappings where actions
 * are processed
 */
@RestController
//@RequestMapping("/api")
//@Api(value = "Game Controller", tags = {"Game"})
public class GameController
{

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private PlayerStatsRepository statsRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private HistoryRepository historyRepository;

    /**
     * Lobby mapping; used to process lobby actions
     *
     * @param packet - a SuperPacket, which has the user's uuid,
     *               selected game, action type, and action data
     * @return Either a ClientPacket / ReturnPacket that expresses
     * a failure to execute the selected action or a packet that
     * extends ClientPacket which holds data that the client requires,
     * which may include no data
     */
    @PostMapping("/lobby")
//    @ApiOperation(value = "Process lobby actions")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "OK"),
//            @ApiResponse(code = 401, message = "Unauthorized"),
//            @ApiResponse(code = 403, message = "Forbidden"),
//            @ApiResponse(code = 404, message = "Not Found"),
//            @ApiResponse(code = 500, message = "Internal Server Error")})
    public ClientPacket lobby(@RequestBody SuperPacket packet)
    {
        return packet.read().execute(playerRepository, sessionRepository, statsRepository, historyRepository);
    }

    /**
     * Lobby mapping; used to process actions while in games
     *
     * @param packet - a SuperPacket, which has the user's uuid,
     *               selected game, action type, and action data
     * @return Either a ClientPacket / ReturnPacket that expresses
     * a failure to execute the selected action or a packet that
     * extends ClientPacket which holds data that the client requires,
     * which may include no data
     */
//    @ApiOperation(value = "Process actions while in games")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "OK"),
//            @ApiResponse(code = 401, message = "Unauthorized"),
//            @ApiResponse(code = 403, message = "Forbidden"),
//            @ApiResponse(code = 404, message = "Not Found"),
//            @ApiResponse(code = 500, message = "Internal Server Error")})
    @PostMapping("/game")
    public ClientPacket game(@RequestBody SuperPacket packet)
    {
        return packet.read().execute(playerRepository, sessionRepository, statsRepository, historyRepository);
    }

    /**
     * Lobby mapping; used to process chat actions
     *
     * @param packet - a SuperPacket, which has the user's uuid,
     *               selected game, action type, and action data
     * @return Either a ClientPacket / ReturnPacket that expresses
     * a failure to execute the selected action or a packet that
     * extends ClientPacket which holds data that the client requires,
     * which may include no data
     */
//    @ApiOperation(value = "Process chat actions")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "OK"),
//            @ApiResponse(code = 401, message = "Unauthorized"),
//            @ApiResponse(code = 403, message = "Forbidden")})
    @PostMapping("/chat")
    public ClientPacket chat(@RequestBody SuperPacket packet)
    {
        return packet.read().execute(playerRepository, sessionRepository, statsRepository, historyRepository);
    }

    @PostMapping("/stats")
    public ClientPacket stats(@RequestBody SuperPacket packet)
    {
        return packet.read().execute(playerRepository, sessionRepository, statsRepository, historyRepository);
    }


    /*
    //DONE
    @PostMapping("/newGame")
    public GamePacket newGame(@RequestBody SessionCheckPacket packet)
    {
        if(!sessionRepository.existsById(packet.getId()))
            return new GamePacket(packet.getId(), "AAAAAA");
        Player p = sessionRepository.getPlayer(packet.getId());
        Game g = GameHolder.newGame();
        g.getClientList().addClient(p);
        g.getClientList().setHost(p);
        p.getPlayerStats().setGamesPlayed(p.getPlayerStats().getGamesPlayed()+1);
        statsRepository.save(p.getPlayerStats());

        return new GamePacket(packet.getId(), g.id);
    }


    @PutMapping("/editGame")
    public @ResponseBody ResultPacket editGame(@RequestBody GameSettingsPacket packet)
    {
        if(!sessionRepository.existsById(packet.getId()))
            return new ResultPacket(false, "noSession");
        else if (!GameHolder.inSession(packet.getGameId()))
            return new ResultPacket(false, "noGame");
        else if(!GameHolder.getGame(packet.getGameId()).getClientList().isHost(
                sessionRepository.getPlayer(packet.getId())))
            return new ResultPacket(false, "notHost");

        GameHolder.getGame(packet.getGameId()).getRules().setBots(packet.getBots());


        return new ResultPacket(true, "");
    }

    //DONE
    @PostMapping("/startGame")
    public @ResponseBody ResultPacket startGame(@RequestBody GamePacket packet)
    {
        if(!sessionRepository.existsById(packet.getId()))
            return new ResultPacket(false, "noSession");
        else if (!GameHolder.inSession(packet.getGameId()))
            return new ResultPacket(false, "noGame");
        else if(!GameHolder.getGame(packet.getGameId()).getClientList().isHost(
                        sessionRepository.getPlayer(packet.getId())))
            return new ResultPacket(false, "notHost");

        Game g = GameHolder.getGame(packet.getGameId());
        g.startGame();

        for(int i = 0; i < g.getRules().getBots(); i++)
        {
            Player bot = new BotPlayer("QUORIBOT #" + i);
            g.getClientList().addClient(bot);
            g.getClientList().setPlay(bot, true);
            g.getClientList().setTeam(bot, 3);
        }


        return new ResultPacket(true, "");
    }

    //DONE
    @PostMapping("/joinGame")
    public @ResponseBody ResultPacket joinGame(@RequestBody GamePacket packet)
    {
        if(!sessionRepository.existsById(packet.getId()) ||
                !GameHolder.inSession(packet.getGameId()) ||
                GameHolder.getGame(packet.getGameId()).getClientList()
                        .isClient(sessionRepository.getPlayer(packet.getId())))
            return new ResultPacket(false, "noSession, noGame, or alreadyInGame");

        Player p = sessionRepository.getPlayer(packet.getId());
        p.getPlayerStats().setGamesPlayed(p.getPlayerStats().getGamesPlayed()+1);
        statsRepository.save(p.getPlayerStats());

        GameHolder.getGame(packet.getGameId()).getClientList()
                .addClient(sessionRepository.getPlayer(packet.getId()));
        return new ResultPacket(true, "");
    }

    //DONE
    @PostMapping("/getHost")
    public @ResponseBody Player getHost(@RequestBody GamePacket packet)
    {
        if(!sessionRepository.existsById(packet.getId()) ||
                !GameHolder.inSession(packet.getGameId()))
            return null;
        return GameHolder.getGame(packet.getGameId()).getClientList()
                .getHost();
    }

     */

}
