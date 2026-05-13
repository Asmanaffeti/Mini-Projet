package pepsmatcher.comparateur;

import pepsmatcher.core.Nom;

public interface Comparateur {
    double comparerNom(Nom nomGauche, Nom nomDroit);
}
