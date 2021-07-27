package manaki.plugin.skybattleclient.rank.reward;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import manaki.plugin.skybattleclient.SkyBattleClient;
import manaki.plugin.skybattleclient.gui.holder.GUIHolder;
import manaki.plugin.skybattleclient.gui.room.BattleType;
import manaki.plugin.skybattleclient.rank.RankGrade;
import manaki.plugin.skybattleclient.rank.RankType;
import manaki.plugin.skybattleclient.rank.player.RankedPlayers;
import manaki.plugin.skybattleclient.util.Utils;
import manaki.plugin.skybattleclient.util.command.Command;
import mk.plugin.santory.gui.GUI;
import mk.plugin.santory.utils.Tasks;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RankRewards {

    private static int rewardGUISize;
    private static Map<String, RankReward> v1Rewards;
    private static Map<String, RankReward> v2Rewards;
    private static Map<String, RankReward> v3Rewards;

    public static void reload(Plugin plugin) {
        var config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "config.yml"));

        rewardGUISize = config.getInt("reward-gui-size");

        v1Rewards = Maps.newHashMap();
        for (String rid : config.getConfigurationSection("reward.v1").getKeys(false)) {
            var slot = config.getInt("reward.v1." + rid + ".slot");
            var type = RankType.valueOf(config.getString("reward.v1." + rid + ".rank-type"));
            var grade = RankGrade.valueOf(config.getString("reward.v1." + rid + ".rank-grade"));
            List<Command> commands = config.getStringList("reward.v1." + rid + ".commands").stream().map(Command::new).collect(Collectors.toList());
            List<String> desc = config.getStringList("reward.v1." + rid + ".desc").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList());
            v1Rewards.put(rid, new RankReward(rid, slot, type, grade, commands, desc));
        }

        v2Rewards = Maps.newHashMap();
        for (String rid : config.getConfigurationSection("reward.v2").getKeys(false)) {
            var slot = config.getInt("reward.v2." + rid + ".slot");
            var type = RankType.valueOf(config.getString("reward.v2." + rid + ".rank-type"));
            var grade = RankGrade.valueOf(config.getString("reward.v2." + rid + ".rank-grade"));
            List<Command> commands = config.getStringList("reward.v2." + rid + ".commands").stream().map(Command::new).collect(Collectors.toList());
            List<String> desc = config.getStringList("reward.v2." + rid + ".desc").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList());
            v2Rewards.put(rid, new RankReward(rid, slot, type, grade, commands, desc));
        }

        v3Rewards = Maps.newHashMap();
        for (String rid : config.getConfigurationSection("reward.v3").getKeys(false)) {
            var slot = config.getInt("reward.v3." + rid + ".slot");
            var type = RankType.valueOf(config.getString("reward.v3." + rid + ".rank-type"));
            var grade = RankGrade.valueOf(config.getString("reward.v3." + rid + ".rank-grade"));
            List<Command> commands = config.getStringList("reward.v3." + rid + ".commands").stream().map(Command::new).collect(Collectors.toList());
            List<String> desc = config.getStringList("reward.v3." + rid + ".desc").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList());
            v3Rewards.put(rid, new RankReward(rid, slot, type, grade, commands, desc));
        }
    }

    // Slots
    private static Map<Integer, BattleType> slots = Map.of(2, BattleType.V1, 4, BattleType.V2, 6, BattleType.V3);

    public static void openSelectGUI(Player p) {
        var inv = Bukkit.createInventory(new GUIHolderImpl(), 9, "§0§lCHỌN CHẾ ĐỘ");
        p.openInventory(inv);

        Bukkit.getScheduler().runTaskAsynchronously(SkyBattleClient.get(), () -> {
            for (int i = 0 ; i < inv.getSize() ; i++) inv.setItem(i, Utils.getBackIcon());
            slots.forEach((slot, d) -> {
                inv.setItem(slot, d.buildIcon());
            });
        });
    }

    public static void openGUI(Player p, BattleType bt) {
        var rp = RankedPlayers.get(p.getName());
        var rd = rp.getRankData(bt);
        var line = rd.getType().getName() + " " + rd.getGrade() + " " + rd.getPoint() + " điểm";

        int size = rewardGUISize;
        var inv = Bukkit.createInventory(new RankGUIHolder(bt), size, "§0§lQUÀ XẾP HẠNG (" + line + ")");
        p.openInventory(inv);

        Bukkit.getScheduler().runTaskAsynchronously(SkyBattleClient.get(), () -> {
            Map<String, RankReward> rewards;
            switch (bt) {
                case V1: rewards = v1Rewards; break;
                case V2: rewards = v2Rewards; break;
                case V3: rewards = v3Rewards; break;
                default:
                    throw new IllegalStateException("Unexpected value: " + bt);
            }
            for (Map.Entry<String, RankReward> e : rewards.entrySet()) {
                var id = e.getKey();
                var r = e.getValue();
                inv.setItem(r.getSlot(), getIcon(p, bt, r));
            }
        });
    }

    public static void onClickSelect(InventoryClickEvent e, Player p) {
        if (!(e.getInventory().getHolder() instanceof GUIHolderImpl)) return;

        int slot = e.getSlot();
        if (slots.containsKey(slot)) {
            var bt = slots.get(slot);
            openGUI(p, bt);
        }
    }


    public static void onClick(InventoryClickEvent e, Player p) {
        if (!(e.getInventory().getHolder() instanceof RankGUIHolder)) return;

        int slot = e.getSlot();
        var holder = (RankGUIHolder) e.getInventory().getHolder();
        var bt = holder.getBattleType();

        // Get
        Map<String, RankReward> rewards;
        switch (bt) {
            case V1: rewards = v1Rewards; break;
            case V2: rewards = v2Rewards; break;
            case V3: rewards = v3Rewards; break;
            default:
                throw new IllegalStateException("Unexpected value: " + bt);
        }

        // Check
        for (Map.Entry<String, RankReward> entry : rewards.entrySet()) {
            var id = entry.getKey();
            var reward = entry.getValue();
            if (reward.getSlot() == slot) {
                var rp = RankedPlayers.get(p.getName());
                var rd = rp.getRankData(bt);

                int pointRequired = Utils.toPoint(reward.getRankType(), reward.getRankGrade(), 0);
                int point = Utils.toPoint(rd.getType(), rd.getGrade(), rd.getPoint());

                if (!rp.isTaken(bt, reward.getId()) && point >= pointRequired) {
                    // Take the reward
                    Tasks.sync(() -> {
                        // Set taken
                        rp.addTakenReward(bt, reward.getId());
                        rp.save();

                        // Execute cmds
                        for (Command cmd : reward.getRewards()) {
                            cmd.execute(SkyBattleClient.get(), p, Map.of("%player%", p.getName()));
                        }

                        // Close inv
                        p.closeInventory();
                    });
                }
                else {
                    p.sendMessage("§c§lKhông đạt yêu cầu hoặc đã nhận rồi");
                }
            }
        }
    }

    public static ItemStack getIcon(Player p, BattleType bt, RankReward reward) {
        var is = new ItemStack(Material.GLASS_PANE);
        var meta = is.getItemMeta();

        meta.setCustomModelData(reward.getRankType().getModelData());
        var rt = reward.getRankType();
        var rg = reward.getRankGrade();
        meta.setDisplayName("§r" + rt.getIcon() + rt.getColor() + "§l " + rt.getName() + " " + rg.name());
        var lore = Lists.newArrayList(reward.getDesc());
        lore.add("");

        var rp = RankedPlayers.get(p.getName());
        var rd = rp.getRankData(bt);

        int pointRequired = Utils.toPoint(reward.getRankType(), reward.getRankGrade(), 0);
        int point = Utils.toPoint(rd.getType(), rd.getGrade(), rd.getPoint());

        if (rp.isTaken(bt, reward.getId())) {
            lore.add("§6§oĐã nhận");
        }
        else if (point >= pointRequired) {
            lore.add("§a§oCó thể nhận");
            meta.addEnchant(Enchantment.DURABILITY, 1, false);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        else {
            lore.add("§c§oTăng bậc xếp hạng để nhận");
        }

        meta.setLore(lore);
        is.setItemMeta(meta);

        return is;
    }

}

class RankGUIHolder implements GUIHolder {

    private final BattleType bt;

    public RankGUIHolder(BattleType bt) {
        this.bt = bt;
    }

    public BattleType getBattleType() {
        return bt;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return null;
    }
}

class GUIHolderImpl implements GUIHolder {

    @Override
    public @NotNull Inventory getInventory() {
        return null;
    }
}