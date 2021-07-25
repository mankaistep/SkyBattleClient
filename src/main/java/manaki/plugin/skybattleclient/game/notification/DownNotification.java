package manaki.plugin.skybattleclient.game.notification;

import manaki.plugin.skybattleclient.game.notification.i.Notificatable;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class DownNotification implements Notificatable {

    private final int top;
    private final int pointDown;

    public DownNotification(int top, int pointDown) {
        this.top = top;
        this.pointDown = pointDown;
    }

    @Override
    public void show(Player p) {
        p.sendMessage("§2[§a§l/skybattle§2] §6Đạt top " + top + ", trừ " + pointDown + " điểm xếp hạng");
        p.sendTitle("§6§lTOP #" + top, "§c-" + pointDown + " điểm xếp hạng", 5, 80, 20);
        p.playSound(p.getLocation(), Sound.ENTITY_GHAST_SCREAM, 1, 1);
    }

}
