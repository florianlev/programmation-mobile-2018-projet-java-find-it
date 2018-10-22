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
            <id><?=($galerie->galerie_id)?></id>
            <url><?=($galerie->url)?></url>
            <utilisateur_id><?=$galerie->utilisateur_id?></utilisateur_id>
        </photo>
        <?php
    }
    ?>
</photos>