package manaki.plugin.skybattleclient.listener;

import manaki.plugin.skybattleclient.gui.BattleSelectGUI;
import manaki.plugin.skybattleclient.gui.RoomSelectGUI;
import manaki.plugin.skybattleclient.gui.TeamSelectGUI;
import manaki.plugin.skybattleclient.gui.TypeSelectGUI;
import manaki.plugin.skybattleclient.gui.holder.GUIHolder;
import manaki.plugin.skybattleclient.gui.room.Rooms;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent e) {
        var p = e.getPlayer();

        // Remove from rooms
        Rooms.removePlayer(p);

        // Check if start out
        if (p.hasMetadata("skybattle.prepare-start")) {
            e.setQuitMessage(null);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!(e.getInventory().getHolder() instanceof GUIHolder)) return;
        e.setCancelled(true);

        var p = (Player) e.getWhoClicked();
        p.playSound(p.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1);

        BattleSelectGUI.onClick(e, p);
        RoomSelectGUI.onClick(e, p);
        TeamSelectGUI.onClick(e, p);
        TypeSelectGUI.onClick(e, p);
    }

    @EventHandler
    public void onDrag(InventoryDragEvent e) {
        if (!(e.getInventory().getHolder() instanceof GUIHolder)) return;
        e.setCancelled(true);
    }

}
