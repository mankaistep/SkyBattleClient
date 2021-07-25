package manaki.plugin.skybattleclient.gui.room;


import manaki.plugin.skybattleclient.SkyBattleClient;
import mk.plugin.santory.utils.ItemStackManager;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum BattleType {

    V1(1, "1v1", Material.LIGHT_BLUE_BANNER),
    V2(2, "2v2", Material.RED_BANNER),
    V3(3, "3v3", Material.YELLOW_BANNER);

    private int teamSize;
    private String name;
    private Material m;

    BattleType(int teamSize, String name, Material m) {
        this.teamSize = teamSize;
        this.name = name;
        this.m = m;
    }

    public int getTeamSize() {
        return teamSize;
    }

    public String getName() {
        return name;
    }

    public ItemStack buildIcon() {
        var is = new ItemStack(m);
        var ism = new ItemStackManager(SkyBattleClient.get(), is);
        ism.setName("§a§l" + this.getName());
        return is;
    }

    public static BattleType parse(int value) {
        for (BattleType bt : values()) {
            if (bt.getTeamSize() == value) return bt;
        }
        return null;
    }
}
