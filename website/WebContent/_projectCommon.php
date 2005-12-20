<?php

	# Set the theme for your project's web pages.
	# See the Committer Tools "How Do I" for list of themes
	# https://dev.eclipse.org/committers/
	# Optional: defaults to system theme 
	$theme = "";

	# Define your project-wide Nav bars here.
	# Format is Link text, link URL (can be http://www.someothersite.com/), target (_self, _blank), level (1, 2 or 3)
	# these are optional
	$Nav->addNavSeparator("VE Home", 	"http://eclipse.org/vep");
	$Nav->addCustomNav("VE Downloads", 		"http://download.eclipse.org/tools/ve/downloads/index.php", 	"_self", 1);
	$Nav->addCustomNav("Installation", 		"install.php", 		"_self", 2);
	$Nav->addCustomNav("VE FAQ", 				"faq.html", 			"_self", 3);
	$Nav->addCustomNav("Download Archives ", 		"http://archive.eclipse.org/tools/ve/downloads/", 	"_self", 2);


?>
