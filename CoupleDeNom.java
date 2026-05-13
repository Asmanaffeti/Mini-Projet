package pepsmatcher.core;

public class CoupleDeNom {
    private Nom nomOriginal;
    private Nom nomCandidat;

    public CoupleDeNom(Nom nomOriginal, Nom nomCandidat) {
        this.nomOriginal = nomOriginal;
        this.nomCandidat = nomCandidat;
    }

    public Nom getNomOriginal() {
        return nomOriginal;
    }

    public Nom getNomCandidat() {
        return nomCandidat;
    }
}
