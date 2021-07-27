package manaki.plugin.skybattleclient.gui;

import com.google.common.collect.Lists;
import manaki.plugin.skybattleclient.SkyBattleClient;
import manaki.plugin.skybattleclient.gui.holder.GUIHolder;
import manaki.plugin.skybattleclient.gui.icon.Icons;
import manaki.plugin.skybattleclient.gui.room.BattleType;
import manaki.plugin.skybattleclient.gui.room.Rooms;
import manaki.plugin.skybattleclient.rank.player.RankedPlayers;
import manaki.plugin.skybattleclient.rank.reward.RankRewards;
import manaki.plugin.skybattleclient.request.JoinRequest;
import mk.plugin.santory.utils.ItemStackManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MainGUI {

    private static final int HELP_BUTTON = 2;
    private static final int RULE_BUTTON = 4;
    private static final int SPEC_BUTTON = 6;
    private static final int CREATE_BUTTON = 10;
    private static final int JOIN_BUTTON = 12;
    private static final int REWARD_BUTTON = 14;
    private static final int QUIT_BUTTON = 16;

    public static void open(Player player) {
        // If have room
        if (Rooms.hasRoom(player)) {
            var room = Rooms.getRoom(player);
            TeamSelectGUI.open(player, room);
            return;
        }

        // Open
        var rd = RankedPlayers.get(player.getName()).getHighestRank();
        var line = rd.getType().getName() + " " + rd.getGrade() + " " + rd.getPoint() + " điểm";
        Inventory inv = Bukkit.createInventory(new MainGUIHolder(), 18, "§0§lSKYBATTLE - " + line);
        player.openInventory(inv);
        player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1);

        Bukkit.getScheduler().runTaskAsynchronously(SkyBattleClient.get(), () -> {
            inv.setItem(HELP_BUTTON, getHelpButton());
            inv.setItem(RULE_BUTTON, getRuleButton(player));
            inv.setItem(SPEC_BUTTON, getSpecButton());
            inv.setItem(CREATE_BUTTON, getCreateButton());
            inv.setItem(JOIN_BUTTON, getJoinButton());
            inv.setItem(REWARD_BUTTON, getRewardButton());
            inv.setItem(QUIT_BUTTON, getQuitButton());
        });
    }


    public static void onClick(InventoryClickEvent e, Player p) {
        if (!(e.getInventory().getHolder() instanceof MainGUIHolder)) return;
        int slot = e.getSlot();

        // Quit button
        if (slot == QUIT_BUTTON) {
            p.closeInventory();
            return;
        }

        // Spectator button
        if (slot == SPEC_BUTTON) {
            SkyBattleClient.get().getExecutor().sendJoin(new JoinRequest(p.getName(), ""));
            p.closeInventory();
            return;
        }

        // Create button
        if (slot == CREATE_BUTTON) {
            CreateGUI.open(p);
            return;
        }

        // Join
        if (slot == JOIN_BUTTON) {
            RoomSelectGUI.open(p);
            return;
        }

        // Reward
        if (slot == REWARD_BUTTON) {
            RankRewards.openSelectGUI(p);
            return;
        }

    }

    public static ItemStack getHelpButton() {
        var is = new ItemStack(Material.GLASS_PANE);
        var ism = new ItemStackManager(SkyBattleClient.get(), is);
        ism.setModelData(25);
        ism.setName("§a§lSkybattle là gì?");
        List<String> lore = Lists.newArrayList();
        lore.add("§f§oLà một chế độ minigame cực đỉnh, kết hợp");
        lore.add("§f§ogiữa sorasky, skywar và battle royale");
        ism.setLore(lore);

        return is;
    }

    public static ItemStack getRuleButton(Player p) {
        var is = new ItemStack(Material.GLASS_PANE);
        var ism = new ItemStackManager(SkyBattleClient.get(), is);
        ism.setModelData(8);
        ism.setName("§a§lLuật chơi");
        List<String> lore = Lists.newArrayList();
        lore.add("§61. §fĐể thắng, bạn phải hạ trùm cuối và toàn bộ địch");
        lore.add("§62. §fĐể mở tiệm rèn, bạn phải tìm §eCái đe §frồi bấm vào");
        lore.add("");
        lore.add("§a§lXếp hạng");

        var rp = RankedPlayers.get(p.getName());
        for (BattleType bt : BattleType.values()) {
            var rd1 = rp.getRankData(bt);
            lore.add("§f" + rd1.getType().getIcon() + rd1.getType().getColor() + " " + rd1.getType().getName() + " " + rd1.getGrade() + " " + rd1.getPoint() + " điểm §f(" + bt.getName() + ")");
        }

        ism.setLore(lore);

        return is;
    }

    public static ItemStack getSpecButton() {
        var is = new ItemStack(Material.GLASS_PANE);
        var ism = new ItemStackManager(SkyBattleClient.get(), is);
        ism.setModelData(45);
        ism.setName("§e§lCLICK §a§lChế độ khán giả");
        List<String> lore = Lists.newArrayList();
        lore.add("§f§oQua sảnh để quan sát người chơi khác");
        lore.add("§f§obằng §a§o/khangia §f§oovà thoát bằng §c§o/thoat");
        ism.setLore(lore);

        return is;
    }

    public static ItemStack getCreateButton() {
        var is = new ItemStack(Material.GOLD_BLOCK);
        var ism = new ItemStackManager(SkyBattleClient.get(), is);
        ism.setName("§e§lCLICK §a§lTạo phòng mới");
        return is;
    }

    public static ItemStack getJoinButton() {
        var is = new ItemStack(Material.IRON_BLOCK);
        var ism = new ItemStackManager(SkyBattleClient.get(), is);
        ism.setName("§e§lCLICK §a§lTham gia phòng");
        return is;
    }

    public static ItemStack getRewardButton() {
        var is = new ItemStack(Material.CHEST);
        var ism = new ItemStackManager(SkyBattleClient.get(), is);
        ism.setName("§e§lCLICK §a§lPhần thưởng xếp hạng");
        return is;
    }

    public static ItemStack getQuitButton() {
        var is = new ItemStack(Material.BARRIER);
        var ism = new ItemStackManager(SkyBattleClient.get(), is);
        ism.setName("§e§lCLICK §c§lThoát");
        return is;
    }


}

class MainGUIHolder implements GUIHolder {

    @Override
    public @NotNull Inventory getInventory() {
        return null;
    }

}
