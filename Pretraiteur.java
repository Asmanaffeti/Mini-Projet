package pepsmatcher.pretraiteur;

import pepsmatcher.core.Nom;

public interface Pretraiteur {
    Nom normaliser(Nom n);
    Nom traiter(Nom n);
}
