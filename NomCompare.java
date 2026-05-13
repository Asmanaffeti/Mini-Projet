package pepsmatcher.core;

public class NomCompare {
    private CoupleDeNom couple;
    private double score;

    public NomCompare(CoupleDeNom couple, double score) {
        this.couple = couple;
        this.score = score;
    }

    public double getScore() {
        return score;
    }

    public CoupleDeNom getCouple() {
        return couple;
    }
}
