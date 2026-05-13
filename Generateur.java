package pepsmatcher.generateur;

import pepsmatcher.core.CoupleDeNom;
import pepsmatcher.core.ListeNoms;

import java.util.List;

public interface Generateur {
    List<CoupleDeNom> generer(ListeNoms L1, ListeNoms L2);
}
