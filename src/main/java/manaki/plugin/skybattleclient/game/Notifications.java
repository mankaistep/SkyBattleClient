package manaki.plugin.skybattleclient.game;

import co.aikar.util.JSONUtil;
import com.google.common.collect.Maps;
import manaki.plugin.skybattleclient.game.notification.i.Notificatable;
import manaki.plugin.skybattleclient.rank.player.RankedPlayers;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;

public class Notifications {

    private static Map<String, Notificatable> data = Maps.newConcurrentMap();

    public static Map<String, Notificatable> getData() {
        return data;
    }

    public static void add(String name, Notificatable noti) {
        data.put(name, noti);

        var p = Bukkit.getPlayer(name);
        if (p != null) {
            RankedPlayers.doNoti(p.getName());
        }
    }

    public static boolean has(String name) {
        return data.containsKey(name);
    }

    public static void show(Player p) {
        var name = p.getName();
        if (!has(name)) return;
        var noti = data.get(name);
        noti.show(p);
        data.remove(p.getName());
    }

    public static Notificatable get(String name) {
        return data.get(name);
    }

    public static void remove(String name) {
        data.remove(name);
    }

}
