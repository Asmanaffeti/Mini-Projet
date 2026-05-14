package pepsmatcher.pretraiteur;

import pepsmatcher.core.Nom;

public class Decouper implements Pretraiteur {

    public Nom normaliser(Nom n) {
        String s = n.getNomNormalise();
        s = s.replace("-", " ").replace("_", " ").replace("'", " ").replace(".", " ");
        s = s.replaceAll("([a-z])([A-Z])", "$1 $2");
        s = s.replaceAll("\\s+", " ").trim();
        return new Nom(n.getNomOriginal(), s);
    }

    public Nom traiter(Nom n) {
        return normaliser(n);
    }
}
