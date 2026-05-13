package pepsmatcher.comparateur;

import pepsmatcher.core.Nom;

public class ComparateurJaro implements Comparateur {

    private double jaro(String s1, String s2) {
        if (s1.equals(s2)) return 1.0;
        int len1 = s1.length(), len2 = s2.length();
        if (len1 == 0 || len2 == 0) return 0.0;
        int matchDist = Math.max(len1, len2) / 2 - 1;
        if (matchDist < 0) matchDist = 0;
        boolean[] s1Matches = new boolean[len1];
        boolean[] s2Matches = new boolean[len2];
        int matches = 0, transpositions = 0;
        for (int i = 0; i < len1; i++) {
            int start = Math.max(0, i - matchDist);
            int end = Math.min(i + matchDist + 1, len2);
            for (int j = start; j < end; j++) {
                if (s2Matches[j] || s1.charAt(i) != s2.charAt(j)) continue;
                s1Matches[i] = true;
                s2Matches[j] = true;
                matches++;
                break;
            }
        }
        if (matches == 0) return 0.0;
        int k = 0;
        for (int i = 0; i < len1; i++) {
            if (!s1Matches[i]) continue;
            while (!s2Matches[k]) k++;
            if (s1.charAt(i) != s2.charAt(k)) transpositions++;
            k++;
        }
        double jaroScore = (matches / (double) len1
                + matches / (double) len2
                + (matches - transpositions / 2.0) / matches) / 3.0;
        int prefix = 0;
        int maxPrefix = Math.min(4, Math.min(len1, len2));
        for (int i = 0; i < maxPrefix; i++) {
            if (s1.charAt(i) == s2.charAt(i)) prefix++;
            else break;
        }
        return jaroScore + prefix * 0.1 * (1 - jaroScore);
    }

    private String normaliserAccents(String s) {
        String src = "ÀÁÂÃÄÅàáâãäåÈÉÊËèéêëÌÍÎÏìíîïÒÓÔÕÖòóôõöÙÚÛÜùúûüÝýÇçÑñŽžŠšÐð";
        String dst = "AAAAAAaaaaaEEEEeeeeIIIIiiiiOOOOOoooooUUUUuuuuYyCcNnZzSsDd";
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            int idx = src.indexOf(c);
            sb.append(idx >= 0 && idx < dst.length() ? dst.charAt(idx) : c);
        }
        return sb.toString();
    }

    private double scoreMeilleurMotVsMot(String m1, String[] mots2) {
        double best = 0.0;
        for (String m2 : mots2) {
            double s = jaro(m1, m2);
            if (s > best) best = s;
        }
        return best;
    }

    private double scoreMotAMot(String[] mots1, String[] mots2) {
        if (mots1.length == 0 || mots2.length == 0) return 0.0;
        double total = 0.0;
        double poidsTot = 0.0;
        for (String m1 : mots1) {
            double poids = m1.length() <= 2 ? 0.5 : 1.0;
            total += scoreMeilleurMotVsMot(m1, mots2) * poids;
            poidsTot += poids;
        }
        return poidsTot == 0 ? 0.0 : total / poidsTot;
    }

    private double scoreBiDirectionnel(String[] mots1, String[] mots2) {
        double s1 = scoreMotAMot(mots1, mots2);
        double s2 = scoreMotAMot(mots2, mots1);
        return (s1 + s2) / 2.0;
    }

    public double comparerNom(Nom nomGauche, Nom nomDroit) {
        String g = normaliserAccents(nomGauche.getNomNormalise());
        String d = normaliserAccents(nomDroit.getNomNormalise());

        double scoreChaine = jaro(g, d);

        String[] motsG = g.trim().split("\\s+");
        String[] motsD = d.trim().split("\\s+");
        double scoreMots = scoreBiDirectionnel(motsG, motsD);

        String gFusion = g.replace(" ", "");
        String dFusion = d.replace(" ", "");
        double scoreFusion = jaro(gFusion, dFusion);

        return Math.max(scoreFusion, (scoreChaine * 0.3 + scoreMots * 0.7));
    }
}
