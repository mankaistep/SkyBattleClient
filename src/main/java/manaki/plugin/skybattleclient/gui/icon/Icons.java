package manaki.plugin.skybattleclient.gui.icon;

import com.google.common.collect.Maps;
import manaki.plugin.skybattleclient.gui.model.BattleIconModel;
import manaki.plugin.skybattleclient.gui.model.IconModel;
import manaki.plugin.skybattleclient.util.ItemBuilder;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.Map;

public class Icons {

    public static Map<String, BattleIconModel> BATTLE_ICONS = Maps.newHashMap();
    public static IconModel BACKGROUND_ICON;
    public static IconModel TUTORIAL_ICON;
    public static IconModel INFO_ICON;
    public static IconModel QUIT_ICON;

    public static void reload(Plugin plugin) {
        var config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "config.yml"));

        // Battle
        BATTLE_ICONS.clear();
        for (String bId : config.getConfigurationSection("battle").getKeys(false)){
            int slot = config.getInt("battle." + bId + ".slot");
            var icon = ItemBuilder.buildItem(config.getConfigurationSection("battle." + bId + ".item"));
            BATTLE_ICONS.put(bId, new BattleIconModel(bId, slot, icon));
        }

        BACKGROUND_ICON = new IconModel(config.getIntegerList("gui.background.slots"),
                ItemBuilder.buildItem(config.getConfigurationSection("gui.background.item")));

        TUTORIAL_ICON = new IconModel(config.getInt("gui.tutorial.slot"),
                ItemBuilder.buildItem(config.getConfigurationSection("gui.tutorial.item")));

        INFO_ICON = new IconModel(config.getInt("gui.info.slot"),
                ItemBuilder.buildItem(config.getConfigurationSection("gui.info.item")));

        QUIT_ICON = new IconModel(config.getInt("gui.quit.slot"),
                ItemBuilder.buildItem(config.getConfigurationSection("gui.quit.item")));

    }

}
