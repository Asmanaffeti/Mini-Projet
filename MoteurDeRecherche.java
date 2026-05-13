package pepsmatcher.core;

import pepsmatcher.comparateur.Comparateur;
import pepsmatcher.generateur.Generateur;
import pepsmatcher.livreur.LivreurResultat;
import pepsmatcher.pretraiteur.Pretraiteur;
import pepsmatcher.selectionneur.SelectionneurResultat;
import pepsmatcher.selectionneur.SelectionneurTopN;
import pepsmatcher.selectionneur.SelectionneurPourcentage;
import pepsmatcher.selectionneur.SelectionneurSeuil;

import java.util.ArrayList;
import java.util.List;

public class MoteurDeRecherche {
    private Generateur generateur;
    private List<Pretraiteur> listePretaiteur;
    private Comparateur comparateur;
    private SelectionneurResultat selectionneur;
    private List<LivreurResultat> livreur;

    public MoteurDeRecherche(Configuration config) {
        this.generateur = config.getGenerateur();
        this.listePretaiteur = config.getPretraiteurs();
        this.comparateur = config.getComparateur();
        this.selectionneur = config.getSelectionneur();
        this.livreur = config.getLivreur();
    }

    private Nom pretraiter(Nom n) {
        Nom resultat = n;
        for (Pretraiteur p : listePretaiteur) {
            resultat = p.traiter(resultat);
        }
        return resultat;
    }

    private List<NomCompare> calculerScores(Nom nomPre, ListeNoms basePre) {
        ListeNoms rechercheListe = new ListeNoms("recherche");
        rechercheListe.ajouterNom(nomPre);
        List<CoupleDeNom> couples = generateur.generer(rechercheListe, basePre);
        List<NomCompare> nomCompares = new ArrayList<>();
        for (CoupleDeNom couple : couples) {
            double score = comparateur.comparerNom(couple.getNomOriginal(), couple.getNomCandidat());
            nomCompares.add(new NomCompare(couple, score));
        }
        return nomCompares;
    }

    private ListeNoms pretraiterBase(ListeNoms base) {
        ListeNoms basePre = new ListeNoms(base.getNomListe());
        for (Nom n : base.getTableau()) {
            basePre.ajouterNom(pretraiter(n));
        }
        return basePre;
    }

    public void lancer(ListeNoms L1, ListeNoms L2) {
        ListeNoms L1pre = pretraiterBase(L1);
        ListeNoms L2pre = pretraiterBase(L2);
        List<CoupleDeNom> couples = generateur.generer(L1pre, L2pre);
        List<NomCompare> nomCompares = new ArrayList<>();
        for (CoupleDeNom couple : couples) {
            double score = comparateur.comparerNom(couple.getNomOriginal(), couple.getNomCandidat());
            nomCompares.add(new NomCompare(couple, score));
        }
        ResultatMatch resultat = selectionneur.selectionner(nomCompares);
        for (LivreurResultat l : livreur) {
            l.livrer(resultat);
        }
    }

    public void lancerParNom(Nom nomRecherche, ListeNoms base) {
        Nom nomPre = pretraiter(nomRecherche);
        ListeNoms basePre = pretraiterBase(base);
        List<NomCompare> nomCompares = calculerScores(nomPre, basePre);

        List<ResultatMatch> resultats = selectionnerTous(nomCompares, base.getNomListe(), nomRecherche);

        if (resultats.isEmpty()) {
            System.out.println("Aucun match trouve pour: " + nomRecherche.getNomOriginal());
        } else {
            for (ResultatMatch r : resultats) {
                for (LivreurResultat l : livreur) {
                    l.livrer(r);
                }
            }
        }
    }

    private List<ResultatMatch> selectionnerTous(List<NomCompare> nomCompares, String fichierSource, Nom nomOriginal) {
        List<ResultatMatch> resultats = new ArrayList<>();

        if (selectionneur instanceof SelectionneurTopN) {
            List<ResultatMatch> raw = ((SelectionneurTopN) selectionneur).selectionnerTous(nomCompares, fichierSource);
            for (ResultatMatch r : raw) {
                resultats.add(new ResultatMatch(nomOriginal, r.getNomMatch(), fichierSource, r.getScore()));
            }
        } else if (selectionneur instanceof SelectionneurPourcentage) {
            List<ResultatMatch> raw = ((SelectionneurPourcentage) selectionneur).selectionnerTous(nomCompares, fichierSource);
            for (ResultatMatch r : raw) {
                resultats.add(new ResultatMatch(nomOriginal, r.getNomMatch(), fichierSource, r.getScore()));
            }
        } else if (selectionneur instanceof SelectionneurSeuil) {
            List<ResultatMatch> raw = ((SelectionneurSeuil) selectionneur).selectionnerTous(nomCompares, fichierSource);
            for (ResultatMatch r : raw) {
                resultats.add(new ResultatMatch(nomOriginal, r.getNomMatch(), fichierSource, r.getScore()));
            }
        } else {
            ResultatMatch r = selectionneur.selectionner(nomCompares);
            if (r != null) {
                resultats.add(new ResultatMatch(nomOriginal, r.getNomMatch(), fichierSource, r.getScore()));
            }
        }
        return resultats;
    }
}
