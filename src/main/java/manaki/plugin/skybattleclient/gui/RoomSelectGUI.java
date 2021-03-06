package manaki.plugin.skybattleclient.gui;

import com.google.common.collect.Lists;
import manaki.plugin.skybattleclient.SkyBattleClient;
import manaki.plugin.skybattleclient.gui.holder.GUIHolder;
import manaki.plugin.skybattleclient.gui.room.Rooms;
import manaki.plugin.skybattleclient.util.Utils;
import mk.plugin.santory.utils.ItemStackManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RoomSelectGUI {

    private static final int CREATE_BUTTON_SLOT = 2;
    private static final int EXIT_BUTTON_SLOT = 6;
    private static final List<Integer> ROOM_SLOTS = Lists.newArrayList(19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43);

    public static void open(Player player) {
        Inventory inv = Bukkit.createInventory(new RoomGUIHolder(), 54, "§0§lCHỌN PHÒNG");
        player.openInventory(inv);

        Bukkit.getScheduler().runTaskAsynchronously(SkyBattleClient.get(), () -> {
            for (int i = 0 ; i < inv.getSize() ; i++) if (!ROOM_SLOTS.contains(i)) inv.setItem(i, Utils.getBackIcon());
            inv.setItem(CREATE_BUTTON_SLOT, getCreateButton());
            inv.setItem(EXIT_BUTTON_SLOT, getExitButton());

            // Slots
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (player.getOpenInventory().getTopInventory() != inv) {
                        this.cancel();
                        return;
                    }
                    for (Integer slot : ROOM_SLOTS) inv.setItem(slot, null);
                    var rooms = Rooms.getRooms();
                    for (int i = 0; i < rooms.size(); i++) {
                        var room = rooms.get(i);
                        inv.setItem(ROOM_SLOTS.get(i), Rooms.getIcon(room, inv.getItem(ROOM_SLOTS.get(i))));
                    }
                }
            }.runTaskTimerAsynchronously(SkyBattleClient.get(), 0, 20);
        });
    }

    public static void onClick(InventoryClickEvent e, Player p) {
        if (!(e.getInventory().getHolder() instanceof RoomGUIHolder)) return;
        int slot = e.getSlot();
        var holder = (RoomGUIHolder) e.getInventory().getHolder();

        if (slot == EXIT_BUTTON_SLOT) {
            MainGUI.open(p);
            return;
        }


        if (slot == CREATE_BUTTON_SLOT) {
            CreateGUI.open(p);
            return;
        }

        if (ROOM_SLOTS.contains(slot)) {
            int index = ROOM_SLOTS.indexOf(slot);
            if (Rooms.getRooms().size() <= index) return;
            var room = Rooms.getRooms().get(index);

            // Check in
            if (room.getPlayers().contains(p)) {
                TeamSelectGUI.open(p, room);
            }

            // Add to
            else if (room.canJoin()) {
                room.addToNull(p);
                TeamSelectGUI.open(p, room);
            }
        }
    }

    private static ItemStack getCreateButton() {
        var is = new ItemStack(Material.GOLD_BLOCK);
        var ism = new ItemStackManager(SkyBattleClient.get(), is);
        ism.setName("§c§lTạo phòng mới");
        return is;
    }

    private static ItemStack getExitButton() {
        var is = new ItemStack(Material.BARRIER);
        var ism = new ItemStackManager(SkyBattleClient.get(), is);
        ism.setName("§c§lThoát");
        return is;
    }

}

class RoomGUIHolder implements GUIHolder {

    @Override
    public @NotNull Inventory getInventory() {
        return null;
    }

}
