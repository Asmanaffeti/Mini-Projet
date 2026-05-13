package pepsmatcher.core;

import pepsmatcher.generateur.Generateur;
import pepsmatcher.pretraiteur.Pretraiteur;
import pepsmatcher.comparateur.Comparateur;
import pepsmatcher.selectionneur.SelectionneurResultat;
import pepsmatcher.livreur.LivreurResultat;

import java.util.List;
import java.util.ArrayList;

public class Configuration {
    private Generateur generateur;
    private List<Pretraiteur> pretraiteurs;
    private Comparateur comparateurs;
    private SelectionneurResultat selectionneurs;
    private List<LivreurResultat> livreur;

    public Configuration() {
        this.pretraiteurs = new ArrayList<>();
        this.livreur = new ArrayList<>();
    }

    public List<Pretraiteur> getPretraiteurs() {
        return pretraiteurs;
    }

    public List<LivreurResultat> getLivreur() {
        return livreur;
    }

    public void setGenerateur(Generateur g) {
        this.generateur = g;
    }

    public void setComparateur(Comparateur c) {
        this.comparateurs = c;
    }

    public void setSelectionneur(SelectionneurResultat s) {
        this.selectionneurs = s;
    }

    public void ajouterPretraiteur(Pretraiteur p) {
        pretraiteurs.add(p);
    }

    public void ajouterLivreur(LivreurResultat l) {
        livreur.add(l);
    }

    public Generateur getGenerateur() {
        return generateur;
    }

    public Comparateur getComparateur() {
        return comparateurs;
    }

    public SelectionneurResultat getSelectionneur() {
        return selectionneurs;
    }
}
