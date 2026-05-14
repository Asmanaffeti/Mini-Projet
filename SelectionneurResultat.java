package pepsmatcher.selectionneur;

import pepsmatcher.core.NomCompare;
import pepsmatcher.core.ResultatMatch;

import java.util.List;

public interface SelectionneurResultat extends SelectionneurListe {
    ResultatMatch selectionner(List<NomCompare> nomCompares);
    String getNomMatch();
    String getFichierSource();
    double getScore();
}
