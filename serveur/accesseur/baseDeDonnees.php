<?php 

	$base = "findItDB";
	$hote = "localhost";
	$usager = "webmester";
	$motdepasse = "webmester";
	$dsn = "mysql:dbname=".$base.";host=" . $hote;
	$basededonnees = new PDO($dsn, $usager, $motdepasse);

?>