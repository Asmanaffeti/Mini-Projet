package pepsmatcher.pretraiteur;

import pepsmatcher.core.Nom;

public class PreMin implements Pretraiteur {

    private String normaliserAccents(String s) {
        String src = "ÀÁÂÃÄÅàáâãäåÈÉÊËèéêëÌÍÎÏìíîïÒÓÔÕÖòóôõöÙÚÛÜùúûüÝýÇçÑñŽžŠšÐð";
        String dst = "aaaaaaeeeeeiiiiooooouuuuycnnzzssd";
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            int idx = src.indexOf(c);
            sb.append(idx >= 0 && idx < dst.length() ? dst.charAt(idx) : c);
        }
        return sb.toString();
    }

    public Nom normaliser(Nom n) {
        return new Nom(n.getNomOriginal(), normaliserAccents(n.getNomNormalise().toLowerCase()));
    }

    public Nom traiter(Nom n) {
        return normaliser(n);
    }
}
