package com.cs309.quoridorApp.dataPackets;

import com.cs309.quoridorApp.dataPackets.toClient.RankPacket;
import com.cs309.quoridorApp.dataPackets.toServer.*;
import com.cs309.quoridorApp.dataPackets.toServer.chat.GetChatPacket;
import com.cs309.quoridorApp.dataPackets.toServer.chat.SendMessagePacket;
import com.cs309.quoridorApp.dataPackets.toServer.game.*;
import com.cs309.quoridorApp.dataPackets.toServer.lobby.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * The packet directly required by the main controllers.
 * Used to generate sub-packets, which hold the logic and interactions
 * for performing actions upon the server and game.
 */
public class SuperPacket extends Packet
{
    private String uuid = "";
    private String session = "AAAAAA";
    private String function = "";
    private Map<String, Object> data = new HashMap<>();

    /**
     * @param uuid - UUID as String
     */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    /**
     * @return UUID as String
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * @param session - Game ID as a 6 letter String
     */
    public void setSession(String session) {
        this.session = session;
    }

    /**
     * @return Game ID as a 6 letter String
     */
    public String getSession() {
        return session;
    }

    /**
     * @param function - Action name as String; all capital,
     *                 no separation between words
     */
    public void setFunction(String function) {
        this.function = function;
    }

    /**
     * @return Action name as String; all capital,
     *                    no separation between words
     */
    public String getFunction() {
        return function;
    }

    /**
     * @param data - Sub-JSON, used to create sub-packet
     */
    public void setData(Map data) {
        this.data = data;
    }

    /**
     * @return Sub-JSON which holds most of sub-packet
     */
    public Map getData() {
        return data;
    }

    /**
     * Converts given data to sub-packet, using function's class
     * type and returns sub-packet to execute
     * @return sub-packet given as function's class type
     */
    public ServerPacket read()
    {
        ServerPacket toReturn = new UnregisteredPacket();
        try
        {

            Packets pType = Packets.valueOf(function.toUpperCase());

            JSONObject nested = new JSONObject(data);

            toReturn = (ServerPacket) new ObjectMapper().readValue(nested.toString(), pType.clazz);
            toReturn.setSession(toReturn.getSession().toUpperCase());
            if(toReturn.getUuid().equals(""))
                toReturn.setUuid(uuid);
            if(toReturn.getSession().equals("AAAAAA"))
                toReturn.setSession(session);
        }
        catch(Exception e)
        {
            System.out.println(e);
        }

        return toReturn;
    }

    /**
     * Enum containing all of the function names and their corresponding
     * packet class types
     */
    public enum Packets
    {
        TEST(TestPacket.class),
        TESTBOARD(TestBoardPacket.class),
        ADDFRIEND(AddFriendPacket.class),
        CHECKFRIENDS(CheckFriendsPacket.class),
        CHECKREQUESTS(CheckFriendRequestsPacket.class),
        STARTSESSION(StartSessionPacket.class),
        SUPERMOVE(SuperMovePacket.class),
        //lobby
        CREATELOBBY(NewGamePacket.class),//
        JOINLOBBY(JoinLobbyPacket.class),//
        GETPLAYERS(GetPlayersPacket.class),
        GETLOBBYSTATE(GetStatePacket.class),
        LEAVELOBBY(LeaveLobbyPacket.class),//
        KICKPLAYER(KickPlayerPacket.class),//
        STARTGAME(StartGamePacket.class),//
        CHANGESETTINGS(ChangeSettingsPacket.class),//
        GETSETTINGS(GetSettingsPacket.class),
        //game
        MOVEPLAYER(MovePlayerPacket.class),
        PLACEWALL(PlaceWallPacket.class),
        GETBOARD(GetBoardPacket.class),
        //chat
        SENDCHAT(SendMessagePacket.class),
        GETCHAT(GetChatPacket.class),
        GETRANK(GetRankPacket.class),
        GETWINSLOSSES(GetPlayerStatsPacket.class),
        GETHISTORY(GetHistoryPacket.class)
        ;

        private Class clazz;

        Packets(Class clazz)
        {
            this.clazz = clazz;
        }
    }
}
