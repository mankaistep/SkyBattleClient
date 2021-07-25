package manaki.plugin.skybattleclient.gui;

import manaki.plugin.skybattleclient.SkyBattleClient;
import manaki.plugin.skybattleclient.gui.holder.GUIHolder;
import manaki.plugin.skybattleclient.gui.icon.Icons;
import manaki.plugin.skybattleclient.gui.model.BattleIconModel;
import manaki.plugin.skybattleclient.gui.room.GameType;
import mk.plugin.santory.utils.ItemStackManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class MapGUI {

    public static void open(Player p) {
        var inv = Bukkit.createInventory(new MapGUIHolder(), 9, "§0§lCHỌN MAP");
        p.openInventory(inv);

        Bukkit.getScheduler().runTaskAsynchronously(SkyBattleClient.get(), () -> {
            inv.setItem(0, getRandom());
            int slot = 1;
            for (Map.Entry<String, BattleIconModel> e : Icons.BATTLE_ICONS.entrySet()) {
                inv.setItem(e.getValue().getSlot(), e.getValue().getIcon());
                slot++;
            }
        });
    }

    public static void onClick(InventoryClickEvent e, Player p) {
        if (!(e.getInventory().getHolder() instanceof MapGUIHolder)) return;

        int slot = e.getSlot();

        if (slot == 0) {
            // Random select
            TypeSelectGUI.open(p, null, GameType.NORMAL);
        }
        else {
            for (Map.Entry<String, BattleIconModel> entry : Icons.BATTLE_ICONS.entrySet()) {
                var id = entry.getKey();
                var bim = entry.getValue();
                if (bim.getSlot() == slot) {
                    TypeSelectGUI.open(p, id, GameType.NORMAL);
                    break;
                }
            }
        }
    }

    public static ItemStack getRandom() {
        var is = new ItemStack(Material.GLASS_PANE);
        var ism = new ItemStackManager(SkyBattleClient.get(), is);
        ism.setModelData(53);
        ism.setName("§a§lNgẫu nhiên");

        return is;
    }

}

class MapGUIHolder implements GUIHolder {

    @Override
    public @NotNull Inventory getInventory() {
        return null;
    }
}

