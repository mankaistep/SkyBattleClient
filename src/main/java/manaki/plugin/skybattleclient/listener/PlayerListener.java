package manaki.plugin.skybattleclient.listener;

import manaki.plugin.skybattleclient.SkyBattleClient;
import manaki.plugin.skybattleclient.game.Notifications;
import manaki.plugin.skybattleclient.game.notification.DownNotification;
import manaki.plugin.skybattleclient.game.notification.UpNotification;
import manaki.plugin.skybattleclient.gui.*;
import manaki.plugin.skybattleclient.gui.holder.GUIHolder;
import manaki.plugin.skybattleclient.gui.room.Rooms;
import manaki.plugin.skybattleclient.rank.RankData;
import manaki.plugin.skybattleclient.rank.player.RankedPlayers;
import manaki.plugin.skybattleclient.rank.reward.RankRewards;
import manaki.plugin.skybattleclient.util.Utils;
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
        if (!Notifications.has(p.getName())) return;
        Bukkit.getScheduler().runTaskLaterAsynchronously(SkyBattleClient.get(), () -> {
            var noti = Notifications.get(p);
            var rp = RankedPlayers.get(p.getName());

            // Show
            Notifications.show(p);

            // Save
            if (noti instanceof UpNotification) {

                var rd = rp.getRankData(((UpNotification) noti).getBattleType());
                int up = ((UpNotification) noti).getPointUp(p);

                var rtbefore = rd.getType();
                var rgbefore = rd.getGrade();

                rd.addPoint(up);

                if (rd.getGrade() != rgbefore || rd.getType() != rtbefore) {
                    Bukkit.getScheduler().runTaskLaterAsynchronously(SkyBattleClient.get(), () -> {
                        p.sendTitle("§e§lThăng hạng!", Utils.getRankDisplay(new RankData(0, rtbefore, rgbefore)) + " >> §r" + Utils.getRankDisplay(rd));
                        p.playSound(p.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1, 1);
                    }, 20);
                }
            }
            else if (noti instanceof DownNotification) {
                var rd = rp.getRankData(((DownNotification) noti).getBattleType());
                int down = ((DownNotification) noti).getPointDown(p);

                var rtbefore = rd.getType();
                var rgbefore = rd.getGrade();

                rd.subtractPoint(down);

                if (rd.getGrade() != rgbefore || rd.getType() != rtbefore) {
                    Bukkit.getScheduler().runTaskLaterAsynchronously(SkyBattleClient.get(), () -> {
                        p.sendTitle("§c§lGiáng hạng", Utils.getRankDisplay(new RankData(0, rtbefore, rgbefore)) + " >> §r" + Utils.getRankDisplay(rd));
                        p.playSound(p.getLocation(), Sound.ENTITY_GHAST_SCREAM, 1, 1);
                    }, 20);
                }
            }

            rp.save();
        }, 50);
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
