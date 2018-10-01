<?php
include_once "baseDeDonnees.php";
class GalerieDAO
{
    function listerPhotos()
    {
        $LISTER_PHOTOS = "SELECT * FROM galerie";
        global $basededonnees;
        $requeteListerPhotos = $basededonnees->prepare($LISTER_PHOTOS);
        $requeteListerPhotos->execute();
        return $requeteListerPhotos->fetchAll(PDO::FETCH_OBJ);
    }

    function ajouterPhoto($galerie)
    {
        echo "ajouterPhoto()";
        print_r($galerie);

        $SQL_AJOUTER_PHOTO = "INSERT into galerie(urlCoordonnees, utilisateur_id) VALUES('$galerie->urlCoordonnes','$galerie->utilisateur_id')";

        echo $SQL_AJOUTER_PHOTO;
        global $basededonnees;
        print_r($basededonnees);

        $requeteAjouterPhoto = $basededonnees->prepare($SQL_AJOUTER_PHOTO);
        $reussite = $requeteAjouterPhoto->execute();

        echo "Code erreur : " . $basededonnees->errorCode();
        echo "Erreurs : ";
        print_r($basededonnees->errorInfo());
        return $reussite;


    }
}
?>