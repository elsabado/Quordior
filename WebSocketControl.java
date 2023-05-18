package com.cs309.quoridorApp.controllers;

import com.cs309.quoridorApp.dataPackets.websockets.WebsocketPacket;
import com.cs309.quoridorApp.game.Game;
import com.cs309.quoridorApp.player.Player;
import com.cs309.quoridorApp.player.PlayerSession;
import com.cs309.quoridorApp.repository.SessionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Controller
@ServerEndpoint(value = "/game/{uuid}")
//@RequestMapping("/api")
//@Api(value = "WebSocketControl", description = "API endpoints for WebSocket communication")
public class WebSocketControl {

    private static SessionRepository sessions;

    @Autowired
    public void setSessionRepository(SessionRepository sr)
    {
        sessions = sr;
    }

    private static Map <Session, String> sessionUser = new Hashtable<>();
    private static Map<String, Session> userSession = new Hashtable<>();

    private static final Logger logger = LoggerFactory.getLogger(WebSocketControl.class);

//    @ApiOperation(value = "Open a new WebSocket connection")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "WebSocket connection opened"),
//            @ApiResponse(code = 400, message = "Bad request"),
//            @ApiResponse(code = 401, message = "Unauthorized"),
//            @ApiResponse(code = 404, message = "Resource not found"),
//            @ApiResponse(code = 500, message = "Internal server error")
//    })
    @OnOpen
    public void onOpen(Session session, @PathParam("uuid") String uuid)
            throws IOException {
        logger.info("Entered into Open");

        String username = "";

        for(PlayerSession ses : sessions.findAll())
            if(ses.getId().equals(uuid))
                username = ses.getPlayer().getUsername();

        if(username.isEmpty()) {
            logger.info("SESSION DOESNT EXIST: " + uuid);
            return;
        }

        logger.info("Player: " + username);

        if(userSession.containsKey(username))
            sessionUser.remove(userSession.get(username));

        if(session == null)
            logger.info("WTF WHY IS THE SESSION NULLLLLL??????????????????????????????????");


        sessionUser.put(session, username);
        userSession.put(username, session);
    }

//    @ApiOperation(value = "Handle WebSocket messages")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "WebSocket message received and processed"),
//            @ApiResponse(code = 400, message = "Bad request"),
//            @ApiResponse(code = 401, message = "Unauthorized"),
//            @ApiResponse(code = 404, message = "Resource not found"),
//            @ApiResponse(code = 500, message = "Internal server error")
//    })
    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        // Handle new messages
        logger.info("Received message (somehow...): " + message);
    }

//    @ApiOperation(value = "Close a WebSocket connection")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "WebSocket connection closed"),
//            @ApiResponse(code = 400, message = "Bad request"),
//            @ApiResponse(code = 401, message = "Unauthorized"),
//            @ApiResponse(code = 404, message = "Resource not found"),
//            @ApiResponse(code = 500, message = "Internal server error")
//    })
    @OnClose
    public void onClose(Session session) throws IOException {
        logger.info("Entered into Close");

        String username = sessionUser.get(session);
        sessionUser.remove(session);
        if(username != null && userSession.containsKey(username))
            userSession.remove(username);
    }

//    @ApiOperation(value = "Handle WebSocket errors")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "WebSocket error handled"),
//            @ApiResponse(code = 400, message = "Bad request"),
//            @ApiResponse(code = 401, message = "Unauthorized"),
//            @ApiResponse(code = 404, message = "Resource not found"),
//            @ApiResponse(code = 500, message = "Internal server error")
//    })
    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
        logger.info("Entered into Error: " + throwable.toString());
    }

    public static void sendMessage(String username, String message) {
        try {
            logger.info("PRE-MESSAGE SEND");
            if(userSession.containsKey(username))
            {
                userSession.get(username).getBasicRemote().sendText(message);
            }
            else {
                logger.info("USER DOES NOT HAVE WEBSOCKET");
            }
            logger.info("POST-MESSAGE SEND");
        } catch (IOException e) {
            logger.info("Exception: " + e.getMessage().toString());
            e.printStackTrace();
        }
    }

    public static void sendMessage(String username, WebsocketPacket packet)
    {
        try {
            String message = new ObjectMapper().writeValueAsString(packet);
            logger.info("JSON: " + message);
            sendMessage(username, message);
        } catch (Exception e) {
            logger.info("Exception: " + e.getMessage().toString());
            e.printStackTrace();
        }
    }

    public static void broadcast(Game game, String message) {
        game.getClientList().getNotBots(). //get all non-bots
                forEach(player -> { //for every player, send the message
            sendMessage(player.getUsername(), message);
        });
    }

    public static void broadcast(Game game, WebsocketPacket packet) {
        try {
            String message = new ObjectMapper().writeValueAsString(packet);
            logger.info("JSON: " + message);
            broadcast(game, message);
        } catch (Exception e) {
            logger.info("Exception: " + e.getMessage().toString());
            e.printStackTrace();
        }
    }
}
