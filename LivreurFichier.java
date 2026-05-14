package pepsmatcher.livreur;

import pepsmatcher.core.ResultatMatch;

import java.io.*;
import java.nio.file.Paths;

public class LivreurFichier implements LivreurResultat {
    private String nomFichier;

    public LivreurFichier(String nomFichier) {
        this.nomFichier = Paths.get(System.getProperty("user.dir"), nomFichier).toString();
        System.out.println("  [Fichier de sortie] " + this.nomFichier);
    }

    public void livrer(ResultatMatch r) {
        if (r == null) return;
        try {
            FileWriter fw = new FileWriter(nomFichier, true);
            fw.write(r.toString() + System.lineSeparator());
            fw.close();
        } catch (IOException e) {
            System.err.println("Erreur ecriture fichier: " + e.getMessage());
        }
    }

    public void setResultat(ResultatMatch r) {
        livrer(r);
    }
}
