package manaki.plugin.skybattleclient.game.notification;

import manaki.plugin.skybattleclient.game.notification.i.Notificatable;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class AfkNotification implements Notificatable {

    private final int pointDown;

    public AfkNotification(int pointDown) {
        this.pointDown = pointDown;
    }

    @Override
    public void show(Player p) {
        p.sendMessage("§2[§a§l/skybattle§2] §cHệ thống phát hiện hành vi AFK của bạn, trừ " + pointDown + " điểm xếp hạng");
        p.sendTitle("§c§lPhạt AFK", "§c-" + pointDown + " điểm xếp hạng", 5, 80, 20);
        p.playSound(p.getLocation(), Sound.ENTITY_GHAST_SCREAM, 1, 1);
    }
}
