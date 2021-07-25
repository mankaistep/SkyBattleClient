package manaki.plugin.skybattleclient.rank;

public class RankData {

    private int point;
    private RankType type;
    private RankGrade grade;

    public RankData(int point, RankType type, RankGrade grade) {
        this.point = point;
        this.type = type;
        this.grade = grade;
    }

    public int getPoint() {
        return point;
    }

    public RankType getType() {
        if (this.type == null) this.type = RankType.BRONZE;
        return type;
    }

    public RankGrade getGrade() {
        if (this.grade == null) this.grade = RankGrade.IV;
        return grade;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public void setType(RankType type) {
        this.type = type;
    }

    public void setGrade(RankGrade grade) {
        this.grade = grade;
    }

    public void addPoint(int value) {
        int newpoint = this.point + value;
        if (newpoint >= 100) {
            var nextgrade = this.getGrade().getNext();
            var nexttype = this.getType();

            // If it's grade I
            if (nextgrade == null) {
                nexttype = this.type.getNext();
                nextgrade = RankGrade.IV;

                // If it's challenger
                if (nexttype == null) {
                    this.point = 99;
                    return;
                }
            }

            // New rank type
            this.type = nexttype;
            this.grade = nextgrade;
            this.point = 0;
        }
        else this.point = newpoint;
    }

    public void subtractPoint(int value) {
        int newpoint = this.point - value;
        if (newpoint < 0) {
            var pregrade = this.getGrade().getPrevious();
            var pretype = this.getType();

            // If it's grade IV
            if (pregrade == null) {
                pretype = this.type.getPrevious();
                pregrade = RankGrade.I;

                // If it's challenger
                if (pretype == null) {
                    this.point = 0;
                    return;
                }
            }

            // New rank type
            this.type = pretype;
            this.grade = pregrade;
            this.point = 75;
        }
        else this.point = newpoint;
    }

}
