package pepsmatcher.selectionneur;

import pepsmatcher.core.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SelectionneurPourcentage implements SelectionneurResultat {
    private double pourcentage;
    private ResultatMatch dernierResultat;

    public SelectionneurPourcentage(double pourcentage) {
        this.pourcentage = pourcentage;
    }

    public ResultatMatch selectionner(List<NomCompare> nomCompares) {
        if (nomCompares.isEmpty()) return null;
        nomCompares.sort(Comparator.comparingDouble(NomCompare::getScore).reversed());
        NomCompare best = nomCompares.get(0);
        dernierResultat = new ResultatMatch(
            best.getCouple().getNomOriginal(),
            best.getCouple().getNomCandidat(),
            "",
            best.getScore()
        );
        return dernierResultat;
    }

    public List<ResultatMatch> selectionnerTous(List<NomCompare> nomCompares, String fichierSource) {
        List<ResultatMatch> resultats = new ArrayList<>();
        if (nomCompares.isEmpty()) return resultats;

        nomCompares.sort(Comparator.comparingDouble(NomCompare::getScore).reversed());

        double scoreMax = nomCompares.get(0).getScore();
        double scoreMin = nomCompares.get(nomCompares.size() - 1).getScore();
        double ecart = scoreMax - scoreMin;

        double seuilDynamique;
        if (ecart < 0.001) {
            seuilDynamique = scoreMax;
        } else {
            seuilDynamique = scoreMax - (ecart * pourcentage / 100.0);
        }

        for (NomCompare nc : nomCompares) {
            if (nc.getScore() >= seuilDynamique) {
                resultats.add(new ResultatMatch(
                    nc.getCouple().getNomOriginal(),
                    nc.getCouple().getNomCandidat(),
                    fichierSource,
                    nc.getScore()
                ));
            }
        }

        if (!resultats.isEmpty()) dernierResultat = resultats.get(0);
        return resultats;
    }

    public String getNomMatch() {
        return dernierResultat != null ? dernierResultat.getNomMatch().getNomOriginal() : "";
    }

    public String getFichierSource() {
        return dernierResultat != null ? dernierResultat.getFichierSource() : "";
    }

    public double getScore() {
        return dernierResultat != null ? dernierResultat.getScore() : 0.0;
    }

    public ListeNoms sectionnerListe(List<Nom> t) {
        ListeNoms liste = new ListeNoms("selection");
        if (t.isEmpty()) return liste;
        int limite = Math.max(1, (int) Math.ceil(t.size() * pourcentage / 100.0));
        for (int i = 0; i < Math.min(limite, t.size()); i++) liste.ajouterNom(t.get(i));
        return liste;
    }
}
