package com.cs309.quoridorApp.controllers;

import com.cs309.quoridorApp.game.board.tileBound.DoubleWall;
import com.cs309.quoridorApp.game.board.tileBound.PlayerMarker;
import com.cs309.quoridorApp.packets.*;
import com.cs309.quoridorApp.player.PlayerList;
import com.cs309.quoridorApp.repository.SessionRepository;
import com.cs309.quoridorApp.game.Game;
import com.cs309.quoridorApp.game.GameHolder;
import com.cs309.quoridorApp.game.board.Tile;
import com.cs309.quoridorApp.player.Player;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
//@Api(value = "BoardPlayer Controller", description = "APIs for Quoridor board and player actions")
//@RequestMapping("/api")
public class BoardPlayerController
{
    /*
    stores the mapping for player input in the actual game
     */

    @Autowired
    private SessionRepository sessionRepository;

//    @ApiOperation(value = "Get the current state of the game board")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Successfully retrieved the game board"),
//            @ApiResponse(code = 404, message = "No session, no game, or no board")
//    })
    @PostMapping("/seeBoardTest")
    public String seeBoardTest(@RequestBody GamePacket packet)
    {
        if(!sessionRepository.existsById(packet.getId()) ||
                !GameHolder.inSession(packet.getGameId()) ||
                GameHolder.getGame(packet.getGameId()).getGameBoard() == null)
            return "noSession, noGame, or noBoard";
        return GameHolder.getGame(packet.getGameId()).getGameBoard().toString();
    }

//    @ApiOperation(value = "Get the byte representation of the game board")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Successfully retrieved the byte representation of the game board"),
//            @ApiResponse(code = 404, message = "No session, no game, or no board")
//    })
    @PostMapping("/byteBoardTest")
    public @ResponseBody ByteBoardPacket byteBoardTest(@RequestBody GamePacket packet)
    {
        if(!sessionRepository.existsById(packet.getId()) ||
                !GameHolder.inSession(packet.getGameId()) ||
                GameHolder.getGame(packet.getGameId()).getGameBoard() == null)
            return new ByteBoardPacket(new int[0][0]);
        Game g = GameHolder.getGame(packet.getGameId());
        return new ByteBoardPacket(g.getGameBoard().getBytes());
    }

//    @ApiOperation(value = "Test a player's move")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Successfully tested the player's move"),
//            @ApiResponse(code = 404, message = "No session, no game, no board, or not in game")
//    })
    @PutMapping("/playTest")
    public @ResponseBody ResultPacket testMove(@RequestBody TestMovementPacket packet)
    {
        if(!sessionRepository.existsById(packet.getId()) ||
                !GameHolder.inSession(packet.getGameId()) ||
                GameHolder.getGame(packet.getGameId()).getGameBoard() == null ||
                !GameHolder.getGame(packet.getGameId()).getClientList()
                        .isClient(sessionRepository.getPlayer(packet.getId())))
            return new ResultPacket(false, "noSession, noGame, noBoard, or notInGame");

        Game g = GameHolder.getGame(packet.getGameId());
        Player p = sessionRepository.getPlayer(packet.getId());
        PlayerList pl = g.getClientList();
        if(!pl.isPlaying(p))
            pl.setPlay(p, true);

        for(Player bot : pl.sortClients(bot -> pl.isBot(bot) && pl.isPlaying(bot)))
            System.out.println("" + bot.getUsername() + " moved: " + pl.getPlayerMaker(bot).setTile(
                    g.getGameBoard().getTile((int) (Math.random()*g.getGameBoard().getRowSize()), (int) (Math.random()*g.getGameBoard().getColumnSize())),
                    Tile.Direction.NONE));

        return new ResultPacket(pl.getPlayerMaker(p).setTile(
                g.getGameBoard().getTile(packet.getX(), packet.getY()),
                Tile.Direction.NONE
        ), "onMoving");
    }

//    @ApiOperation(value = "Test a player's wall placement")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Successfully tested the player's wall placement"),
//            @ApiResponse(code = 404, message = "No session, no game, or no board")
//    })
    @PostMapping("/testWall")
    public String byteBoardTest(@RequestBody TestWallPacket packet)
    {
        if(!sessionRepository.existsById(packet.getId()) ||
                !GameHolder.inSession(packet.getGameId()) ||
                GameHolder.getGame(packet.getGameId()).getGameBoard() == null)
            return "fucked up";
        Game g = GameHolder.getGame(packet.getGameId());
        Player p = sessionRepository.getPlayer(packet.getId());

        DoubleWall wall = new DoubleWall(p);

        wall.setTile(g.getGameBoard().getTile(packet.getX(), packet.getY()), packet.getIsHorizontal() == 0);


        return "k";
    }

//    @ApiOperation(value = "Test a player's move to a specific tile on the board")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Successfully tested the player's move"),
//            @ApiResponse(code = 404, message = "No session, no game, or no board")
//    })
    @PostMapping("/movePlayer")
    public String movePlayer(@RequestBody TestMovePacket packet)
    {
        if(!sessionRepository.existsById(packet.getId()) ||
                !GameHolder.inSession(packet.getGameId()) ||
                GameHolder.getGame(packet.getGameId()).getGameBoard() == null)
            return "fucked up";
        Game g = GameHolder.getGame(packet.getGameId());
        Player p = sessionRepository.getPlayer(packet.getId());
        PlayerMarker pm = g.getClientList().getPlayerMaker(p);

        Tile.Direction d1 = Tile.Direction.values()[packet.getD1()%5];
        Tile.Direction d2 = Tile.Direction.values()[packet.getD2()%5];

        if(!g.getRules().canMoveThere(g.getGameBoard().getTile(packet.getX(), packet.getY()), p, d1, d2))
            return "cant go there";

        return "can go there";
    }
}
