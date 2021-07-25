package manaki.plugin.skybattleclient.game.notification;

import manaki.plugin.skybattleclient.game.notification.i.Notificatable;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class UpNotification implements Notificatable {

    private final int top;
    private final int pointUp;

    public UpNotification(int top, int pointUp) {
        this.top = top;
        this.pointUp = pointUp;
    }

    @Override
    public void show(Player p) {
        p.sendMessage("§2[§a§l/skybattle§2] §aĐạt top " + top + ", cộng " + pointUp + " điểm xếp hạng");
        p.sendTitle("§a§lTOP #" + top, "§a+" + pointUp + " điểm xếp hạng", 5, 80, 20);
        p.playSound(p.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1, 1);
    }

}
