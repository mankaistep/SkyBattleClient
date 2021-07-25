package manaki.plugin.skybattleclient.listener;

import manaki.plugin.skybattleclient.SkyBattleClient;
import manaki.plugin.skybattleclient.game.Notifications;
import manaki.plugin.skybattleclient.gui.*;
import manaki.plugin.skybattleclient.gui.holder.GUIHolder;
import manaki.plugin.skybattleclient.gui.room.Rooms;
import manaki.plugin.skybattleclient.rank.player.RankedPlayers;
import manaki.plugin.skybattleclient.rank.reward.RankRewards;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onJoinNoti(PlayerJoinEvent e) {
        var p = e.getPlayer();
        Notifications.show(p);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent e) {
        var p = e.getPlayer();

        // Ranked cache remove
        RankedPlayers.removeCache(p.getName());

        // Remove from rooms
        Rooms.removePlayer(p);

        // Check if start out
        if (p.hasMetadata("skybattle.prepare-start")) {
            e.setQuitMessage(null);
            Bukkit.getScheduler().runTask(SkyBattleClient.get(), () -> p.removeMetadata("skybattle.prepare-start", SkyBattleClient.get()));
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!(e.getInventory().getHolder() instanceof GUIHolder)) return;
        e.setCancelled(true);

        var p = (Player) e.getWhoClicked();
        p.playSound(p.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1);

        MainGUI.onClick(e, p);
        RoomSelectGUI.onClick(e, p);
        TeamSelectGUI.onClick(e, p);
        TypeSelectGUI.onClick(e, p);
        RankRewards.onClick(e, p);
        RankRewards.onClickSelect(e, p);
        MapGUI.onClick(e, p);
        CreateGUI.onClick(e, p);
    }

    @EventHandler
    public void onDrag(InventoryDragEvent e) {
        if (!(e.getInventory().getHolder() instanceof GUIHolder)) return;
        e.setCancelled(true);
    }

}
