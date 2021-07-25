package manaki.plugin.skybattleclient.rank;

public enum RankGrade {

    IV(4),
    III(3),
    II(2),
    I(1);

    private int value;

    RankGrade(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public RankGrade getNext() {
        if (this.value == 1) return null;
        for (RankGrade g : values()) {
            if (g.value == this.value - 1) return g;
        }
        return null;
    }

    public RankGrade getPrevious() {
        if (this.value == 4) return null;
        for (RankGrade g : values()) {
            if (g.value == this.value + 1) return g;
        }
        return null;
    }

}
