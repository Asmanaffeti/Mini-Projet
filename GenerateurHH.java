package pepsmatcher.generateur;

import pepsmatcher.core.CoupleDeNom;
import pepsmatcher.core.ListeNoms;
import pepsmatcher.core.Nom;

import java.util.*;

public class GenerateurHH implements Generateur {

    public List<CoupleDeNom> generer(ListeNoms L1, ListeNoms L2) {
        Map<String, List<Nom>> index = new HashMap<>();
        for (Nom n2 : L2.getTableau()) {
            for (String key : getKeys(n2.getNomNormalise())) {
                index.computeIfAbsent(key, k -> new ArrayList<>()).add(n2);
            }
        }
        Set<String> seen = new HashSet<>();
        List<CoupleDeNom> couples = new ArrayList<>();
        for (Nom n1 : L1.getTableau()) {
            for (String key : getKeys(n1.getNomNormalise())) {
                List<Nom> candidats = index.get(key);
                if (candidats == null) continue;
                for (Nom n2 : candidats) {
                    String k = n1.getNomOriginal() + "|" + n2.getNomOriginal();
                    if (seen.add(k)) couples.add(new CoupleDeNom(n1, n2));
                }
            }
        }
        return couples;
    }

    private Set<String> getKeys(String nom) {
        Set<String> keys = new LinkedHashSet<>();
        String clean = nom.trim().toUpperCase().replaceAll("[^A-Z ]", "");
        if (clean.length() >= 2) keys.add(clean.substring(0, 2));
        if (clean.length() >= 3) keys.add(clean.substring(0, 3));
        String[] mots = clean.split("\\s+");
        for (String mot : mots) {
            if (mot.length() >= 2) keys.add(mot.substring(0, 2));
        }
        return keys;
    }
}
