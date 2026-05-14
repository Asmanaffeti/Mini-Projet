

package pepsmatcher.core;

public class Nom {
    private String nomOriginal;
    private String nomNormalise;

    public Nom(String nomOriginal) {
        this.nomOriginal = nomOriginal;
        this.nomNormalise = nomOriginal;
    }

    public Nom(String nomOriginal, String nomNormalise) {
        this.nomOriginal = nomOriginal;
        this.nomNormalise = nomNormalise;
    }

    public String getNomOriginal() {
        return this.nomOriginal;
    }

    public String getNomNormalise() {
        return this.nomNormalise;
    }

    public int getNbMots() {
        return this.nomNormalise.trim().isEmpty() ? 0 : this.nomNormalise.trim().split("\\s+").length;
    }

    public int getLongueurTotale() {
        return this.nomNormalise.length();
    }

    public String[] getMots() {
        return this.nomNormalise.trim().split("\\s+");
    }

    public String getPremierMot() {
        String[] mots = this.getMots();
        return mots.length > 0 ? mots[0] : "";
    }

    public String getDernierMot() {
        String[] mots = this.getMots();
        return mots.length > 0 ? mots[mots.length - 1] : "";
    }

    public String toString() {
        return this.nomOriginal;
    }
}
