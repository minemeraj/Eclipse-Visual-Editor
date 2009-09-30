<?php

$Nav->setLinkList(null);

$PR = "ve";
$projectName = "VE";

$isBuildServer = (preg_match("/^(emft|modeling|build)\.eclipse\.org$/", $_SERVER["SERVER_NAME"]));
$isBuildDotEclipseServer = $_SERVER["SERVER_NAME"] == "build.eclipse.org";
$isWWWserver = (preg_match("/^(?:www.|)eclipse.org$/", $_SERVER["SERVER_NAME"]));
$isEclipseCluster = (preg_match("/^(?:www.||download.|download1.|build.)eclipse.org$/", $_SERVER["SERVER_NAME"]));
$debug = (isset ($_GET["debug"]) && preg_match("/^\d+$/", $_GET["debug"]) ? $_GET["debug"] : -1);
$writableRoot = ($isBuildServer ? $_SERVER["DOCUMENT_ROOT"] . "/modeling/includes/" : "/home/data/httpd/writable/www.eclipse.org/");
$writableBuildRoot = $isBuildDotEclipseServer ? "/opt/public/cbi" : "/home/www-data";

$rooturl = "http://" . $_SERVER["HTTP_HOST"] . "/vep";
$downurl = ($isBuildServer ? "" : "http://www.eclipse.org");
$bugurl = "https://bugs.eclipse.org";

if (isset ($_GET["skin"]) && preg_match("/^(Blue|EclipseStandard|Industrial|Lazarus|Miasma|Modern|OldStyle|Phoenix|PhoenixTest|PlainText|Nova)$/", $_GET["skin"], $regs))
{
	$theme = $regs[1];
}
else
{
	$theme = "Nova";
}

/* projects/components in cvs */
/* "proj" => "cvsname" */
$cvsprojs = array ("ve" => "org.eclipse.ve");

/* sub-projects/components in cvs for projects/components above (if any) */
/* "cvsname" => array("shortname" => "cvsname") */
$cvscoms = array();

$defaultProj = "ve";

$extraprojects = array(); //components with only downloads, no info yet, "prettyname" => "directory"
$nodownloads = array(); //components with only information, no downloads, or no builds available yet, "projectkey"
$nonewsgroup = array(); //components without newsgroup
$nomailinglist = array(); //components without mailinglist
$incubating = array(); // components which are incubating - EMF will never have incubating components -- see EMFT
$nomenclature = "Project"; //are we dealing with "components" or "projects"?

include_once $_SERVER["DOCUMENT_ROOT"] . "/modeling/includes/scripts.php";

$regs = null;
$proj = "/ve";
$projct= preg_replace("#^/#", "", $proj);

$buildtypes = array(
	"R" => "Release",
	"S" => "Stable",
	"I" => "Integration",
	"M" => "Maintenance",
	"N" => "Nightly"
);

$Nav->addCustomNav("About This Project", "/projects/project_summary.php?projectid=tools.ve", "", 1);
$Nav->addNavSeparator("Visual Editor (VE)", "$rooturl/");

$Nav->addNavSeparator("Downloads", "$downurl/vep/downloads/");
$Nav->addCustomNav("Installation", "http://wiki.eclipse.org/VE/Installing", "_self", 2);
$Nav->addCustomNav("Update Manager", "http://wiki.eclipse.org/VE/Update", "_self", 2);

$Nav->addNavSeparator("Documentation", "http://wiki.eclipse.org/VE/Documentation");
$Nav->addCustomNav("Documentation", "http://wiki.eclipse.org/VE/Documentation", "_self", 2);
$Nav->addCustomNav("FAQ", "http://www.eclipse.org/vep/WebContent/faq.html", "_self", 2);
#$Nav->addCustomNav("Plan", "http://www.eclipse.org/projects/project-plan.php?projectid=tools.ve", "_self", 2);

#$Nav->addCustomNav("Release Notes", $rooturl . "/news/relnotes.php?project=$projct&amp;version=HEAD", "_self", 2);

$Nav->addNavSeparator("Community", "http://wiki.eclipse.org/VE");
$Nav->addCustomNav("Wiki",		"http://wiki.eclipse.org/VE", "_self", 2);
$Nav->addCustomNav("Newsgroup Search", "http://www.eclipse.org/newsportal/thread.php?group=eclipse.tools.ve", "_self", 2);
$collist = "%26query_format%3Dadvanced&amp;column_changeddate=on&amp;column_bug_severity=on&amp;column_priority=on&amp;column_rep_platform=on&amp;column_bug_status=on&amp;column_product=on&amp;column_component=on&amp;column_version=on&amp;column_target_milestone=on&amp;column_short_short_desc=on&amp;splitheader=0";
#$Nav->addCustomNav("Open Bugs", "$bugurl/bugs/colchange.cgi?rememberedquery=product%3D" . $projectName . "%26bug_status%3DNEW%26bug_status%3DASSIGNED%26bug_status%3DREOPENED%26bug_status%3DRESOLVED%26resolution%3DFIXED%26resolution%3D---%26order%3Dbugs.bug_status%2Cbugs.target_milestone%2Cbugs.bug_id" . $collist, "_self", 2);
$Nav->addCustomNav("Open Bugs", "$bugurl/bugs/colchange.cgi?rememberedquery=product%3D" . $projectName . "%26bug_status%3DNEW%26bug_status%3DASSIGNED%26bug_status%3DREOPENED%26order%3Dbugs.bug_status%2Cbugs.target_milestone%2Cbugs.bug_id" . $collist, "_self", 2);
$Nav->addCustomNav("Submit A Bug", "$bugurl/bugs/enter_bug.cgi?product=" . $projectName, "_self", 2);

#$Nav->addCustomNav("Contributors", "http://www.eclipse.org/vep/project-info/team.php", "_self", 2);
$Nav->addCustomNav("IP Log", "http://www.eclipse.org/projects/ip_log.php?projectid=tools.ve", "_self", 2);

$App->AddExtraHtmlHeader("<link rel=\"stylesheet\" type=\"text/css\" href=\"/modeling/includes/common.css\"/>\n");
addGoogleAnalyticsTrackingCodeToHeader("UA-2566337-9");
$App->Promotion = TRUE;
?>
