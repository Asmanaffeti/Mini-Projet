package pepsmatcher.livreur;

import pepsmatcher.core.ResultatMatch;

public interface LivreurResultat {
    void livrer(ResultatMatch r);
    void setResultat(ResultatMatch r);
}
