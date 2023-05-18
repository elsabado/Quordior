package com.cs309.quoridorApp.controllers;

import com.cs309.quoridorApp.packets.ResultPacket;
import com.cs309.quoridorApp.packets.SessionCheckPacket;
import com.cs309.quoridorApp.packets.SessionConfirmationPacket;
import com.cs309.quoridorApp.packets.StartSessionPacket;
import com.cs309.quoridorApp.player.Player;
import com.cs309.quoridorApp.player.PlayerSession;
import com.cs309.quoridorApp.repository.PlayerRepository;
import com.cs309.quoridorApp.repository.PlayerStatsRepository;
import com.cs309.quoridorApp.repository.SessionRepository;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
//@Api(value = "SessionController", tags = { "Session" })
//@RequestMapping("/api")
public class SessionController
{
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private PlayerStatsRepository statsRepository;

    /**
     * This method starts a new player session.
     *
     * @param sessionPacket The StartSessionPacket containing the username and password.
     * @return A SessionConfirmationPacket containing the session ID and error message.
     */
    @PostMapping("/login")
    //@ApiOperation(value = "Start a new session")
    public @ResponseBody SessionConfirmationPacket startSession(@RequestBody StartSessionPacket sessionPacket)
    {

        String id = "";
        String error = "";

        Player player = null;
        if(playerRepository.existsById(sessionPacket.getUsername()))
            player = playerRepository.getOne(sessionPacket.getUsername());

        if(sessionPacket.isLogin())
        {
            if (player == null || !player.getPassword().equals(sessionPacket.getPassword()))
                error = "Invalid username or password";
            else
            {
                PlayerSession ps = new PlayerSession(player);
                sessionRepository.save(ps);
                id = ps.getId();
            }
        }
        else
        {
            if (player == null)
            {
                Player p = new Player(sessionPacket.getUsername(), sessionPacket.getPassword());
                statsRepository.save(p.getPlayerStats());
                playerRepository.save(p);


                PlayerSession ps = new PlayerSession(p);
                sessionRepository.save(ps);
                id = ps.getId();
            }
            else
                error = "Username is taken";
        }
        return new SessionConfirmationPacket(id, error);
    }

    /**
     * This method logs out a player session.
     *
     * @param packet The SessionCheckPacket containing the session ID.
     * @return A ResultPacket indicating if the logout was successful and the error message if any.
     */
    @DeleteMapping("/logout")
    //@ApiOperation(value = "Logout from an existing session")
    public @ResponseBody ResultPacket logout(@RequestBody SessionCheckPacket packet)
    {
        if(!sessionRepository.existsById(packet.getId()))
            return new ResultPacket(false, "Session does not exist closed");
        sessionRepository.deleteById(packet.getId());
        return new ResultPacket(true, "");
    }

    /**
     * This method retrieves a player object for a given session ID.
     *
     * @param packet The SessionCheckPacket containing the session ID.
     * @return The Player object associated with the session ID, or null if the session ID does not exist.
     */
    @PostMapping("/getSession")
    //@ApiOperation(value = "Get the player associated with a session ID")
    public @ResponseBody Player getPlayer(@RequestBody SessionCheckPacket packet)
    {
        if(!sessionRepository.existsById(packet.getId()))
            return null;
        return sessionRepository.getPlayer(packet.getId());
    }

    @PostMapping("/allSessions")
    //@ApiOperation(value = "Get all the active sessions")
    public List<PlayerSession> getAllSessions()
    {
        sessionRepository.findAll().forEach(ps -> System.out.println(ps.getId()));
        return sessionRepository.findAll();
    }

    @DeleteMapping("/allSessions")
    //@ApiOperation(value = "Delete all active sessions")
    public void deleteAllSessions()
    {
        sessionRepository.findAll().forEach(
                playerSession -> sessionRepository.delete(playerSession)
        );
    }
}
