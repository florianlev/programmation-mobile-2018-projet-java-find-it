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
        $SQL_AJOUTER_PHOTO = "INSERT into galerie(urlCoordonnees, utilisateur_id) VALUES('$galerie->urlCoordonnes','$galerie->utilisateur_id')";
        global $basededonnees;
        $requeteAjouterPhoto = $basededonnees->prepare($SQL_AJOUTER_PHOTO);
        $reussite = $requeteAjouterPhoto->execute();
        return $reussite;


    }
}
?>