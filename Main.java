package pepsmatcher;

import pepsmatcher.comparateur.*;
import pepsmatcher.core.*;
import pepsmatcher.generateur.*;
import pepsmatcher.livreur.*;
import pepsmatcher.pretraiteur.*;
import pepsmatcher.selectionneur.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        afficherBanniere();

        System.out.print("Entrez le nom a rechercher : ");
        String nomManuel = scanner.nextLine().trim();
        if (nomManuel.isEmpty()) {
            System.out.println("  [!] Nom vide, annulation.");
            return;
        }

        String jarPath = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        java.io.File jarFile = new java.io.File(jarPath);
        String repertoire = jarFile.isFile() ? jarFile.getParent() : System.getProperty("user.dir");
        System.out.println("  [Dossier courant] " + repertoire);
        System.out.println("  Copiez vos fichiers CSV dans : " + repertoire);

        List<ListeNoms> listeBases = new ArrayList<>();
        System.out.println("Entrez les fichiers de base un par un (ligne vide pour terminer) :");
        while (true) {
            System.out.print("  Fichier (ex: peps_names_4k.csv) : ");
            String fichier = scanner.nextLine().trim();
            if (fichier.isEmpty()) break;
            java.io.File f = new java.io.File(fichier);
            if (!f.isAbsolute()) {
                f = new java.io.File(repertoire, fichier);
            }
            try {
                System.out.println("  Chargement de " + f.getAbsolutePath() + "...");
                ListeNoms base = ListeNoms.lireCSV(f.getAbsolutePath());
                System.out.println("  -> " + base.taille() + " noms charges.");
                listeBases.add(base);
            } catch (IOException e) {
                System.err.println("  [!] Fichier introuvable: " + f.getAbsolutePath());
                System.err.println("  [!] Verifiez que le fichier est bien dans: " + repertoire);
            }
        }
        if (listeBases.isEmpty()) {
            System.out.println("  [!] Aucun fichier charge, annulation.");
            return;
        }

        Configuration config = new Configuration();

        choisirPretraiteurs(config);
        choisirGenerateur(config);
        boolean soundexActif = config.getPretraiteurs().stream()
            .anyMatch(p -> p instanceof Soundex);
        choisirComparateur(config, soundexActif);
        String typeSelectionneur = choisirSelectionneur(config);
        choisirLivreurs(config);

        afficherRecapitulatif(config);

        System.out.print("\nDemarrer la recherche ? (o/n) : ");
        if (!scanner.nextLine().trim().equalsIgnoreCase("o")) {
            System.out.println("Annule.");
            return;
        }

        MoteurDeRecherche moteur = new MoteurDeRecherche(config);
        System.out.println("\nRecherche en cours...\n");

        double meilleurScore = 0.0;
        for (ListeNoms base : listeBases) {
            System.out.println("--- Recherche dans : " + base.getNomListe() + " ---");
            moteur.lancerParNom(new Nom(nomManuel), base);
            double score = config.getSelectionneur().getScore();
            if (score > meilleurScore) meilleurScore = score;
        }

        System.out.println("\n============================================");
        System.out.println("  FIN DE LA RECHERCHE");
        System.out.println("============================================");
        System.out.println("  Nom recherche  : " + nomManuel);
        System.out.println("  Meilleur score : " + String.format("%.3f", meilleurScore));
        System.out.println("  Fichiers bases : " + listeBases.size());
        System.out.println("============================================");
    }

    static boolean aUnMatch(String typeSelectionneur, double score, Configuration config) {
        if (typeSelectionneur.equals("SEUIL")) {
            return score >= ((SelectionneurSeuil) config.getSelectionneur()).getSeuil();
        }
        return score > 0.0;
    }

    static void afficherBanniere() {
        System.out.println("============================================");
        System.out.println("      PEPs MATCHER - Moteur de noms        ");
        System.out.println("============================================");
        System.out.println();
    }

    static void choisirPretraiteurs(Configuration config) {
        System.out.println("\n--------------------------------------------");
        System.out.println("  PRETRAITEURS  (plusieurs choix possibles)");
        System.out.println("--------------------------------------------");
        System.out.println("  1. RemovePoint   - Supprime les points");
        System.out.println("  2. PreRemarque   - Supprime les parentheses");
        System.out.println("  3. Decouper      - Separe tirets, underscores, casse mixte");
        System.out.println("  4. PreMin        - Mise en minuscules");
        System.out.println("  5. Soundex       - Encodage phonetique");
        System.out.println("     [!] Soundex est toujours applique AVANT PreMin");
        System.out.println("  6. Tous (1+2+3+4 - recommande)");
        System.out.print("Vos choix separes par des virgules (ex: 1,2,3) : ");

        String ligne = scanner.nextLine().trim();
        if (ligne.equals("6")) {
            config.ajouterPretraiteur(new RemovePoint());
            config.ajouterPretraiteur(new PreRemarque());
            config.ajouterPretraiteur(new Decouper());
            config.ajouterPretraiteur(new PreMin());
            return;
        }
        boolean soundexChoisi = false;
        boolean preMinChoisi  = false;
        for (String choix : ligne.split(",")) {
            switch (choix.trim()) {
                case "1": config.ajouterPretraiteur(new RemovePoint());  break;
                case "2": config.ajouterPretraiteur(new PreRemarque());  break;
                case "3": config.ajouterPretraiteur(new Decouper());     break;
                case "4": preMinChoisi  = true; break;
                case "5": soundexChoisi = true; break;
            }
        }
        if (soundexChoisi) config.ajouterPretraiteur(new Soundex());
        if (preMinChoisi)  config.ajouterPretraiteur(new PreMin());
        if (soundexChoisi && preMinChoisi)
            System.out.println("  [OK] Soundex applique avant PreMin.");
    }

    static void choisirGenerateur(Configuration config) {
        System.out.println("\n--------------------------------------------");
        System.out.println("  GENERATEUR  (1 seul)");
        System.out.println("--------------------------------------------");
        System.out.println("  1. GenerateurTous    - Produit cartesien complet (lent sur grandes listes)");
        System.out.println("  2. GenerateurIndex   -Dictionnaire dont les cles sont la longueur et la valeur est une liste de noms correspondants ");
        System.out.println("  3. GenerateurHH      - Index sur 2 premiers caracteres (rapide, recommande)");
        System.out.println("  4. GenerateurSyllabe - Couples par bigrammes communs");
        System.out.print("Votre choix (1-4) : ");

        switch (scanner.nextLine().trim()) {
            case "1": config.setGenerateur(new GenerateurTous());    break;
            case "2": config.setGenerateur(new GenerateurIndex());   break;
            case "4": config.setGenerateur(new GenerateurSyllabe()); break;
            default:  config.setGenerateur(new GenerateurHH());      break;
        }
    }

    static void choisirComparateur(Configuration config, boolean soundexActif) {
        System.out.println("\n--------------------------------------------");
        System.out.println("  COMPARATEUR  (1 seul)");
        System.out.println("--------------------------------------------");
        System.out.println("  1. ComparateurExact      - Egalite stricte (0 ou 1)");
        if (soundexActif) {
            System.out.println("  2. ComparateurLevenstein - [INCOMPATIBLE avec Soundex]");
        } else {
            System.out.println("  2. ComparateurLevenstein - Distance d'edition normalisee");
        }
        System.out.println("  3. ComparateurJaro       - Similarite Jaro-Winkler (recommande)");
        System.out.print("Votre choix (1-3) : ");

        String choix = scanner.nextLine().trim();
        if (soundexActif && choix.equals("2")) {
            System.out.println("  [ERREUR] ComparateurLevenstein est incompatible avec Soundex.");
            System.out.println("  Soundex encode les noms en codes numeriques (ex: M300 A400).");
            System.out.println("  Levenstein sur ces codes ne produit pas de resultats significatifs.");
            System.out.println("  Veuillez choisir ComparateurExact (1) ou ComparateurJaro (3).");
            System.out.print("Votre choix (1 ou 3) : ");
            choix = scanner.nextLine().trim();
        }
        switch (choix) {
            case "1": config.setComparateur(new ComparateurExact());       break;
            default:  config.setComparateur(new ComparateurJaro());        break;
        }
    }

    static String choisirSelectionneur(Configuration config) {
        System.out.println("\n--------------------------------------------");
        System.out.println("  SELECTIONNEUR  (1 seul)");
        System.out.println("--------------------------------------------");
        System.out.println("  1. SelectionneurSeuil      - Garde tous les resultats dont le score >= seuil");
        System.out.println("  2. SelectionneurTopN       - Garde les N meilleurs resultats");
        System.out.println("  3. SelectionneurPourcentage- Garde les X% meilleurs resultats");
        System.out.print("Votre choix (1-3) : ");

        switch (scanner.nextLine().trim()) {
            case "2":
                System.out.print("  Valeur de N (ex: 5) : ");
                int n = lireEntier(5);
                System.out.print("  Seuil minimum entre 0.0 et 1.0 (ex: 0.80) : ");
                double seuilN = lireDouble(0.80);
                config.setSelectionneur(new SelectionneurTopN(n, seuilN));
                return "TOPN";
            case "3":
                System.out.print("  Pourcentage de l'ecart de score (ex: 10) : ");
                double pct = lireDouble(10.0);
                config.setSelectionneur(new SelectionneurPourcentage(pct));
                return "POURCENTAGE";
            default:
                System.out.print("  Seuil minimum, entre 0.0 et 1.0 (ex: 0.7) : ");
                config.setSelectionneur(new SelectionneurSeuil(lireDouble(0.7)));
                return "SEUIL";
        }
    }

    static void choisirLivreurs(Configuration config) {
        System.out.println("\n--------------------------------------------");
        System.out.println("  LIVREURS  (1 ou 2 choix)");
        System.out.println("--------------------------------------------");
        System.out.println("  1. LivreurConsole - Affiche les resultats dans la console");
        System.out.println("  2. LivreurFichier - Sauvegarde les resultats dans un fichier");
        System.out.println("  3. Les deux");
        System.out.print("Votre choix (1-3) : ");

        switch (scanner.nextLine().trim()) {
            case "2":
                config.ajouterLivreur(new LivreurFichier(demanderNomFichier()));
                break;
            case "3":
                config.ajouterLivreur(new LivreurConsole());
                config.ajouterLivreur(new LivreurFichier(demanderNomFichier()));
                break;
            default:
                config.ajouterLivreur(new LivreurConsole());
                break;
        }
    }

    static String demanderNomFichier() {
        System.out.print("  Nom du fichier de sortie (ex: resultats.txt) : ");
        String f = scanner.nextLine().trim();
        return f.isEmpty() ? "resultats.txt" : f;
    }

    static void afficherRecapitulatif(Configuration config) {
        System.out.println("\n============================================");
        System.out.println("  RECAPITULATIF DE LA CONFIGURATION");
        System.out.println("============================================");
        System.out.println("  Pretraiteurs  (" + config.getPretraiteurs().size() + ") :");
        for (Pretraiteur p : config.getPretraiteurs())
            System.out.println("    - " + p.getClass().getSimpleName());
        System.out.println("  Generateur    : " + config.getGenerateur().getClass().getSimpleName());
        System.out.println("  Comparateur   : " + config.getComparateur().getClass().getSimpleName());
        System.out.println("  Selectionneur : " + config.getSelectionneur().getClass().getSimpleName());
        System.out.println("  Livreurs      (" + config.getLivreur().size() + ") :");
        for (LivreurResultat l : config.getLivreur())
            System.out.println("    - " + l.getClass().getSimpleName());
        System.out.println("============================================");
    }

    static double lireDouble(double defaut) {
        try {
            String ligne = scanner.nextLine().trim();
            return ligne.isEmpty() ? defaut : Double.parseDouble(ligne);
        } catch (NumberFormatException e) {
            return defaut;
        }
    }

    static int lireEntier(int defaut) {
        try {
            String ligne = scanner.nextLine().trim();
            return ligne.isEmpty() ? defaut : Integer.parseInt(ligne);
        } catch (NumberFormatException e) {
            return defaut;
        }
    }
}
