package manaki.plugin.skybattleclient.gui;

import manaki.plugin.skybattleclient.SkyBattleClient;
import manaki.plugin.skybattleclient.gui.holder.GUIHolder;
import manaki.plugin.skybattleclient.gui.room.GameType;
import manaki.plugin.skybattleclient.gui.room.Rooms;
import manaki.plugin.skybattleclient.util.Utils;
import mk.plugin.santory.utils.ItemStackManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CreateGUI {

    private static final int NORMAL_SLOT = 3;
    private static final int RANK_SLOT = 5;

    public static void open(Player p) {
        var inv = Bukkit.createInventory(new CreateGUIHolder(), 9, "§0§lCHỌN LOẠI GAME");
        p.openInventory(inv);

        Bukkit.getScheduler().runTaskAsynchronously(SkyBattleClient.get(), () -> {
            for (int i = 0 ; i < inv.getSize() ; i++) inv.setItem(i, Utils.getBackIcon());
            inv.setItem(NORMAL_SLOT, getNormal());
            inv.setItem(RANK_SLOT, getRank());
        });
    }

    public static void onClick(InventoryClickEvent e, Player p) {
        if (!(e.getInventory().getHolder() instanceof CreateGUIHolder)) return;

        int slot = e.getSlot();

        if (slot == NORMAL_SLOT) {
            // Open map gui
            MapGUI.open(p);
        }
        else if (slot == RANK_SLOT) {
            // Open type select
            TypeSelectGUI.open(p, null, GameType.RANKED);
        }
    }

    public static ItemStack getNormal() {
        var is = new ItemStack(Material.GREEN_BED);
        var ism = new ItemStackManager(SkyBattleClient.get(), is);
        ism.setName("§a§lThường");

        return is;
    }

    public static ItemStack getRank() {
        var is = new ItemStack(Material.RED_BANNER);
        var ism = new ItemStackManager(SkyBattleClient.get(), is);
        ism.setName("§c§lHạng");

        return is;
    }

}

class CreateGUIHolder implements GUIHolder {

    @Override
    public @NotNull Inventory getInventory() {
        return null;
    }
}
