<?php
	include_once "baseDeDonnees.php";
	class UtilisateurDAO
	{
		function listerUtilisateurs()
		{			
			$LISTER_UTILISATEUR = "SELECT * FROM utilisateur";
			global $basededonnees;
			$requeteListeUtilisateurs = $basededonnees->prepare($LISTER_UTILISATEUR);
			$requeteListeUtilisateurs->execute();
			return $requeteListeUtilisateurs->fetchAll(PDO::FETCH_OBJ);
        }
        
        function ajouterUtilisateur($utilisateur)
		{
			echo "ajouterUtilisateur()";
			print_r($utilisateur);
			
			$SQL_AJOUTER_UTILISATEUR = "INSERT into utilisateur(pseudo, mail, mdp) VALUES('$utilisateur->nom','$utilisateur->mail','$utilisateur->mdp')";
			
			echo $SQL_AJOUTER_UTILISATEUR;
			global $basededonnees;
			print_r($basededonnees);
			
			$requeteAjouterUtilisateur = $basededonnees->prepare($SQL_AJOUTER_UTILISATEUR);
			$reussite = $requeteAjouterUtilisateur->execute();
			
			echo "Code erreur : " . $basededonnees->errorCode();
			echo "Erreurs : ";
			print_r($basededonnees->errorInfo());
			return $reussite;
		}	

		function verifierConnection($utilisateur){
			$SQL_VERIF_CONNECTION = "SELECT count(utilisateur_id) as nombre FROM utilisateur WHERE pseudo ='$utilisateur->pseudo' AND mdp ='$utilisateur->mdp'";
			global $basededonnees;
			$requeteVerifUtilisateur = $basededonnees->prepare($SQL_VERIF_CONNECTION);
			$requeteVerifUtilisateur->execute();
			return $requeteVerifUtilisateur->fetchAll(PDO::FETCH_OBJ);
		}

		function recupererIdUtilisateur($utilisateur){
			$SQL_RECUP_ID = "SELECT utilisateur_id FROM utilisateur WHERE pseudo ='$utilisateur->pseudo' AND mail ='$utilisateur->mail'";
			global $basededonnees;
			$requeteRecupId = $basededonnees->prepare($SQL_RECUP_ID);
			$requeteRecupId->execute();
			return $requeteRecupId->fetchAll(PDO::FETCH_OBJ);
		}
    }
?>