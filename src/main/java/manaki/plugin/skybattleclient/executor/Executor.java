package manaki.plugin.skybattleclient.executor;


import manaki.plugin.skybattleclient.SkyBattleClient;
import manaki.plugin.skybattleclient.request.JoinRequest;
import manaki.plugin.skybattleclient.request.StartRequest;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Executor {

    private final Plugin plugin;

    public Executor(Plugin plugin) {
        this.plugin = plugin;
    }

    public void sendStart(StartRequest sr) {
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
