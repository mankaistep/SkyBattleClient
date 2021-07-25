package manaki.plugin.skybattleclient.game.notification;

import manaki.plugin.skybattleclient.game.PlayerResult;
import manaki.plugin.skybattleclient.game.notification.i.Notificatable;
import manaki.plugin.skybattleclient.gui.room.BattleType;
import manaki.plugin.skybattleclient.rank.player.RankedPlayers;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class UpNotification extends Notificatable {

    private final BattleType battleType;
    private final int top;
    private final boolean isWinner;

    public UpNotification(PlayerResult pr, BattleType battleType, int top, boolean isWinner) {
        super(pr);
        this.battleType = battleType;
        this.top = top;
        this.isWinner = isWinner;
    }

    public BattleType getBattleType() {
        return battleType;
    }

    public int getTop() {
        return top;
    }

    public int getPointUp(String name) {
        var rp = RankedPlayers.get(name);
        var rd = rp.getRankData(battleType);
        return RankedPlayers.calPointUp(this.top, rd.getType().getPointUp(), isWinner);
    }

    @Override
    public void show(Player p) {
        int up = getPointUp(p.getName());

        if (!isWinner) {
            p.sendMessage("§2[§a/skybattle§2] §aĐạt top " + top + ", cộng " + up + " điểm xếp hạng");
            p.sendTitle("§a§lTOP #" + top, "§a+" + up + " điểm xếp hạng", 5, 80, 20);
        }
        else {
            p.sendMessage("§2[§a/skybattle§2] §aĐạt top " + top + " - chiến thắng, cộng " + up + " điểm xếp hạng");
            p.sendTitle("§e§lTOP #" + top + " - Chiến thắng", "§a+" + up + " điểm xếp hạng", 5, 80, 20);
        }
        p.playSound(p.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1, 1);
    }

}
