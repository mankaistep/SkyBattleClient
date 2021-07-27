package manaki.plugin.skybattleclient.placeholder;

import manaki.plugin.skybattleclient.SkyBattleClient;
import manaki.plugin.skybattleclient.gui.room.BattleType;
import manaki.plugin.skybattleclient.rank.player.RankedPlayers;
import manaki.plugin.skybattleclient.util.Utils;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RankPlaceholder extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "skybattleclient";
    }

    @Override
    public @NotNull String getAuthor() {
        return "MankaiStep";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String s) {
        try {
            if (s.equalsIgnoreCase("rank_point")) {
                // Get max rank
                BattleType bt = null;
                int maxp = -1;
                var rp = RankedPlayers.get(player.getName());
                for (BattleType type : BattleType.values()) {
                    var rd = rp.getRankData(type);
                    var point = Utils.toPoint(rd.getType(), rd.getGrade(), rd.getPoint());
                    if (point > maxp) {
                        maxp = point;
                        bt = type;
                    }
                }

                // return
                var rd = rp.getRankData(bt);
                return Utils.toPoint(rd.getType(), rd.getGrade(), rd.getPoint()) + "";
            }
            else if (s.contains("rank_point_")) {
                var bt = BattleType.valueOf(s.replace("rank_point_", "").toUpperCase());
                var rp = RankedPlayers.get(player.getName());
                var rd = rp.getRankData(bt);
                return Utils.toPoint(rd.getType(), rd.getGrade(), rd.getPoint()) + "";
            }
            else if (s.contains("rank_display")) {
                // Get max rank
                BattleType bt = null;
                int maxp = -1;
                var rp = RankedPlayers.get(player.getName());
                for (BattleType type : BattleType.values()) {
                    var rd = rp.getRankData(type);
                    var point = Utils.toPoint(rd.getType(), rd.getGrade(), rd.getPoint());
                    if (point > maxp) {
                        maxp = point;
                        bt = type;
                    }
                }

                // return
                var rd = rp.getRankData(bt);
                return "§r" + Utils.getRankDisplay(rd);
            }
        }
        catch (Exception e) {
            SkyBattleClient.get().getLogger().severe(e.getMessage());
            return "...";
        }



        return "Wrong!";
    }
}
