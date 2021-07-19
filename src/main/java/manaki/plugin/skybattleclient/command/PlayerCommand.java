package manaki.plugin.skybattleclient.command;

import manaki.plugin.skybattleclient.SkyBattleClient;
import manaki.plugin.skybattleclient.gui.BattleSelectGUI;
import manaki.plugin.skybattleclient.request.JoinRequest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlayerCommand implements CommandExecutor {

    private SkyBattleClient plugin;

    public PlayerCommand(SkyBattleClient plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String j, @NotNull String[] args) {

        if (args.length == 0) {
            var p = (Player) sender;
            BattleSelectGUI.open(p);

            return false;
        }
        else if (args[0].equalsIgnoreCase("khangia")) {
            var p = (Player) sender;
            plugin.getExecutor().sendJoin(new JoinRequest(p.getName(), ""));
            return false;
        }

        sender.sendMessage("");
        sender.sendMessage("§a§lSkybattle - SoraSky - Minevn.net");
        sender.sendMessage("§e/skybattle: §fMở menu Skybattle");
        sender.sendMessage("§e/skybattle khangia: §fChuyển sang khu vực skybattle để làm khán giả quan sát người chơi khác");
        sender.sendMessage("");

        return false;
    }

}
