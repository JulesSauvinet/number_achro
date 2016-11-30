# number_achro
A simple solver to find the number achromatic of a graph, which is a NP-Comple problem, based on CSP constraints.

/***************************************************************/
</br>
28 novembre 2016

/*** Membres du projet ***/
Bruno Dumas
Gregory Howard
Marine Ruiz
Jules Sauvinet
</br>
REPERTOIRE MAIN</br>
java/benchmark/Test       : Lance la résultion sur le graphe contigence des Etat-Unis. On conseille de regarder ce fichier et de l'éxécuter.</br>
java/solver/Achrosolver   : Implémentation du problème décisionnel : est-ce que le graphe G admet une coloration complete de taille k? </br>
java/solver/Achrosolver_k : Lancement d'une boucle pour trouver en un temps raisonnable la coloration complete maximale</br>
java/graphmodel           : Définition du modèle des graphes</br>
java/utils                : Implémentation de classes utilitaires, notamment le parsing des fichiers texte d'entrée représentant les graphes </br>
resources                 : Fichiers texte d'entrée représentant les graphes</br>

REPERTOIRE TEST</br>
java/test/TestSolver.java : lance des tests sur les graphes du dossier resources avec différentes stratégies
et renvoie les temps d'exécution au format CSV sur la sortie standard</br>
ATTENTION C'EST LONG</br>
</br>
