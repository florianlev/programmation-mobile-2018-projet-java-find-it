<?php 

	$base = "findItDB";
	$hote = "localhost";
	$usager = "webmestre";
	$motdepasse = "projetFindIt2018";
	$dsn = "mysql:dbname=".$base.";host=" . $hote;
	$basededonnees = new PDO($dsn, $usager, $motdepasse);

?>