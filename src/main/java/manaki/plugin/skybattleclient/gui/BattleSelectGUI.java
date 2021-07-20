package manaki.plugin.skybattleclient.gui;

import manaki.plugin.skybattleclient.SkyBattleClient;
import manaki.plugin.skybattleclient.gui.holder.GUIHolder;
import manaki.plugin.skybattleclient.gui.icon.Icons;
import manaki.plugin.skybattleclient.gui.model.BattleIconModel;
import manaki.plugin.skybattleclient.request.JoinRequest;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class BattleSelectGUI {

    public static void open(Player player) {
        Inventory inv = Bukkit.createInventory(new BattleGUIHolder(), 27, "§0§lCHỌN CHIẾN TRƯỜNG");
        player.openInventory(inv);
        player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1);

        Bukkit.getScheduler().runTaskAsynchronously(SkyBattleClient.get(), () -> {
            for (Integer slot : Icons.BACKGROUND_ICON.getSlots()) inv.setItem(slot, Icons.BACKGROUND_ICON.getIcon());
            inv.setItem(Icons.INFO_ICON.getSlot(), Icons.INFO_ICON.getIcon());
            inv.setItem(Icons.TUTORIAL_ICON.getSlot(), Icons.TUTORIAL_ICON.getIcon());
            inv.setItem(Icons.QUIT_ICON.getSlot(), Icons.QUIT_ICON.getIcon());

            for (BattleIconModel bim : Icons.BATTLE_ICONS.values()) {
                inv.setItem(bim.getSlot(), bim.getIcon());
            }
        });
    }


    public static void onClick(InventoryClickEvent e, Player p) {
        if (!(e.getInventory().getHolder() instanceof BattleGUIHolder)) return;
        int slot = e.getSlot();

        // Quit button
        if (slot == Icons.QUIT_ICON.getSlot()) {
            p.closeInventory();
            return;
        }

        // Spectator button
        if (slot == Icons.INFO_ICON.getSlot()) {
            SkyBattleClient.get().getExecutor().sendJoin(new JoinRequest(p.getName(), ""));
            p.closeInventory();
            return;
        }

        for (BattleIconModel bim : Icons.BATTLE_ICONS.values()) {
            if (bim.getSlot() == slot) {
                RoomSelectGUI.open(p, bim.getBattleId());
            }
        }
    }

}

class BattleGUIHolder implements GUIHolder {

    @Override
    public @NotNull Inventory getInventory() {
        return null;
    }

}
