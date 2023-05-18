package com.cs309.quoridorApp.dataPackets;

import com.cs309.quoridorApp.game.Game;
import com.cs309.quoridorApp.player.Player;
import com.cs309.quoridorApp.player.PlayerList;

public class ListedPlayer
{

    private String name;
    private String role;
    private int teamNumber;

    public ListedPlayer(String name, String role, int teamNumber)
    {
        this.name = name;
        this.role = role;
        this.teamNumber = teamNumber;
    }

    public ListedPlayer(Game game, Player player)
    {
        PlayerList cl = game.getClientList();
        name = player.getUsername();
        role = cl.isHost(player) ? "host" : "client";
        teamNumber = cl.getTeam(player);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getTeamNumber() {
        return teamNumber;
    }

    public void setTeamNumber(int teamNumber) {
        this.teamNumber = teamNumber;
    }

}
