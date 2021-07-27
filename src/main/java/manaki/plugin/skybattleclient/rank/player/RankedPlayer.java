package manaki.plugin.skybattleclient.rank.player;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import manaki.plugin.skybattleclient.gui.room.BattleType;
import manaki.plugin.skybattleclient.rank.RankData;
import manaki.plugin.skybattleclient.rank.RankGrade;
import manaki.plugin.skybattleclient.rank.RankType;
import manaki.plugin.skybattleclient.util.Utils;

import java.util.Map;
import java.util.Set;

public class RankedPlayer {

    private final String name;
    private Map<BattleType, RankData> rankData;
    private Map<BattleType, Set<String>> takenRewards;

    public RankedPlayer(String name) {
        this.name = name;
        this.rankData = Maps.newConcurrentMap();
        this.takenRewards = Maps.newHashMap();
        for (BattleType bt : BattleType.values()) {
            rankData.put(bt, new RankData(0, RankType.BRONZE, RankGrade.IV));
        };
    }

    public RankedPlayer(String name, Map<BattleType, RankData> rankData) {
        this.name = name;
        this.rankData = rankData;
    }

    public String getName() {
        return name;
    }

    public Map<BattleType, RankData> getRankData() {
        return rankData;
    }

    public RankData getRankData(BattleType type) {
        if (!rankData.containsKey(type)) rankData.put(type, new RankData(0, RankType.BRONZE, RankGrade.IV));
        return rankData.get(type);
    }

    public RankData getHighestRank() {
        // Get max rank
        BattleType bt = null;
        int maxp = -1;
        for (BattleType type : BattleType.values()) {
            var rd = this.getRankData(type);
            var point = Utils.toPoint(rd.getType(), rd.getGrade(), rd.getPoint());
            if (point > maxp) {
                maxp = point;
                bt = type;
            }
        }

        // return
        return this.getRankData(bt);
    }

    public Map<BattleType, Set<String>> getTakenRewards() {
        return takenRewards;
    }

    public boolean isTaken(BattleType bt, String id) {
        return getTakenRewards().containsKey(bt) && getTakenRewards().get(bt).contains(id);
    }

    public void addTakenReward(BattleType bt, String id) {
        if (!takenRewards.containsKey(bt)) takenRewards.put(bt, Sets.newHashSet());
        takenRewards.get(bt).add(id);
    }

    public void save() {
        RankedPlayers.save(this);
    }

}
