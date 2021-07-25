package manaki.plugin.skybattleclient.game;

import com.google.common.collect.Maps;
import manaki.plugin.skybattleclient.game.notification.i.Notificatable;
import org.bukkit.entity.Player;

import java.util.Map;

public class Notifications {

    private static Map<String, Notificatable> data = Maps.newConcurrentMap();

    public static void add(String name, Notificatable noti) {
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

    public static Notificatable get(Player p) {
        return data.get(p.getName());
    }

}
