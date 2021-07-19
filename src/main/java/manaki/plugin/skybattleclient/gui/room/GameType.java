package manaki.plugin.skybattleclient.gui.room;

public enum GameType {

    NORMAL("Thường"),
    RANKED("Hạng");

    private String name;

    GameType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
