package manaki.plugin.skybattleclient.game.notification;

import manaki.plugin.skybattleclient.game.notification.i.Notificatable;
import manaki.plugin.skybattleclient.gui.room.BattleType;
import manaki.plugin.skybattleclient.rank.player.RankedPlayers;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class DownNotification implements Notificatable {

    private final BattleType battleType;
    private final int top;

    public DownNotification(BattleType battleType, int top) {
        this.battleType = battleType;
        this.top = top;
    }

    public BattleType getBattleType() {
        return battleType;
    }

    public int getTop() {
        return top;
    }

    public int getPointDown(Player p) {
        var rp = RankedPlayers.get(p.getName());
        var rd = rp.getRankData(battleType);
        return RankedPlayers.calPointDown(this.top, rd.getType().getPointDown());
    }

    @Override
    public void show(Player p) {
        int down = getPointDown(p);
        p.sendMessage("§2[§a/skybattle§2] §6Đạt top " + top + ", trừ " + down + " điểm xếp hạng");
        p.sendTitle("§6§lTOP #" + top, "§c-" + down + " điểm xếp hạng", 5, 80, 20);
        p.playSound(p.getLocation(), Sound.ENTITY_GHAST_SCREAM, 1, 1);
    }

}
