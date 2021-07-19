package manaki.plugin.skybattleclient.request.util;

import com.google.gson.GsonBuilder;
import manaki.plugin.skybattleclient.request.QuitRequest;
import manaki.plugin.skybattleclient.request.StartRequest;


public class Requests {

    public static StartRequest parseStart(String s) {
        return new GsonBuilder().create().fromJson(s, StartRequest.class);
    }

    public static QuitRequest parseQuit(String s) {
        return new GsonBuilder().create().fromJson(s, QuitRequest.class);
    }

}
