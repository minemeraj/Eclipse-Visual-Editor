<?php
$buildServerCommonFiles = array( 
	"/opt/public/cbi/modeling/includes/buildServer-common.php",
	$_SERVER['DOCUMENT_ROOT'] . "/modeling/includes/buildServer-common.php",
);
$foundBuildServerCommonFile = false;
foreach ($buildServerCommonFiles as $bs)
{
	if (is_file($bs))
	{
		require_once($bs); $foundBuildServerCommonFile = true; break;
	}
}
if (!$foundBuildServerCommonFile)
{
	print "Warning: could not find modeling/includes/buildServer-common.php in _common.php";
}
else
{
	unset($foundBuildServerCommonFile);
}

$defaultProj = "";
unset($_GET["project"]); // pdt has no subprojects, so this should be blank
require_once($_SERVER['DOCUMENT_ROOT'] . "/eclipse.org-common/system/app.class.php"); require_once($_SERVER['DOCUMENT_ROOT'] . "/eclipse.org-common/system/nav.class.php");  require_once($_SERVER['DOCUMENT_ROOT'] . "/eclipse.org-common/system/menu.class.php"); $App = new App(); $Nav = new Nav(); $Menu = new Menu(); include($App->getProjectCommon());
ob_start();

/* config */

/* zips that are allowed to be absent from the downloads page (eg., new ones added mid-stream) */
$extraZips = array();

/* $project => sections/Project Name => (prettyname => filename) */
/* only required if using something other than the default 4; otherwise will be generated */
$dls = array(
	"/" => array( # use "/" because PDT has no parent or child projects/components
		"PDT" => array(
		 	"<acronym title=\"Click to download archived All-In-One p2 Repo Update Site\"><img alt=\"Click to download archived All-In-One p2 Repo Update Site\" src=\"/modeling/images/dl-icon-update-zip.gif\"/> <b style=\"color:green\">All-In-One Update Site</b></acronym>" => "Update",
			"Automated Tests" => "Automated-Tests",
		),
	),
);

/* list of valid file prefixes for projects who have been renamed; keys have leading / to match $proj */
/* only required if using something other than the default; otherwise will be generated */
$filePre = array( # use "/" because PDT has no parent or child projects/components
	"/" => array("VE") // PDT-sdk-*.zip
);

/* define showNotes(), $oldrels, doLanguagePacks() in extras-$proj.php (or just extras.php for flat projects) if necessary, downloads-common.php will include them */
/* end config */

require_once($_SERVER["DOCUMENT_ROOT"] . "/modeling/includes/downloads-common.php");

$html = ob_get_contents();
ob_end_clean();

/* Note: Google Analytics moved to _projectCommon.php so it's on EVERY page */

$pageTitle = "Visual Editor (VE) - Downloads";
$pageKeywords = ""; // TODO: add something here
$pageAuthor = "Nick Boldt";

# Generate the web page
$App->AddExtraHtmlHeader('<link rel="stylesheet" type="text/css" href="/modeling/includes/downloads.css"/>' . "\n");
$App->AddExtraHtmlHeader('<script src="/modeling/includes/downloads.js" type="text/javascript"></script>' . "\n"); //ie doesn't understand self closing script tags, and won't even try to render the page if you use one
$App->generatePage($theme, $Menu, $Nav, $pageAuthor, $pageKeywords, $pageTitle, $html);

?>
