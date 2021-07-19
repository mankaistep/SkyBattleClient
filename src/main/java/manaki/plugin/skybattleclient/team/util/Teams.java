package manaki.plugin.skybattleclient.team.util;

import manaki.plugin.skybattleclient.team.player.TeamPlayer;
import mk.plugin.santory.item.Items;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Teams {

    public static TeamPlayer read(Player player) {
        var name = player.getName();
        var items = new ArrayList<String> ();

        addNotNull(items, player.getInventory().getItemInMainHand());
        addNotNull(items, player.getInventory().getItemInOffHand());
        addNotNull(items, player.getInventory().getHelmet());
        addNotNull(items, player.getInventory().getChestplate());

        return new TeamPlayer(name, items);
    }

    private static void addNotNull(List<String> items, ItemStack is) {
        var item = Items.read(is);
        if (item != null) items.add(item.toString());
    }

}
