package manaki.plugin.skybattleclient.rank;

public enum RankType {

    BRONZE("退", 47, "§6", 1, "Đồng", 20, 10),

    SILVER("送", 48, "§f", 2, "Bạc", 17, 12),

    GOLD("适", 49, "§e", 3, "Vàng", 15, 14),

    PLANTIUM("逃", 50, "§3", 4, "Bạch kim", 14, 16),

    DIAMOND("逄", 51, "§b", 5, "Kim cương", 12, 18),

    CHALLENGER("逅", 52, "§a", 6, "Thách đấu", 8, 25);

    private final String icon;
    private final int modelData;
    private final String color;
    private final int value;
    private final String name;
    private final int pointUp;
    private final int pointDown;

    RankType(String icon, int modelData, String color, int value, String name, int pointUp, int pointDown) {
        this.icon = icon;
        this.modelData = modelData;
        this.color = color;
        this.value = value;
        this.name = name;
        this.pointUp = pointUp;
        this.pointDown = pointDown;
    }

    public String getIcon() {
        return icon;
    }

    public int getModelData() {
        return modelData;
    }

    public String getColor() {
        return color;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public int getPointUp() {
        return pointUp;
    }

    public int getPointDown() {
        return pointDown;
    }

    public RankType getNext() {
        if (this == CHALLENGER) return null;
        for (RankType t : values()) {
            if (t.getValue() == this.value + 1) return t;
        }
        return null;
    }

    public RankType getPrevious() {
        if (this == BRONZE) return null;
        for (RankType t : values()) {
            if (t.getValue() == this.value - 1) return t;
        }
        return null;
    }

}
