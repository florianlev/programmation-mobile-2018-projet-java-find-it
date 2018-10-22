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
        $SQL_AJOUTER_PHOTO = "INSERT into galerie(url) VALUES('$galerie->url')";
        echo ($SQL_AJOUTER_PHOTO);
        global $basededonnees;
        $requeteAjouterPhoto = $basededonnees->prepare($SQL_AJOUTER_PHOTO);
        $reussite = $requeteAjouterPhoto->execute();
        return $reussite;


    }
}
?>