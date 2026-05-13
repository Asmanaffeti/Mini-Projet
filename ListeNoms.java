package pepsmatcher.core;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ListeNoms {
    private String nomListe;
    private List<Nom> tableau;


    public ListeNoms(String nomListe) {
        this.nomListe = nomListe;
        this.tableau = new ArrayList<>();

    }

    public int getID(Nom n) {
        return tableau.indexOf(n);
    }

    public int taille() {
        return tableau.size();
    }

    public String getNomListe() {
        return nomListe;
    }

    public List<Nom> getTableau() {
        return tableau;
    }

    public void ajouterNom(Nom n) {
        tableau.add(n);
    }

    public static ListeNoms lireCSV(String cheminFichier) throws IOException {
        ListeNoms liste = new ListeNoms(cheminFichier);
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(new FileInputStream(cheminFichier), StandardCharsets.UTF_8));
        String ligne = reader.readLine();
        while ((ligne = reader.readLine()) != null) {
            String[] parts = ligne.split(",", 2);
            if (parts.length >= 2) {
                String nom = parts[1].trim().replaceAll("^\"|\"$", "");
                if (!nom.isEmpty()) {
                    liste.ajouterNom(new Nom(nom));
                }
            }
        }
        reader.close();
        return liste;
    }
}
