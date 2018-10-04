<?php
include "../../accesseur/UtilisateurDAO.php";
$utilisateurDAO = new UtilisateurDAO();
//print_r($scoreDAO);
$listeUtilisateur = $utilisateurDAO->listerUtilisateurs();

?>
<?php
header("Content-type: text/xml");
echo '<?xml version="1.0" encoding="UTF-8"?>';
?>
<utilisateurs>

    <?php
    foreach($listeUtilisateur as $utilisateur)
    {
        ?>
        <utilisateur>
            <utilisateur_id><?=($utilisateur->utilisateur_id)?></utilisateur_id>
            <pseudo><?=($utilisateur->pseudo)?></pseudo>
            <mdp><?=$utilisateur->mdp?></mdp>
            <mail><?=$utilisateur->mail?></mail>

        </utilisateur>
        <?php
    }
    ?>
</utilisateurs>
