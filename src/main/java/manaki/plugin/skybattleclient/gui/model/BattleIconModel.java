package manaki.plugin.skybattleclient.gui.model;

import org.bukkit.inventory.ItemStack;

public class BattleIconModel extends IconModel {

    private final String battleId;
    private final String name;

    public BattleIconModel(String battleId, String name, int slot, ItemStack icon) {
        super(slot, icon);
        this.battleId = battleId;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getBattleId() {
        return battleId;
    }
}
