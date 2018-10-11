<?php
include "../../accesseur/ScoreDAO.php";
$scoreDAO = new ScoreDAO();
//print_r($scoreDAO);
$listeScore = $scoreDAO->listerScores();

?>
<?php
header("Content-type: text/xml");
echo '<?xml version="1.0" encoding="UTF-8"?>';
?>
<scores>

    <?php
    foreach($listeScore as $score)
    {
        ?>
        <score>
            <id><?=($score->score_id)?></id>
            <valeur><?=($score->score)?></valeur>
            <utilisateur_id><?=$score->utilisateur_id?></utilisateur_id>
        </score>
        <?php
    }
    ?>
</scores>
