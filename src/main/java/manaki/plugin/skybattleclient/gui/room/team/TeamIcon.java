package manaki.plugin.skybattleclient.gui.room.team;

import org.bukkit.DyeColor;
import org.bukkit.Material;

public enum TeamIcon {

    I(DyeColor.CYAN, "§3"),
    II(DyeColor.RED, "§c"),
    III(DyeColor.PURPLE, "§d"),
    IV(DyeColor.LIGHT_BLUE, "§b"),
    V(DyeColor.BLUE, "§9"),
    VI(DyeColor.GREEN, "§2"),
    VII(DyeColor.LIME, "§a"),
    VIII(DyeColor.YELLOW, "§e"),
    NULL(null, "§8");

    private final DyeColor color;
    private final String colorS;

    TeamIcon(DyeColor color, String colorS) {
        this.color = color;
        this.colorS = colorS;
    }

    public String getColor() {
        return colorS;
    }

    public Material getIcon() {
        return Material.valueOf(this.color.name() + "_WOOL");
    }

}
