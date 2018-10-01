<?php
include "../../accesseur/GalerieDAO.php";
$galerieDAO = new GalerieDAO();
//print_r($galerieDAO);
$listePhotos = $galerieDAO->listerPhotos();

?>
<?php
header("Content-type: text/xml");
echo '<?xml version="1.0" encoding="UTF-8"?>';
?>
<photos>

    <?php
    foreach($listePhotos as $galerie)
    {
        ?>
        <photo>
            <id><?=($galerie->id_photo)?></id>
            <urlCoordonnees><?=($galerie->urlCoordonnees)?></urlCoordonnees>
            <utilisateur_id><?=$galerie->utilisateur_id?></utilisateur_id>
        </photo>
        <?php
    }
    ?>
</photos>