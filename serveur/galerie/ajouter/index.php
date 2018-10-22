<?php
    include "../../accesseur/GalerieDAO.php";
    $galerieDAO = new GalerieDAO();
    //print_r($galerieDAO);
    $photo = new stdClass();

   

    $nom = $_POST['nom'];
    $image = $_POST['image'];

    $adresse = "http://158.69.113.110/findItServeur/galerie/ajouter/images/".$nom.".jpg";
    echo ($adresse);
    $photo->url = $adresse;

    $listePhotos = $galerieDAO->ajouterPhoto($photo);

    $decodedImage = base64_decode("$image");
    file_put_contents("images/" . $nom. ".jpg", $decodedImage);

?>

