package manaki.plugin.skybattleclient.game.notification;

import manaki.plugin.skybattleclient.game.PlayerResult;
import manaki.plugin.skybattleclient.game.notification.i.Notificatable;
import manaki.plugin.skybattleclient.gui.room.BattleType;
import manaki.plugin.skybattleclient.rank.player.RankedPlayers;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class DownNotification extends Notificatable {

    private final BattleType battleType;
    private final int top;

    public DownNotification(PlayerResult pr, BattleType battleType, int top) {
        super(pr);
        this.battleType = battleType;
        this.top = top;
    }

    public BattleType getBattleType() {
        return battleType;
    }

    public int getTop() {
        return top;
    }

    public int getPointDown(String name) {
        var rp = RankedPlayers.get(name);
        var rd = rp.getRankData(battleType);
        return RankedPlayers.calPointDown(this.top, rd.getType().getPointDown());
    }

    @Override
    public void show(Player p) {
        int down = getPointDown(p.getName());
        p.sendMessage("§2[§a/skybattle§2] §6Đạt top " + top + ", trừ " + down + " điểm xếp hạng");
        p.sendTitle("§6§lTOP #" + top, "§c-" + down + " điểm xếp hạng", 5, 80, 20);
        p.playSound(p.getLocation(), Sound.ENTITY_GHAST_SCREAM, 1, 1);
    }

}
