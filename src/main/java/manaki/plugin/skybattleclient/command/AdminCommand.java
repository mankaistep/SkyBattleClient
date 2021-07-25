package manaki.plugin.skybattleclient.command;

import com.google.common.collect.Lists;
import manaki.plugin.skybattleclient.SkyBattleClient;
import manaki.plugin.skybattleclient.gui.room.BattleType;
import manaki.plugin.skybattleclient.rank.player.RankedPlayers;
import manaki.plugin.skybattleclient.request.JoinRequest;
import manaki.plugin.skybattleclient.request.StartRequest;
import manaki.plugin.skybattleclient.team.Team;
import manaki.plugin.skybattleclient.team.player.TeamPlayer;
import manaki.plugin.skybattleclient.team.util.Teams;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdminCommand implements CommandExecutor {

    private SkyBattleClient plugin;

    public AdminCommand(SkyBattleClient plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String j, @NotNull String[] args) {

        try {
            if (args[0].equalsIgnoreCase("reload")) {
                SkyBattleClient.get().reloadConfig();
                sender.sendMessage("§aReloaded");
            }
            else if (args[0].equalsIgnoreCase("start")) {
                var id = args[1];
                List<Team> teams = Lists.newArrayList();

                // Parse
                boolean isRanked = Boolean.parseBoolean(args[2]);
                var ts = args[3];
                for (String ps : ts.split(";")) {
                    List<TeamPlayer> players = Lists.newArrayList();
                    for (String pn : ps.split(":")) {
                        var p = Bukkit.getPlayer(pn);
                        if (p == null) {
                            sender.sendMessage("Player " + pn + " is null!");
                            return false;
                        }
                        players.add(Teams.read(p));
                    }
                    teams.add(new Team(players));
                }

                // Send
                sender.sendMessage("§aSent!");
                plugin.getExecutor().sendStart(new StartRequest(id, teams, "ranked:" + isRanked));
            }

            else if (args[0].equalsIgnoreCase("join")) {
                var jr = new JoinRequest(args[1], "");
                plugin.getExecutor().sendJoin(jr);
            }

            else if (args[0].equalsIgnoreCase("addpoint")) {
                var bt = BattleType.valueOf(args[1]);
                var name = args[2];
                int point = Integer.parseInt(args[3]);

                var rp = RankedPlayers.get(name);
                var rd = rp.getRankData(bt);
                rd.addPoint(point);
                rp.save();

                sender.sendMessage("§aAdd ranked point done!");
            }

            else if (args[0].equalsIgnoreCase("subtractpoint")) {
                var bt = BattleType.valueOf(args[1]);
                var name = args[2];
                int point = Integer.parseInt(args[3]);

                var rp = RankedPlayers.get(name);
                var rd = rp.getRankData(bt);
                rd.subtractPoint(point);
                rp.save();

                sender.sendMessage("§6Subtracted ranked point done!");
            }

        }
        catch (ArrayIndexOutOfBoundsException e) {
            sendHelp(sender);
        }

        return false;
    }

    public void sendHelp(CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage("§a/skybattleclient(sbc) reload");
        sender.sendMessage("§a/skybattleclient(sbc) start <battleId> <isRanked> <player1>:<player2>;<player3>:<player4>");
        sender.sendMessage("§a/skybattleclient(sbc) join <player>");
        sender.sendMessage("§a/skybattleclient(sbc) addpoint <battleType(V1,V2,V3) <player> <point>");
        sender.sendMessage("§a/skybattleclient(sbc) subtractpoint <battleType(V1,V2,V3) <player> <point>");
    }

}
