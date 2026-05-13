package pepsmatcher.comparateur;

import pepsmatcher.core.Nom;

import java.util.ArrayList;
import java.util.List;

public class ComparateurChaine implements Comparateur {
    private List<Comparateur> listeComparateur;

    public ComparateurChaine() {
        this.listeComparateur = new ArrayList<>();
    }

    public void ajouterComparateur(Comparateur c) {
        listeComparateur.add(c);
    }

    public double comparerNom(Nom nomGauche, Nom nomDroit) {
        if (listeComparateur.isEmpty()) return 0;
        double total = 0;
        for (Comparateur c : listeComparateur) {
            total += c.comparerNom(nomGauche, nomDroit);
        }
        return total / listeComparateur.size();
    }
}
