package manaki.plugin.skybattleclient.util;

import manaki.plugin.skybattleclient.SkyBattleClient;
import manaki.plugin.skybattleclient.gui.icon.Icons;
import mk.plugin.santory.utils.ItemStackManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Utils {

    public static void broadcast(String s) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(s);
        }
    }

    public static ItemStack getBackIcon() {
        var is = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        var ism = new ItemStackManager(SkyBattleClient.get(), is);
        ism.setName("§f");
        return is;
    }

}
