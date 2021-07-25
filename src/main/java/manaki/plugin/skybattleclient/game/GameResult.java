package manaki.plugin.skybattleclient.game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;
import java.util.Set;

public class GameResult {

    private final int type;
    private final Map<String, Integer> tops;
    private final Set<String> afks;

    public GameResult(int type, Map<String, Integer> tops, Set<String> afks) {
        this.type = type;
        this.tops = tops;
        this.afks = afks;
    }

    public int getType() {
        return type;
    }

    public Map<String, Integer> getTops() {
        return tops;
    }

    public Set<String> getAfks() {
        return afks;
    }

    public static GameResult parse(String s) {
        return new GsonBuilder().create().fromJson(s, GameResult.class);
    }

}
