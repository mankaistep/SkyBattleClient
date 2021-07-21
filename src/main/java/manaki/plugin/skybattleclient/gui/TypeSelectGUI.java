package manaki.plugin.skybattleclient.gui;

import manaki.plugin.skybattleclient.SkyBattleClient;
import manaki.plugin.skybattleclient.gui.holder.GUIHolder;
import manaki.plugin.skybattleclient.gui.icon.Icons;
import manaki.plugin.skybattleclient.gui.room.BattleType;
import manaki.plugin.skybattleclient.gui.room.GameType;
import manaki.plugin.skybattleclient.gui.room.Rooms;
import manaki.plugin.skybattleclient.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class TypeSelectGUI {

    private static final Map<BattleType, Integer> SLOTS = Map.of(BattleType.V1, 2, BattleType.V2, 4, BattleType.V3, 6);

    public static void open(Player player, String battleId, GameType gameType) {
        var inv = Bukkit.createInventory(new TypeGUIHolder(battleId, gameType), 9, "§0§lCHỌN HÌNH THỨC");
        player.openInventory(inv);

        Bukkit.getScheduler().runTaskAsynchronously(SkyBattleClient.get(), () -> {
            for (int i = 0 ; i < inv.getSize() ; i++) inv.setItem(i, Utils.getBackIcon());
            SLOTS.forEach((d, slot) -> {
                inv.setItem(slot, d.buildIcon());
            });
        });
    }

    public static void onClick(InventoryClickEvent e, Player p) {
        if (!(e.getInventory().getHolder() instanceof TypeGUIHolder)) return;

        int slot = e.getSlot();
        if (!SLOTS.containsValue(slot)) return;

        var holder = (TypeGUIHolder) e.getInventory().getHolder();
        var bt = fromSlot(slot);

        var room = Rooms.createRoom(holder.getBattleId(), holder.getGameType(), bt, p);
        TeamSelectGUI.open(p, room);

        // Broadcast
        var bname = Icons.BATTLE_ICONS.get(room.getBattleId()).getName();
        Utils.broadcast("§2[§a§l/skybattle§2] §aPhòng #" + room.getId() + " §fvới chiến trường §c" + bname + " §fđược tạo bởi §a" + p.getName());
    }

    private static BattleType fromSlot(int slot) {
        for (Map.Entry<BattleType, Integer> e : SLOTS.entrySet()) {
            if (e.getValue() == slot) return e.getKey();
        }
        return null;
    }

}

class TypeGUIHolder implements GUIHolder {

    private String battleId;
    private GameType gameType;

    public TypeGUIHolder(String battleId, GameType gameType) {
        this.battleId = battleId;
        this.gameType = gameType;
    }

    public String getBattleId() {
        return battleId;
    }

    public GameType getGameType() {
        return gameType;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return null;
    }
}
