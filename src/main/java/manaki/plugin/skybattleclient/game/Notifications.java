package manaki.plugin.skybattleclient.game;

import com.google.common.collect.Maps;
import manaki.plugin.skybattleclient.game.notification.i.Notificatable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;

public class Notifications {

    private static Map<String, Notificatable> data = Maps.newConcurrentMap();

    public static void add(String name, Notificatable noti) {
        var p = Bukkit.getPlayer(name);

        // Send noti
        if (p != null) {
            noti.show(p);
            return;
        }

        // Add to map
        data.put(name, noti);
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

}
