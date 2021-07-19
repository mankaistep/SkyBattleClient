package manaki.plugin.skybattleclient.gui.model;

import com.google.common.collect.Lists;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class IconModel {

    private final List<Integer> slots;
    private final ItemStack icon;

    public IconModel(int slot, ItemStack icon) {
        this.slots = Lists.newArrayList(slot);
        this.icon = icon;
    }

    public IconModel(List<Integer> slots, ItemStack icon) {
        this.slots = slots;
        this.icon = icon;
    }

    public int getSlot() {
        return this.slots.get(0);
    }

    public List<Integer> getSlots() {
        return slots;
    }

    public ItemStack getIcon() {
        return icon;
    }
}
