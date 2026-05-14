package pepsmatcher.pretraiteur;

import pepsmatcher.core.Nom;

public class RemovePoint implements Pretraiteur {

    public Nom normaliser(Nom n) {
        String s = n.getNomNormalise().replace(".", " ").replaceAll("\\s+", " ").trim();
        return new Nom(n.getNomOriginal(), s);
    }

    public Nom traiter(Nom n) {
        return normaliser(n);
    }
}
