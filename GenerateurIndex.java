package pepsmatcher.generateur;

import pepsmatcher.core.CoupleDeNom;
import pepsmatcher.core.ListeNoms;
import pepsmatcher.core.Nom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenerateurIndex implements Generateur {

    public List<CoupleDeNom> generer(ListeNoms L1, ListeNoms L2) {
        Map<Integer, List<Nom>> index = new HashMap<>();
        for (Nom n2 : L2.getTableau()) {
            int longueur = n2.getNomNormalise().length();
            index.computeIfAbsent(longueur, k -> new ArrayList<>()).add(n2);
        }
        List<CoupleDeNom> couples = new ArrayList<>();
        for (Nom n1 : L1.getTableau()) {
            int longueur = n1.getNomNormalise().length();
            List<Nom> candidats = index.get(longueur);
            if (candidats != null) {
                for (Nom n2 : candidats) {
                    couples.add(new CoupleDeNom(n1, n2));
                }
            }
        }
        return couples;
    }
}
