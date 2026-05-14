# PEPs Matcher — Moteur de Recherche de Noms

Projet Java de correspondance de noms (name matching) basé sur une architecture modulaire.

## Membres du groupe

| Nom | Branche Git |
|-----|-------------|
| Mariem El Abed | mariem |
| Eya Joudi | EyaJoudi |
| Chadha Fajraoui | chadhafajraoui |
| Asma Naffeti | AsmaNafati |

## Description

Système de recherche et de correspondance de noms PEPs (Personnes Politiquement Exposées)
basé sur un pipeline modulaire composé de :

- **Prétraiteurs** : normalisation des noms (RemovePoint, PreRemarque, Decouper, PreMin, Soundex)
- **Générateurs** : génération des couples candidats (HH, Syllabe, Index, Tous)
- **Comparateurs** : calcul de similarité (Jaro-Winkler, Levenshtein, Exact)
- **Selectionneurs** : filtrage des résultats (Seuil, TopN, Pourcentage)
- **Livreurs** : affichage des résultats (Console, Fichier)

## Technologies

- Java JDK 21
- IntelliJ IDEA 2026
- Git / GitHub

## Lancement

```bash
java -jar pepsmatcher.jar
```

## Université

Projet réalisé dans le cadre du cours de Programmation Orientée Objet — 2026
