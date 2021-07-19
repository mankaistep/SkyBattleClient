package manaki.plugin.skybattleclient.command;

import com.google.common.collect.Lists;
import manaki.plugin.skybattleclient.SkyBattleClient;
import manaki.plugin.skybattleclient.request.JoinRequest;
import manaki.plugin.skybattleclient.request.StartRequest;
import manaki.plugin.skybattleclient.team.Team;
import manaki.plugin.skybattleclient.team.player.TeamPlayer;
import manaki.plugin.skybattleclient.team.util.Teams;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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
            if (args[0].equalsIgnoreCase("start")) {
                var id = args[1];
                List<Team> teams = Lists.newArrayList();

                // Parse
                var ts = args[2];
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
                plugin.getExecutor().sendStart(new StartRequest(id, teams, ""));
            }

            else if (args[0].equalsIgnoreCase("join")) {
                var jr = new JoinRequest(args[1], "");
                plugin.getExecutor().sendJoin(jr);
            }
        }
        catch (ArrayIndexOutOfBoundsException e) {
            sendHelp(sender);
        }

        return false;
    }

    public void sendHelp(CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage("§a/skybattleclient(sbc) start <battleId> <player1>:<player2>;<player3>:<player4>");
        sender.sendMessage("§a/skybattleclient(sbc) join <player>");
    }

}
