package pepsmatcher.pretraiteur;

import pepsmatcher.core.Nom;

public class Soundex implements Pretraiteur {

    private String normaliserCaractere(char c) {
        String source = "ÀÁÂÃÄÅàáâãäåÈÉÊËèéêëÌÍÎÏìíîïÒÓÔÕÖòóôõöÙÚÛÜùúûüÝýÇçÑñ";
        String cible  = "AAAAAAaaaaaEEEEeeeeIIIIiiiiOOOOOoooooUUUUuuuuYyCcNn";
        int idx = source.indexOf(c);
        if (idx >= 0 && idx < cible.length()) return String.valueOf(cible.charAt(idx));
        return String.valueOf(c);
    }

    private String nettoyerMot(String mot) {
        StringBuilder sb = new StringBuilder();
        for (char c : mot.toCharArray()) {
            String converti = normaliserCaractere(c);
            char ch = converti.charAt(0);
            if (Character.isLetter(ch)) sb.append(Character.toUpperCase(ch));
        }
        return sb.toString();
    }

    private String soundex(String mot) {
        if (mot == null || mot.isEmpty()) return "0000";
        mot = nettoyerMot(mot);
        if (mot.isEmpty()) return "0000";
        char[] codes = {'0','1','2','3','0','1','2','0','0','2','2','4','5','5','0','1','2','6','2','3','0','1','0','2','0','2'};
        char premier = mot.charAt(0);
        if (premier < 'A' || premier > 'Z') return "0000";
        StringBuilder sb = new StringBuilder();
        sb.append(premier);
        char last = codes[premier - 'A'];
        for (int i = 1; i < mot.length() && sb.length() < 4; i++) {
            char c = mot.charAt(i);
            if (c < 'A' || c > 'Z') continue;
            char code = codes[c - 'A'];
            if (code != '0' && code != last) {
                sb.append(code);
                last = code;
            }
        }
        while (sb.length() < 4) sb.append('0');
        return sb.toString();
    }

    public Nom normaliser(Nom n) {
        String[] mots = n.getNomNormalise().trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String mot : mots) {
            if (mot.isEmpty()) continue;
            if (sb.length() > 0) sb.append(" ");
            sb.append(soundex(mot));
        }
        return new Nom(n.getNomOriginal(), sb.toString());
    }

    public Nom traiter(Nom n) {
        return normaliser(n);
    }
}
