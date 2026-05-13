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
        return nomOriginal;
    }

    public String getNomNormalise() {
        return nomNormalise;
    }

    public int getNbMots() {
        return nomNormalise.trim().isEmpty() ? 0 : nomNormalise.trim().split("\\s+").length;
    }

    public int getLongueurTotale() {
        return nomNormalise.length();
    }

    public String[] getMots() {
        return nomNormalise.trim().split("\\s+");
    }

    public String getPremierMot() {
        String[] mots = getMots();
        if  mots.length > 0 {
            return mots[0] : "";
        }
    }
    public String getDernierMot() {
        String[] mots = getMots();
        if mots.length > 0 {
            return mots[mots.length - 1] : "";
        }

    }

    public String toString() {
        return nomOriginal;
    }
}
