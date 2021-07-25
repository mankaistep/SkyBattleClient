package manaki.plugin.skybattleclient.rank.player;

import com.google.common.collect.Maps;
import com.google.gson.GsonBuilder;
import manaki.plugin.skybattleclient.SkyBattleClient;
import manaki.plugin.skybattleclient.event.PlayerEndGameEvent;
import manaki.plugin.skybattleclient.game.Notifications;
import manaki.plugin.skybattleclient.game.notification.DownNotification;
import manaki.plugin.skybattleclient.game.notification.UpNotification;
import manaki.plugin.skybattleclient.game.notification.i.Notificatable;
import manaki.plugin.skybattleclient.rank.RankData;
import manaki.plugin.skybattleclient.util.Utils;
import mk.plugin.playerdata.storage.PlayerDataAPI;
import org.bukkit.Bukkit;
import org.bukkit.Sound;

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

    public static void doNoti(String name) {
        var noti = Notifications.get(name);
        var rp = RankedPlayers.get(name);
        var p = Bukkit.getPlayer(name);

        if (p != null) {
            noti.show(p);
        }
        Notifications.remove(name);

        // Save
        if (noti instanceof UpNotification) {

            var rd = rp.getRankData(((UpNotification) noti).getBattleType());
            int up = ((UpNotification) noti).getPointUp(name);

            var rtbefore = rd.getType();
            var rgbefore = rd.getGrade();

            rd.addPoint(up);

            if (rd.getGrade() != rgbefore || rd.getType() != rtbefore) {
                if (p != null) {
                    Bukkit.getScheduler().runTaskLaterAsynchronously(SkyBattleClient.get(), () -> {
                        p.sendTitle("§e§lThăng hạng!", Utils.getRankDisplay(new RankData(0, rtbefore, rgbefore)) + " >> §r" + Utils.getRankDisplay(rd));
                        p.playSound(p.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1, 1);
                    }, 20);

                    Bukkit.getScheduler().runTask(SkyBattleClient.get(), () -> {
                        // Call event
                        Bukkit.getPluginManager().callEvent(new PlayerEndGameEvent(p, noti.getResult()));
                    });
                }
            }

        }
        else if (noti instanceof DownNotification) {
            var rd = rp.getRankData(((DownNotification) noti).getBattleType());
            int down = ((DownNotification) noti).getPointDown(name);

            var rtbefore = rd.getType();
            var rgbefore = rd.getGrade();

            rd.subtractPoint(down);

            if (rd.getGrade() != rgbefore || rd.getType() != rtbefore) {
                if (p != null) {
                    Bukkit.getScheduler().runTaskLaterAsynchronously(SkyBattleClient.get(), () -> {
                        p.sendTitle("§c§lGiáng hạng", Utils.getRankDisplay(new RankData(0, rtbefore, rgbefore)) + " >> §r" + Utils.getRankDisplay(rd));
                        p.playSound(p.getLocation(), Sound.ENTITY_GHAST_SCREAM, 1, 1);
                    }, 20);
                }
            }
        }

        rp.save();

        if (p != null) {
            Bukkit.getScheduler().runTask(SkyBattleClient.get(), () -> {
                // Call event
                Bukkit.getPluginManager().callEvent(new PlayerEndGameEvent(p, noti.getResult()));
            });
        }
    }

}
