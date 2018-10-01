<?php 

	$base = "findItDB";
	$hote = "localhost";
	$usager = "root";
	$motdepasse = "";
	$dsn = "mysql:dbname=".$base.";host=" . $hote;
	$basededonnees = new PDO($dsn, $usager, $motdepasse);

?>