package pepsmatcher.generateur;

import pepsmatcher.core.CoupleDeNom;
import pepsmatcher.core.ListeNoms;
import pepsmatcher.core.Nom;

import java.util.*;

public class GenerateurSyllabe implements Generateur {

    private List<String> getSyllabes(String mot) {
        List<String> syllabes = new ArrayList<>();
        int len = mot.length();
        for (int i = 0; i + 2 <= len; i++) {
            syllabes.add(mot.substring(i, i + 2));
        }
        return syllabes;
    }

    public List<CoupleDeNom> generer(ListeNoms L1, ListeNoms L2) {
        Map<String, List<Nom>> index = new HashMap<>();
        for (Nom n2 : L2.getTableau()) {
            for (String syllabe : getSyllabes(n2.getNomNormalise().replaceAll("\\s+", ""))) {
                index.computeIfAbsent(syllabe, k -> new ArrayList<>()).add(n2);
            }
        }
        Set<String> seen = new HashSet<>();
        List<CoupleDeNom> couples = new ArrayList<>();
        for (Nom n1 : L1.getTableau()) {
            Set<Nom> candidats = new LinkedHashSet<>();
            for (String syllabe : getSyllabes(n1.getNomNormalise().replaceAll("\\s+", ""))) {
                List<Nom> matches = index.get(syllabe);
                if (matches != null) candidats.addAll(matches);
            }
            for (Nom n2 : candidats) {
                String key = n1.getNomOriginal() + "|" + n2.getNomOriginal();
                if (seen.add(key)) {
                    couples.add(new CoupleDeNom(n1, n2));
                }
            }
        }
        return couples;
    }
}
