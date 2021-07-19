package manaki.plugin.skybattleclient;

import manaki.plugin.skybattleclient.command.AdminCommand;
import manaki.plugin.skybattleclient.executor.Executor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

public class SkyBattleClient extends JavaPlugin implements @NotNull PluginMessageListener {

    public static final String CHANNEL = "game:skybattle";

    private Executor executor;

    @Override
    public void onEnable() {
        this.executor = new Executor(this);

        this.getCommand("skybattleclient").setExecutor(new AdminCommand(this));

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, CHANNEL);
        this.getServer().getMessenger().registerIncomingPluginChannel(this, CHANNEL, this);
    }

    public Executor getExecutor() {
        return executor;
    }

    public static SkyBattleClient get() {
        return JavaPlugin.getPlugin(SkyBattleClient.class);
    }

    @Override
    public void onPluginMessageReceived(@NotNull String s, @NotNull Player player, @NotNull byte[] bytes) {

    }
}
