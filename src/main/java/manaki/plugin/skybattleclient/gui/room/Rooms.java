package manaki.plugin.skybattleclient.gui.room;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import manaki.plugin.skybattleclient.SkyBattleClient;
import manaki.plugin.skybattleclient.gui.room.team.TeamIcon;
import mk.plugin.santory.utils.ItemStackManager;
import mk.plugin.santory.utils.Tasks;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Rooms {

    public static final long START_COUNT = 10000;
    public static final long REMOVE_COUNT = 5000;

    private static Map<String, List<Room>> rooms = Maps.newConcurrentMap();

    private static int lastId = 0;

    public static Room createRoom(String battleId, GameType gameType, BattleType battleType, Player creator) {
        lastId++;
        var room = new Room(lastId, battleId, battleType, gameType, creator);
        getRooms(battleId).add(room);

        // Status check
        long start = System.currentTimeMillis();
        new BukkitRunnable() {
            long lastSeconds = 0;
            @Override
            public void run() {
                // Check delete
                if (room.isDeleting()) {
                    if (System.currentTimeMillis() - room.getDeleteCount() >= REMOVE_COUNT) {
                        // Remove room
                        removeRoom(room);
                        this.cancel();
                    }
                    return;
                }

                // To delete
                if (room.getPlayers().size() == 0) {
                    room.setDelete(true);
                    return;
                }

                // Check start (counting)
                if (room.canStarted()) {
                    if (!room.isCountdowning()) room.setStartCountdown(true);
                }
                else room.setStartCountdown(false);

                // Check count
                if (room.isCountdowning()) {
                    // Sound effect
                    long second = (System.currentTimeMillis() - start) / 1000L;
                    if (second != lastSeconds) {
                        lastSeconds = second;
                        for (Player p : room.getPlayers()) {
                            p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                        }
                    }

                    // Check start
                    if (System.currentTimeMillis() - room.getStartCount() >= START_COUNT) {
                        for (Player p : room.getPlayers()) {
                            p.closeInventory();
                            p.sendTitle("", "§aĐang bắt đầu...", 0, 100, 0);
                        }

                        // Start
                        SkyBattleClient.get().getExecutor().sendStart(room);

                        // Remove room
                        room.setDelete(true);
                        room.setStarted(true);
                        removeRoom(room);
                        this.cancel();
                        return;
                    }
                    return;
                }

            }
        }.runTaskTimer(SkyBattleClient.get(), 0, 5);

        return room;
    }

    public static List<Room> getRooms(String battleId) {
        if (!rooms.containsKey(battleId)) rooms.put(battleId, Lists.newArrayList());
        return rooms.get(battleId);
    }

    public static void removeRoom(Room room) {
        for (Map.Entry<String, List<Room>> e : rooms.entrySet()) {
            e.getValue().remove(room);
        }
    }

    public static void removePlayer(Player player) {
        for (Map.Entry<String, List<Room>> e : rooms.entrySet()) {
            for (Room room : e.getValue()) {
                for (Map.Entry<TeamIcon, Set<Player>> e2 : room.getTeamPlayers().entrySet()) {
                    e2.getValue().remove(player);
                }
            }
        }
    }

    public static ItemStack getIcon(Room room) {
        var is = new ItemStack(Material.WHITE_BED);
        var ism = new ItemStackManager(SkyBattleClient.get(), is);
        List<String> lore = Lists.newArrayList();

        if (room.canJoin()) {
            ism.setName("§a§lPhòng #" + room.getId());
            is.setType(Material.LIME_BED);
            lore.add("§2§oCó thể vào");
        }
        else if (room.isDeleting()) {
            ism.setName("§c§lPhòng #" + room.getId());
            is.setType(Material.RED_BED);
            lore.add("§c§oXóa phòng sau §c§l§o" + (REMOVE_COUNT - (System.currentTimeMillis() - room.getDeleteCount())) / 1000 + "s");
        }
        else if (room.isCountdowning()) {
            ism.setName("§3§lPhòng #" + room.getId());
            is.setType(Material.LIGHT_BLUE_BED);
            lore.add("§b§oĐếm ngược bắt đầu §b§l§o" + (START_COUNT - (System.currentTimeMillis() - room.getStartCount())) / 1000 + "s");
        }

        is.setAmount(Math.max(1, room.getPlayers().size()));
        lore.add("");
        lore.add("§eHình thức: §f" + room.getBattleType().getName());
        lore.add("§eLoại: §f" + room.getGameType().getName());
        lore.add("§eNgười chơi (" + room.getPlayers().size() + "/" + room.getMaxPlayers() + ")");
        for (Player p : room.getPlayers()) lore.add("§a ● " + p.getName());

        ism.setLore(lore);

        return is;
    }

}
