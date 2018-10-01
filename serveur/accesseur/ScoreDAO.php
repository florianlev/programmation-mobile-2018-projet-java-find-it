<?php
include_once "baseDeDonnees.php";
class ScoreDAO
{
    function listerScores()
    {
        $LISTER_SCORES = "SELECT * FROM score";
        global $basededonnees;
        $requeteListeScores = $basededonnees->prepare($LISTER_SCORES);
        $requeteListeScores->execute();
        return $requeteListeScores->fetchAll(PDO::FETCH_OBJ);
    }

    function ajouterScore($score)
    {
        echo "ajouterScore()";
        print_r($score);

        $SQL_AJOUTER_SCORE = "INSERT into score(score, utilisateur_id) VALUES('$score->score','$score->utilisateur_id')";

        echo $SQL_AJOUTER_SCORE;
        global $basededonnees;
        print_r($basededonnees);

        $requeteAjouterScore = $basededonnees->prepare($SQL_AJOUTER_SCORE);
        $reussite = $requeteAjouterScore->execute();

        echo "Code erreur : " . $basededonnees->errorCode();
        echo "Erreurs : ";
        print_r($basededonnees->errorInfo());
        return $reussite;


    }
}
?>