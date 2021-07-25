package manaki.plugin.skybattleclient.executor;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import manaki.plugin.skybattleclient.SkyBattleClient;
import manaki.plugin.skybattleclient.gui.icon.Icons;
import manaki.plugin.skybattleclient.gui.room.GameType;
import manaki.plugin.skybattleclient.gui.room.Room;
import manaki.plugin.skybattleclient.gui.room.team.TeamIcon;
import manaki.plugin.skybattleclient.request.JoinRequest;
import manaki.plugin.skybattleclient.request.StartRequest;
import manaki.plugin.skybattleclient.team.Team;
import manaki.plugin.skybattleclient.team.player.TeamPlayer;
import manaki.plugin.skybattleclient.team.util.Teams;
import mk.plugin.santory.utils.Tasks;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class Executor {

    private final Plugin plugin;

    // Room pending
    private final Set<Integer> startedRooms;

    public Executor(Plugin plugin) {
        this.plugin = plugin;
        startedRooms = Sets.newHashSet();
    }

    public void sendStart(Room room) {
        int id = room.getId();
        if (startedRooms.contains(id)) return;

        // Add and remove later
        startedRooms.add(room.getId());
        Tasks.sync(() -> {
            startedRooms.remove(id);
        }, 200);

        // Get data
        var players = room.getTeamPlayers();
        var battleId = room.getBattleId();

        // Random
        if (battleId == null) {
            var l = Lists.newArrayList(Icons.BATTLE_ICONS.keySet());
            battleId = l.get(new Random().nextInt(l.size()));
        }

        List<Team> teams = Lists.newArrayList();

        // NULL add
        for (Player p : players.get(TeamIcon.NULL)) {
            for (TeamIcon ti : TeamIcon.values()) {
                if (ti == TeamIcon.NULL) continue;
                if (players.get(ti).size() < room.getBattleType().getTeamSize()) {
                    players.get(ti).add(p);
                    break;
                }
            }
        }

        // Add team not NULL
        for (Map.Entry<TeamIcon, Set<Player>> e : players.entrySet()) {
            var ti = e.getKey();
            if (ti != TeamIcon.NULL) {
                var set = e.getValue();
                if (set.size() == 0) continue;
                var pl = set.stream().map(Teams::read).collect(Collectors.toList());
                teams.add(new Team(pl));
            }
        }

        // Send
        var sr = new StartRequest(battleId, teams, "ranked:" + (room.getGameType() == GameType.RANKED));
        sendStart(sr);
    }

    public void sendStart(StartRequest sr) {
        // Add tag check
        for (Team t : sr.getTeams()) {
            for (TeamPlayer tp : t.getPlayers()) {
                var pn = tp.getName();
                var p = Bukkit.getPlayer(pn);
                if (p != null) {
                    p.setMetadata("skybattle.prepare-start", new FixedMetadataValue(SkyBattleClient.get(), ""));
                }
            }
        }

        // Send request to game server
        var rs = sr.toString();
        var stream = new ByteArrayOutputStream();
        var out = new DataOutputStream(stream);
        try {
            out.writeUTF("skybattle-start");
            out.writeUTF(rs);
        } catch (IOException e) {
            SkyBattleClient.get().getLogger().severe("An I/O error occurred!");
        }
        ((Player) Bukkit.getOnlinePlayers().toArray()[0]).sendPluginMessage(SkyBattleClient.get(), SkyBattleClient.CHANNEL, stream.toByteArray());
    }

    public void sendJoin(JoinRequest jr) {
        // Send request to game server
        var js = jr.toString();
        var stream = new ByteArrayOutputStream();
        var out = new DataOutputStream(stream);
        try {
            out.writeUTF("skybattle-join");
            out.writeUTF(js);
        } catch (IOException e) {
            SkyBattleClient.get().getLogger().severe("An I/O error occurred!");
        }
        ((Player) Bukkit.getOnlinePlayers().toArray()[0]).sendPluginMessage(SkyBattleClient.get(), SkyBattleClient.CHANNEL, stream.toByteArray());
    }

}
