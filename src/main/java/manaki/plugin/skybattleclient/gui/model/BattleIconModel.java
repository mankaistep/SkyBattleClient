package manaki.plugin.skybattleclient.gui.model;

import org.bukkit.inventory.ItemStack;

public class BattleIconModel extends IconModel {

    private final String battleId;

    public BattleIconModel(String battleId, int slot, ItemStack icon) {
        super(slot, icon);
        this.battleId = battleId;
    }

    public String getBattleId() {
        return battleId;
    }
}
