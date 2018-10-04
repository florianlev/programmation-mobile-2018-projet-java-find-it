<?php

    include "../../accesseur/UtilisateurDAO.php";
    $utilisateurDAO = new UtilisateurDAO();

    $utilisateurTableau = $_POST;
    $utilisateur = new stdClass();

    $utilisateur->nom = $_GET['pseudo'];
    $utilisateur->mail= $_GET['mail'];
    $utilisateur->mdp = $_GET['mdp'];
	/*$utilisateur->nom = $utilisateurTableau ['pseudo'];
	$utilisateur->mail = $utilisateurTableau ['mail'];
    $utilisateur->mdp = $utilisateurTableau ['mdp'];*/
        
    $succes = $utilisateurDAO->ajouterUtilisateur($utilisateur);

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