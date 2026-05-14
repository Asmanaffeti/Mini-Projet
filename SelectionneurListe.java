package pepsmatcher.selectionneur;

import pepsmatcher.core.ListeNoms;
import pepsmatcher.core.Nom;

import java.util.List;

public interface SelectionneurListe {
    ListeNoms sectionnerListe(List<Nom> t);
}
