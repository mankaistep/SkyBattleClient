package manaki.plugin.skybattleclient.rank.reward;

import manaki.plugin.skybattleclient.rank.RankGrade;
import manaki.plugin.skybattleclient.rank.RankType;
import manaki.plugin.skybattleclient.util.command.Command;

import java.util.List;

public class RankReward {

    private final String id;
    private final int slot;
    private final RankType rankType;
    private final RankGrade rankGrade;
    private final List<Command> rewards;
    private final List<String> desc;

    public RankReward(String id, int slot, RankType rankType, RankGrade rankGrade, List<Command> rewards, List<String> desc) {
        this.id = id;
        this.slot = slot;
        this.rankType = rankType;
        this.rankGrade = rankGrade;
        this.rewards = rewards;
        this.desc = desc;
    }

    public String getId() {
        return id;
    }

    public int getSlot() {
        return slot;
    }

    public RankType getRankType() {
        return rankType;
    }

    public RankGrade getRankGrade() {
        return rankGrade;
    }

    public List<Command> getRewards() {
        return rewards;
    }

    public List<String> getDesc() {
        return desc;
    }
}
