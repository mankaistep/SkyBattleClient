package manaki.plugin.skybattleclient.event;

import manaki.plugin.skybattleclient.game.PlayerResult;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerEndGameEvent extends PlayerEvent {

    private final PlayerResult result;

    public PlayerEndGameEvent(@NotNull Player who, PlayerResult result) {
        super(who);
        this.result = result;
    }

    public PlayerResult getResult() {
        return result;
    }


    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
        return handlers;
    }

    public static final HandlerList getHandlerList() {
        return handlers;
    }

}
