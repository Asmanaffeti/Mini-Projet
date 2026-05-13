package pepsmatcher.generateur;

import pepsmatcher.core.CoupleDeNom;
import pepsmatcher.core.ListeNoms;
import pepsmatcher.core.Nom;

import java.util.ArrayList;
import java.util.List;

public class GenerateurTous implements Generateur {

    public List<CoupleDeNom> generer(ListeNoms L1, ListeNoms L2) {
        List<CoupleDeNom> couples = new ArrayList<>();
        for (Nom n1 : L1.getTableau()) {
            for (Nom n2 : L2.getTableau()) {
                couples.add(new CoupleDeNom(n1, n2));
            }
        }
        return couples;
    }
}
