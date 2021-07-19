package manaki.plugin.skybattleclient.team;

import manaki.plugin.skybattleclient.team.player.TeamPlayer;

import java.util.List;

public class Team {

    private final List<TeamPlayer> players;

    public Team(List<TeamPlayer> players) {
        this.players = players;
    }

    public List<TeamPlayer> getPlayers() {
        return players;
    }
}
