package pepsmatcher.selectionneur;

import pepsmatcher.core.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SelectionneurSeuil implements SelectionneurResultat {
    private double seuil;
    private ResultatMatch dernierResultat;

    public SelectionneurSeuil(double seuil) {
        this.seuil = seuil;
    }

    public double getSeuil() {
        return seuil;
    }

    public ResultatMatch selectionner(List<NomCompare> nomCompares) {
        NomCompare best = null;
        for (NomCompare nc : nomCompares) {
            if (nc.getScore() >= seuil) {
                if (best == null || nc.getScore() > best.getScore()) {
                    best = nc;
                }
            }
        }
        if (best == null) return null;
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
        nomCompares.sort(Comparator.comparingDouble(NomCompare::getScore).reversed());
        for (NomCompare nc : nomCompares) {
            if (nc.getScore() >= seuil) {
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
        for (Nom n : t) liste.ajouterNom(n);
        return liste;
    }
}
