package pepsmatcher.selectionneur;

import pepsmatcher.core.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SelectionneurTopN implements SelectionneurResultat {
    private int n;
    private double seuilMinimum;
    private ResultatMatch dernierResultat;

    public SelectionneurTopN(int n) {
        this.n = n;
        this.seuilMinimum = 0.0;
    }

    public SelectionneurTopN(int n, double seuilMinimum) {
        this.n = n;
        this.seuilMinimum = seuilMinimum;
    }

    public int getN() {
        return n;
    }

    public ResultatMatch selectionner(List<NomCompare> nomCompares) {
        if (nomCompares.isEmpty()) return null;
        nomCompares.sort(Comparator.comparingDouble(NomCompare::getScore).reversed());
        NomCompare best = nomCompares.get(0);
        if (best.getScore() < seuilMinimum) return null;
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
        int limite = Math.min(n, nomCompares.size());
        for (int i = 0; i < limite; i++) {
            NomCompare nc = nomCompares.get(i);
            if (nc.getScore() < seuilMinimum) break;
            resultats.add(new ResultatMatch(
                nc.getCouple().getNomOriginal(),
                nc.getCouple().getNomCandidat(),
                fichierSource,
                nc.getScore()
            ));
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
        int max = Math.min(n, t.size());
        for (int i = 0; i < max; i++) liste.ajouterNom(t.get(i));
        return liste;
    }
}
