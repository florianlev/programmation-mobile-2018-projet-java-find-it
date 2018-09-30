<?php

    include "../../accesseur/UtilisateurDAO";
    $utilisateurDAO = new UtilisateurDAO();

    $utilisateurTableau = $_POST;
    $utilisateur = new stdClass();
	$utilisateur->nom = $utilisateurTableau ['nom'];
	$utilisateur->mail = $utilisateurTableau ['mail'];
    $utilisateur->mdp = $utilisateurTableau ['mdp'];
        
    $succes = $voyagesDAO->ajouterVoyagePourUnVaisseau($voyage);

    ?>
    <?php 

header("Content-type: text/xml");
echo '<?xml version="1.0" encoding="UTF-8"?>';
?>
<action>
	<type>ajouter</type>
	<moment><?=time()?></moment>
	<succes><?=$succes?></succes>
	<message></message>
</action>