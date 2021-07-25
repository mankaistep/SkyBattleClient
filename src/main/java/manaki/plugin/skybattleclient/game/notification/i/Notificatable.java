package manaki.plugin.skybattleclient.game.notification.i;

import manaki.plugin.skybattleclient.game.PlayerResult;
import org.bukkit.entity.Player;

public abstract class Notificatable {

    private PlayerResult result;

    public Notificatable(PlayerResult result) {
        this.result = result;
    }

    public PlayerResult getResult() {
        return result;
    }

    public abstract void show(Player p);

}
