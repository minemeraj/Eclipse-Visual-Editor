<?php
require($_SERVER["DOCUMENT_ROOT"] . "/modeling/includes/buildServer-common.php");

$pageTitle = "Visual Editor (VE) - Release Notes";

$streams = array(
	"ve" => array(
		"1.4.x" => "HEAD",
	),
);

require ($_SERVER["DOCUMENT_ROOT"] . "/modeling/includes/relnotes-common.php");
?>
