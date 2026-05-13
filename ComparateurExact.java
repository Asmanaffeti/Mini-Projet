package pepsmatcher.comparateur;

import pepsmatcher.core.Nom;

public class ComparateurExact implements Comparateur {

    public double comparerNom(Nom nomGauche, Nom nomDroit) {
        return nomGauche.getNomNormalise().equals(nomDroit.getNomNormalise()) ? 1.0 : 0.0;
    }
}
