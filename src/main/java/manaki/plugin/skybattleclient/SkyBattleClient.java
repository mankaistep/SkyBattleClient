package manaki.plugin.skybattleclient;

import manaki.plugin.skybattleclient.command.AdminCommand;
import manaki.plugin.skybattleclient.command.PlayerCommand;
import manaki.plugin.skybattleclient.executor.Executor;
import manaki.plugin.skybattleclient.game.GameResult;
import manaki.plugin.skybattleclient.game.Notifications;
import manaki.plugin.skybattleclient.game.notification.AfkNotification;
import manaki.plugin.skybattleclient.game.notification.DownNotification;
import manaki.plugin.skybattleclient.game.notification.UpNotification;
import manaki.plugin.skybattleclient.gui.holder.GUIHolder;
import manaki.plugin.skybattleclient.gui.icon.Icons;
import manaki.plugin.skybattleclient.gui.room.BattleType;
import manaki.plugin.skybattleclient.listener.PlayerListener;
import manaki.plugin.skybattleclient.placeholder.RankPlaceholder;
import manaki.plugin.skybattleclient.rank.player.RankedPlayers;
import manaki.plugin.skybattleclient.rank.reward.RankRewards;
import manaki.plugin.skybattleclient.request.util.Requests;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;

public class SkyBattleClient extends JavaPlugin implements @NotNull PluginMessageListener {

    public static final String CHANNEL = "game:skybattle";

    private Executor executor;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.reloadConfig();
        this.executor = new Executor(this);

        this.getCommand("skybattleclient").setExecutor(new AdminCommand(this));
        this.getCommand("skybattle").setExecutor(new PlayerCommand(this));

        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, CHANNEL);
        this.getServer().getMessenger().registerIncomingPluginChannel(this, CHANNEL, this);

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new RankPlaceholder().register();
        }
    }

    @Override
    public void onDisable() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.getOpenInventory();
            if (p.getOpenInventory().getTopInventory().getHolder() instanceof GUIHolder) p.closeInventory();
        }
    }

    @Override
    public void reloadConfig() {
        Icons.reload(this);
        RankRewards.reload(this)    ;
    }

    public Executor getExecutor() {
        return executor;
    }

    public static SkyBattleClient get() {
        return JavaPlugin.getPlugin(SkyBattleClient.class);
    }

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, @NotNull byte[] msg) {
        if (!channel.equals(CHANNEL)) return;

        var in = new DataInputStream(new ByteArrayInputStream(msg));
        String type = null;
        String data = null;
        try {
            type = in.readUTF();
            data = in.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Start request
        if (type.equalsIgnoreCase("skybattle-quit")) {
            var qr = Requests.parseQuit(data);
            var name = qr.getPlayer();

            Bukkit.getScheduler().runTaskLater(this, () -> {
                var p = Bukkit.getPlayer(name);
                if (p == null) return;

                p.setMetadata("skybattle-quit", new FixedMetadataValue(SkyBattleClient.get(), ""));
                Bukkit.getScheduler().runTaskLater(SkyBattleClient.get(), () -> {
                    p.removeMetadata("skybattle-quit", SkyBattleClient.get());
                }, 100);

                SkyBattleClient.get().getLogger().info("Added tag to " + name);
            }, 20);

        }

        // Battle result
        if (type.equalsIgnoreCase("game-result")) {
            var br = GameResult.parse(data);
            BattleType bt = BattleType.parse(br.getType());

            // Afk players = top 8
            for (String name : br.getAfks()) {
                var rp = RankedPlayers.get(name);
                var rd = rp.getRankData(bt);

                // Subtract
                int down = RankedPlayers.calPointDown(8, rd.getType().getPointDown());
                rd.subtractPoint(down);
                rp.save();

                // Pending noti
                Notifications.add(name, new AfkNotification(down));
            }

            // Top
            for (Map.Entry<String, Integer> e : br.getTops().entrySet()) {
                var name = e.getKey();
                var top = e.getValue();

                var rp = RankedPlayers.get(name);
                var rd = rp.getRankData(bt);

                // Add or subtract
                if (top >= 4) {
                    int up = RankedPlayers.calPointUp(top, rd.getType().getPointUp());
                    rd.addPoint(up);
                    Notifications.add(name, new UpNotification(top, up));
                }
                else {
                    int down = RankedPlayers.calPointDown(top, rd.getType().getPointDown());
                    rd.subtractPoint(down);
                    Notifications.add(name, new DownNotification(top, down));
                }
                rp.save();
            }
        }
    }
}
