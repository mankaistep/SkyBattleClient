package manaki.plugin.skybattleclient.gui.room;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import manaki.plugin.skybattleclient.gui.room.team.TeamIcon;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Set;

public class Room {

    private final int id;

    private final String battleId;
    private final BattleType battleType;
    private final GameType gameType;

    private final Map<TeamIcon, Set<Player>> players;
    private boolean countdown;
    private long startCount;
    private boolean delete;
    private long deleteCount;
    private boolean started;

    public Room(int id, String battleId, BattleType battleType, GameType gameType, Player creator) {
        this.id = id;
        this.battleId = battleId;
        this.battleType = battleType;
        this.gameType = gameType;
        this.startCount = 0;
        this.deleteCount = 0;
        this.started = false;
        this.players = Maps.newConcurrentMap();

        this.addToNull(creator);
        this.countdown = false;
    }

    public int getId() {
        return id;
    }

    public String getBattleId() {
        return battleId;
    }

    public BattleType getBattleType() {
        return battleType;
    }

    public GameType getGameType() {
        return gameType;
    }

    public Map<TeamIcon, Set<Player>> getTeamPlayers() {
        return players;
    }

    public void addToNull(Player p) {
        getPlayers(TeamIcon.NULL).add(p);
    }

    public void changeTeam(Player p, TeamIcon team) {
        // Remove from current team
        var currentTeam = getTeam(p);

        // Add to NULL
        this.addToNull(p);
        this.getPlayers(currentTeam).remove(p);

        if (team != currentTeam) {
            // Add to team
            getPlayers(team).add(p);

            // Remove from NULL
            this.getPlayers(TeamIcon.NULL).remove(p);
        }
    }

    public TeamIcon getTeam(Player p) {
        for (Map.Entry<TeamIcon, Set<Player>> e : this.players.entrySet()) {
            if (e.getValue().contains(p)) return e.getKey();
        }
        return null;
    }

    public Set<Player> getPlayers() {
        Set<Player> list = Sets.newHashSet();
        for (Set<Player> pl : this.players.values()) {
            list.addAll(pl);
        }
        return list;
    }

    public Set<Player> getPlayers(TeamIcon team) {
        if (!this.players.containsKey(team)) this.players.put(team, Sets.newHashSet());
        return this.players.get(team);
    }

    public boolean canStarted() {
        return this.getPlayers().size() == getMaxPlayers();
    }

    public int getMaxPlayers() {
//        return this.battleType.getTeamSize() * 8;
        return 2;
    }

    public boolean isCountdowning() {
        return countdown;
    }

    public void setStartCountdown(boolean value) {
        this.countdown = value;
        this.startCount = System.currentTimeMillis();
    }

    public long getStartCount() {
        return startCount;
    }

    public boolean isDeleting() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
        this.deleteCount = System.currentTimeMillis();
    }

    public long getDeleteCount() {
        return deleteCount;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public boolean canJoin() {
        return !canStarted() && !isDeleting() && !isCountdowning();
    }

}
