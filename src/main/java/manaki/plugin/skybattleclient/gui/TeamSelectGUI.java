package manaki.plugin.skybattleclient.gui;

import com.google.common.collect.Lists;
import manaki.plugin.skybattleclient.SkyBattleClient;
import manaki.plugin.skybattleclient.gui.holder.GUIHolder;
import manaki.plugin.skybattleclient.gui.room.Room;
import manaki.plugin.skybattleclient.gui.room.Rooms;
import manaki.plugin.skybattleclient.gui.room.team.TeamIcon;
import manaki.plugin.skybattleclient.util.Utils;
import mk.plugin.santory.utils.ItemStackManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class TeamSelectGUI {

    private static final int STATUS_SLOT = 4;
    private static final int QUIT_SLOT = 6;
    private static final Map<TeamIcon, Integer> SLOTS = Map.of(
            TeamIcon.I, 19,
            TeamIcon.II, 21,
            TeamIcon.III, 23,
            TeamIcon.IV, 25,
            TeamIcon.V, 28,
            TeamIcon.VI, 30,
            TeamIcon.VII, 32,
            TeamIcon.VIII, 34);

    public static void open(Player player, Room room) {
        Inventory inv = Bukkit.createInventory(new TeamGUIHolder(room), 54, "§0§lCHỌN ĐỘI");
        player.openInventory(inv);
        player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1);

        Bukkit.getScheduler().runTaskAsynchronously(SkyBattleClient.get(), () -> {
            for (int i = 0 ; i < inv.getSize() ; i++) {
                if (i != 26 && i != 27 && i > 18 && i < 35) continue;
                inv.setItem(i, Utils.getBackIcon());
            }
            inv.setItem(QUIT_SLOT, getExitButton());
            // Slots
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (player.getOpenInventory().getTopInventory() != inv) {
                        this.cancel();
                        return;
                    }
                    inv.setItem(STATUS_SLOT, Rooms.getIcon(room));
                    for (Map.Entry<TeamIcon, Integer> e : SLOTS.entrySet()) {
                        var ti = e.getKey();
                        int slot = e.getValue();
                        inv.setItem(slot, getTeamIcon(player, room, ti));

                    }
                }
            }.runTaskTimerAsynchronously(SkyBattleClient.get(), 0, 5);
        });
    }

    public static void onClick(InventoryClickEvent e, Player p) {
        if (!(e.getInventory().getHolder() instanceof TeamGUIHolder)) return;
        int slot = e.getSlot();
        var holder = (TeamGUIHolder) e.getInventory().getHolder();

        // Quit
        if (slot == QUIT_SLOT) {
            var bid = holder.getRoom().getBattleId();
            Rooms.removePlayer(p);
            RoomSelectGUI.open(p, bid);
            return;
        }

        if (!SLOTS.containsValue(slot)) return;

        var ti = fromSlot(slot);
        var room = holder.getRoom();

        // Check size
        if (room.getPlayers(ti).size() >= room.getBattleType().getTeamSize()) {
            p.sendMessage("§cĐội đã đầy!");
            return;
        }

        // Change team
        room.changeTeam(p, ti);
    }

    private static ItemStack getExitButton() {
        var is = new ItemStack(Material.BARRIER);
        var ism = new ItemStackManager(SkyBattleClient.get(), is);
        ism.setName("§c§lThoát");
        List<String> lore = Lists.newArrayList();
        lore.add("§fThoát phòng");
        ism.setLore(lore);
        return is;
    }


    private static TeamIcon fromSlot(int slot) {
        for (Map.Entry<TeamIcon, Integer> e : SLOTS.entrySet()) {
            if (e.getValue() == slot) return e.getKey();
        }
        return null;
    }

    public static ItemStack getTeamIcon(Player player, Room room, TeamIcon ti) {
        var m = ti.getIcon();
        var is = new ItemStack(m);
        var ism = new ItemStackManager(SkyBattleClient.get(), is);
        ism.setName(ti.getColor() + "§lĐội " + ti.name());
        List<String> lore = Lists.newArrayList();
        lore.add(ti.getColor() + "Người chơi (" + room.getPlayers(ti).size() + "/" + room.getBattleType().getTeamSize() + ")");
        for (Player p : room.getPlayers(ti)) {
            lore.add(" §f- " + p.getName());
        }
        lore.add("");
        lore.add(ti.getColor() + "Click để tham gia hoặc thoát phòng");
        ism.setLore(lore);

        if (room.getPlayers(ti).contains(player)) {
            var meta = is.getItemMeta();
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            is.setItemMeta(meta);
        }

        return is;
    }

}

class TeamGUIHolder implements GUIHolder {

    private final Room room;

    public TeamGUIHolder(Room room) {
        this.room = room;
    }

    public Room getRoom() {
        return room;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return null;
    }
}
