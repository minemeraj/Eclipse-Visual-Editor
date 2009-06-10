<?php
require($_SERVER["DOCUMENT_ROOT"] . "/modeling/includes/buildServer-common.php");

$pageTitle = "Visual Editor (VE) - Release Notes";

$streams = array(
	"ve" => array(
		"1.4.x" => "HEAD",
		"1.2.x" => "R1_2_maintenance",
	),
	"vep" => array(
		"1.4.x" => "HEAD",
		"1.2.x" => "R1_2_maintenance",
	),
);

require ($_SERVER["DOCUMENT_ROOT"] . "/modeling/includes/relnotes-common.php");
?>
