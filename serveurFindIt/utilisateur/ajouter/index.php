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