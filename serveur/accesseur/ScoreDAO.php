<?php
include_once "baseDeDonnees.php";
class ScoreDAO
{
    function listerScores()
    {
        $LISTER_SCORES = "SELECT * FROM score ORDER BY score DESC ";
        global $basededonnees;
        $requeteListeScores = $basededonnees->prepare($LISTER_SCORES);
        $requeteListeScores->execute();
        return $requeteListeScores->fetchAll(PDO::FETCH_OBJ);
    }

    function ajouterScore($score)
    {

        $SQL_AJOUTER_SCORE = "INSERT into score(score, utilisateur_id) VALUES('$score->valeur','$score->utilisateur_id')";

        global $basededonnees;

        $requeteAjouterScore = $basededonnees->prepare($SQL_AJOUTER_SCORE);
        $reussite = $requeteAjouterScore->execute();

        return $reussite;


    }
}
?>