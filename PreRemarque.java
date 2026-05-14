package pepsmatcher.pretraiteur;

import pepsmatcher.core.Nom;

public class PreRemarque implements Pretraiteur {

    public Nom normaliser(Nom n) {
        String s = n.getNomNormalise().replaceAll("\\(.*?\\)", "").replaceAll("\\s+", " ").trim();
        return new Nom(n.getNomOriginal(), s);
    }

    public Nom traiter(Nom n) {
        return normaliser(n);
    }
}
