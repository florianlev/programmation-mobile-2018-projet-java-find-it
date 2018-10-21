<?php

    if (isset($_FILE['image'])){
        $nomDeBase = $_FILES['image']['name'];     //Le nom original du fichier, comme sur le disque du visiteur (exemple : mon_icone.png).
        
        $nom = "image/".$nomDeBase;
        $resultat = move_uploaded_file($_FILES['Image']['tmp_name'],$nom);
    }
    
    else{
            echo "pas d'image reçue";
    }

?>

