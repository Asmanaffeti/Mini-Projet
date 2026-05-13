package pepsmatcher.comparateur;

import pepsmatcher.core.Nom;

public class ComparateurLevenstein implements Comparateur {

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

    private int distance(String a, String b) {
        int m = a.length(), n = b.length();
        int[][] dp = new int[m + 1][n + 1];
        for (int i = 0; i <= m; i++) dp[i][0] = i;
        for (int j = 0; j <= n; j++) dp[0][j] = j;
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1]));
                }
            }
        }
        return dp[m][n];
    }

    private double scoreMeilleurMot(String m1, String[] mots2) {
        double best = 0.0;
        for (String m2 : mots2) {
            int maxLen = Math.max(m1.length(), m2.length());
            if (maxLen == 0) continue;
            double s = 1.0 - (double) distance(m1, m2) / maxLen;
            if (s > best) best = s;
        }
        return best;
    }

    public double comparerNom(Nom nomGauche, Nom nomDroit) {
        String a = normaliserAccents(nomGauche.getNomNormalise());
        String b = normaliserAccents(nomDroit.getNomNormalise());
        int maxLen = Math.max(a.length(), b.length());
        if (maxLen == 0) return 1.0;
        double scoreChaine = 1.0 - (double) distance(a, b) / maxLen;
        String[] motsA = a.trim().split("\\s+");
        String[] motsB = b.trim().split("\\s+");
        double totalAB = 0.0, totalBA = 0.0;
        for (String m : motsA) totalAB += scoreMeilleurMot(m, motsB);
        for (String m : motsB) totalBA += scoreMeilleurMot(m, motsA);
        double scoreMots = (totalAB / motsA.length + totalBA / motsB.length) / 2.0;
        return scoreChaine * 0.3 + scoreMots * 0.7;
    }
}
