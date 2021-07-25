package manaki.plugin.skybattleclient.util;

import manaki.plugin.skybattleclient.SkyBattleClient;
import manaki.plugin.skybattleclient.gui.icon.Icons;
import manaki.plugin.skybattleclient.rank.RankData;
import manaki.plugin.skybattleclient.rank.RankGrade;
import manaki.plugin.skybattleclient.rank.RankType;
import mk.plugin.santory.utils.ItemStackManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class Utils {

    public static int toPoint(RankType t, RankGrade g, int point) {
        return (t.getValue() - 1) * 400 + (4 - g.getValue()) * 100 + point;
    }

    public static double random(double min, double max) {
        return (new Random().nextInt(new Double((max - min) * 1000).intValue()) + min * 1000) / 1000;
    }

    public static int randomInt(int min, int max) {
        return new Random().nextInt(max - min + 1) + min;
    }


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
