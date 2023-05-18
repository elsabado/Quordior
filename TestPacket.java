package com.cs309.quoridorApp.dataPackets;

import com.cs309.quoridorApp.dataPackets.toClient.ClientPacket;
import com.cs309.quoridorApp.dataPackets.toClient.ReturnTestPacket;
import com.cs309.quoridorApp.dataPackets.toServer.ServerPacket;
import com.cs309.quoridorApp.player.PlayerList;
import com.cs309.quoridorApp.repository.HistoryRepository;
import com.cs309.quoridorApp.repository.PlayerRepository;
import com.cs309.quoridorApp.repository.PlayerStatsRepository;
import com.cs309.quoridorApp.repository.SessionRepository;

/**
 * Packet meant to test formatting
 */
public class TestPacket extends ServerPacket
{

    private int number = 0;
    private int bumber = -1;

    public String getPl() {
        return pl;
    }

    public void setPl(String pl) {
        this.pl = pl;
    }

    private String pl = null;

    public String getPll() {
        return pll;
    }

    public void setPll(String pll) {
        this.pll = pll;
    }

    private String pll = "";

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getBumber() {
        return bumber;
    }

    public void setBumber(int bumber) {
        this.bumber = bumber;
    }

    @Override
    public ClientPacket execute(PlayerRepository players, SessionRepository sessions, PlayerStatsRepository stats, HistoryRepository history) {
        ClientPacket toReturn = new ReturnTestPacket();
        toReturn.setMessage("" + number + " " + bumber);
        return toReturn;
    }
}
