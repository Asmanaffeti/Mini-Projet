package pepsmatcher.livreur;

import pepsmatcher.core.ResultatMatch;

public class LivreurConsole implements LivreurResultat {
    private ResultatMatch resultat;

    public void livrer(ResultatMatch r) {
        if (r != null) {
            System.out.println(r.toString());
        }
    }

    public void setResultat(ResultatMatch r) {
        this.resultat = r;
    }

    public void afficherConsole() {
        if (resultat != null) {
            System.out.println(resultat.toString());
        }
    }
}
