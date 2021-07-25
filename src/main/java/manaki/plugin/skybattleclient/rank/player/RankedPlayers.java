package manaki.plugin.skybattleclient.rank.player;

import com.google.common.collect.Maps;
import com.google.gson.GsonBuilder;
import mk.plugin.playerdata.storage.PlayerDataAPI;

import java.util.Map;

public class RankedPlayers {

    public static final String HOOK = "skybattle-ranked";
    public static final String KEY = "ranked-player";

    private static Map<String, RankedPlayer> data = Maps.newConcurrentMap();

    public static RankedPlayer get(String name) {
        // Already loaded
        if (data.containsKey(name)) {
            return data.get(name);
        }

        // Load from data
        RankedPlayer rp;
        var pd = PlayerDataAPI.get(name, HOOK);
        if (pd.hasData(KEY)) {
            rp = new GsonBuilder().create().fromJson(pd.getValue(KEY), RankedPlayer.class);
        }
        else {
            rp = new RankedPlayer(name);
        }

        data.put(name, rp);

        return rp;
    }

    public static void save(RankedPlayer rp) {
        var pd = PlayerDataAPI.get(rp.getName(), HOOK);
        pd.set(KEY, new GsonBuilder().create().toJson(rp));
        pd.save();
    }

    public static void removeCache(String name) {
        data.remove(name);
    }

    public static int calPointUp(int top, int base, boolean isWinner) {
        int r = base;
        for (int i = 0 ; i < (4 - top) ; i++) r *= 1.2;
        if (isWinner) r *= 1.2;
        return r;
    }

    public static int calPointDown(int top, int base) {
        int r = base;
        for (int i = 0 ; i < (top - 5) ; i++) r *= 1.2;
        return r;
    }

}
