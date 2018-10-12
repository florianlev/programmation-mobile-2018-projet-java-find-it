<?php

include "../../accesseur/UtilisateurDAO.php";
$utilisateurDAO = new UtilisateurDAO();

$utilisateur = new stdClass();

$utilisateur->pseudo = $_GET['pseudo'];
$utilisateur->mdp = $_GET['mdp'];

    
$idUtilisateur = $utilisateurDAO->recupererIdUtilisateur($utilisateur);
?>
<?php 
header("Content-type: text/xml");
echo '<?xml version="1.0" encoding="UTF-8"?>';
?>  
<Utilisateurs>
<?php
    foreach ($idUtilisateur as $unIdUtilisateur) {
?>
    <utilisateur>
        <id><?=$unIdUtilisateur->utilisateur_id?></id>
    </utilisateur>
<?php
    }
?>
</Utilisateurs>

